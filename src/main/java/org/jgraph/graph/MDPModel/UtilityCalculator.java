package org.jgraph.graph.MDPModel;

import logic.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class UtilityCalculator {


    private Double maxLambda = 10000000000.0;

    private Double epsilon = 0.1;
    // 0 to 1...
    private Double discountFactor = 0.1;


    public UtilityCalculator(Double epsilon, Double discountFactor) {
        this.discountFactor = discountFactor;
        this.epsilon = epsilon;
    }

    public void setDiscountFactor(Double discountFactor) {
        this.discountFactor = discountFactor;
    }

    public MDP setOptimalPolicy(MDP currentMDP) {

        Integer iterationCounter = 0;
        Double stopCondition = epsilon * (1 - discountFactor) / discountFactor;

        while (maxLambda >= stopCondition) {

            iterationCounter++;
            setUtilitiesForStatesIteration(currentMDP.actionsMap.values().stream().collect(Collectors.toList()),
                    currentMDP.states.values().stream().collect(Collectors.toSet()));


            // Check diff to stop...
            for (State state : currentMDP.states.values().stream().collect(Collectors.toSet())) {

                Double minimalUtility = state.getUtility();
                Double prevUtility = state.getPreviousUtility();
                //    System.out.println("Utility found for state:"+state.getStateId()+" and bestAction:"+state.getBestAction()+" is: "+state.getUtility());
                Double diffUtility = Math.abs(minimalUtility - prevUtility);
                // max diff per ALL states ... //
                if (maxLambda > diffUtility) {
                    maxLambda = diffUtility;
                }

                if (maxLambda < stopCondition) {

                    System.out.println("Stopping at lambda:" + maxLambda);
                    break;
                }


//                if (stoppedStatesCounter >= (3 * currentMDP.states.size())) {
//
//                    System.out.println("Stopping at lambda:" + maxLambda + ",With iteration counter:" + iterationCounter);
//                    break;
//                }

            }

            currentMDP.getStates().values().stream().forEach(state -> {
                System.out.println("State:" + state.toString());
            });

        }

        return currentMDP;
    }

    /**
     * Method to return list of actions with updated utility.
     *
     * @param allstates - states to calc utility from
     * @return updated utilities on actions with sorted list.
     * action_utility <-- 0 if final_state, else Sigma(p(s|s')*U'(s'))
     */
    private List<Action> setActionsUtility(List<Action> allActions, Collection<State> allstates) {

        // Init & Build Map<ActionId,State>
        HashMap<String, List<State>> actionSourcesMap = new HashMap<String, List<State>>();
        HashMap<String, List<State>> actionDestinationsMap = new HashMap<String, List<State>>();
        for (Action action : allActions) {
            actionSourcesMap.put(action.getSrc().getId(), new LinkedList<State>());
            actionDestinationsMap.put(action.getDst().getId(), new LinkedList<State>());
        }
        for (State state : allstates) {
            String stateLocID = state.getAgentLocation().getId();
            if (actionSourcesMap.containsKey(stateLocID)) {
                actionSourcesMap.get(stateLocID).add(state);
            }
            if (actionDestinationsMap.containsKey(stateLocID)) {
                actionDestinationsMap.get(stateLocID).add(state);
            }
        }

        // Stage 1: create map<Action,List<State>> to  find all states belonging to an action.
        for (Action act : allActions) {

            List<State> actionSourceStates = actionSourcesMap.get(act.getSrc().getId());
            List<State> actionDestStates = actionDestinationsMap.get(act.getDst().getId());
            Double actionLocalUtility = 0.0;
            // Business Logic:
            for (State sourceState : actionSourceStates) {
                for (State destState : actionDestStates) {
                    actionLocalUtility += calcStatesUtility(sourceState, destState);
                }
            }
            act.setActionUtility(actionLocalUtility);
        }

        allActions.sort(Action::compareTo);

        return allActions;
    }

    // U(s) <- R(s) + Sigma[ P(s|s')*U(s') ]
    private Double calcStatesUtility(State source, State dest) {
        Double utility = 0.0;
        if (source.getAgentLocation().isFinal()) {
            return utility;
        } else {
            Double prob = 0.0;
            Double sourceStProb = source.getStateProbability();
            Double destStProb = dest.getStateProbability();
            if (sourceStProb != 0.0 && destStProb != 0.0) {
                prob = (sourceStProb / destStProb);
                if(prob > 1.0){
                    prob = 1.0;
                }
            }

            // Utility for the two states to add to the action.
            // U(action) <-- Sigma[ P(s|s')*U(s') ]
            Double actionSubUtility = prob * (dest.getUtility());
            // we DON'T set the source utility at this point YET! choosing minimum.
            return actionSubUtility;
        }
    }

    /**
     * Method to set utility per iteration for all states.
     *
     * @param bestActions
     * @return
     */
    private Set<State> setUtilitiesForStatesIteration(List<Action> bestActions, Set<State> allStates) {
        List<Action> updatedActionsUtility = setActionsUtility(bestActions, allStates);

        for (State state : allStates) {
            setUtilitySingleState(state, updatedActionsUtility);
        }

        return allStates;
    }

    private void setUtilitySingleState(State state, List<Action> allActions) {

        Set<Action> stateActionsFiltered =
                allActions.stream().filter(action -> action.getSrc().getId().equals(state.getAgentLocation().getId())).collect(Collectors.toSet());
        Action minimalUtilityAction = null;
        Double minimalUtility = 0.0;

        if (!stateActionsFiltered.isEmpty()) {
            Integer actionIndex = stateActionsFiltered.size() - 1;
            minimalUtilityAction =findMinimalUnblockedAction(state, actionIndex,
                    stateActionsFiltered);

            // U(s) <- R(a) + Utility(a)
            minimalUtility = minimalUtilityAction != null ? (minimalUtilityAction.getReward() +  minimalUtilityAction.actionUtility) : 0.0;
        }

        state.setPreviousUtility(state.getUtility());
        //minimalUtility = CollectionUtils.roundTwoDigits(minimalUtility);
        state.setUtility(minimalUtility);
        state.setBestAction(minimalUtilityAction);
    }

    private Action findMinimalUnblockedAction(State state, Integer actionIndex, Set<Action> stateActionsFiltered) {
        Vector<MDPStatusEdge> stateStatusedEdges = state.edgeStatuses;
        Action currentAction = (Action) stateActionsFiltered.toArray()[actionIndex];
        Map<String, MDPStatusEdge> statuses = CollectionUtils.mdpEdgeToMap(stateStatusedEdges);
        while (actionIndex > 0 && statuses.get(currentAction.getActionId()).getStatus() == BlockingStatus.Closed) {
            actionIndex--;
            currentAction = (Action) stateActionsFiltered.toArray()[actionIndex];
        }
        // IF found no possible valid action due to action blockings...
        if (actionIndex == 0 && statuses.get(currentAction.getActionId()).getStatus() == BlockingStatus.Closed) {
            //System.out.println("Found no valid action due to action blockings - returning null! For state:" + state.getStateId());
            return null;
        }
        return currentAction;
    }
}