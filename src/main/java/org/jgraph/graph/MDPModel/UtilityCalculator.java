package org.jgraph.graph.MDPModel;

import logic.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class UtilityCalculator {


    private Double maxLambda = 10000000000.0;
    Integer iterationCounter = 0;

    private Double epsilon = 0.1;
    // 0 to 1...
    private Double discountFactor = 0.1;


    public UtilityCalculator(Double epsilon, Double discountFactor){
        this.discountFactor = discountFactor;
        this.epsilon = epsilon;
    }
    public void setDiscountFactor(Double discountFactor) {
        this.discountFactor = discountFactor;
    }



//    public MDP setOptimalPolicy(MDP currentMDP) {
//
//        // For each state:
//
//        HashMap<Action, Double> actionUtilities = new HashMap();
//        for (State currentState : currentMDP.getStates().values()) {
//
//            iterationCounter++;
//
//            String stateId = currentState.getStateId();
//
//
//            for (State other : currentMDP.getStates().values()) {
//                if (other.getStateId() == stateId || (!currentMDP.actionExists(currentState, other))) {
//                    continue;
//                } else {
//
//                    Action action = currentMDP.getActionByStates(currentState, other);
//                    Double stateUtility;
//                    // init minimal utility: if in destination, set the current reward..
//                    if(currentState.getAgentLocation().isFinal()){
//                        stateUtility = action.getReward();
//                    }
//                    else {
//
//                        Double joinedprob = 0.0;
//                        Double utilityOther;
//                        Double rewardCurrentOther = action.getReward();
//                        if(other.getStateProbability() == 0.0){
//                            joinedprob = 0.0;
//                        }
//                        else {
//
//                            joinedprob = currentState.stateProbability / other.stateProbability;
//                        }
//                         utilityOther = other.getUtility();
//                        // U(s)i+1 <- max(MDPModel.Action)sigma(P(s/s',a)(R(s,s',a)+U(s'))
//                        stateUtility = joinedprob *  (rewardCurrentOther +  utilityOther);
//
//                    }
//
//                    if (!actionUtilities.containsKey(action)) {
//                        actionUtilities.put(action, 0.0);
//                    }
//                    //Double prevActionUtility = actionUtilities.get(action);
//                    actionUtilities.put(action, stateUtility);
//
//                }
//
//            }
//
//
//            // todo: put in method
//            // find minimal utility
//
//            HashMap<Action,Double> minimalActionWithUtility = findMinimalAction(actionUtilities,currentState);
//            Action minimalAction = (Action)minimalActionWithUtility.keySet().toArray()[0];
//            Double minimalUtility= minimalActionWithUtility.get(minimalAction);
//
//
//            Double stopCondition = epsilon * (1 - discountFactor) / discountFactor;
//
//
//            Double prevUtility = currentState.getPreviousUtility();
//            Double diffUtility = Math.abs(minimalUtility - prevUtility);
//            // max diff per ALL states ... //
//            if (maxLambda > diffUtility) {
//                maxLambda = diffUtility;
//            }
//            if (maxLambda < stopCondition) {
//                break;
//            }
//
//            // todo: set utility for state per iteration. Ui+1(s) how?...
//            // U(s)i+1 <- max(MDPModel.Action)sigma(P(s/s',a)(R(s,s',a)+U(s'))
//            // todo: calc delta. how?
//
//
//            //  find all actions of this source;
//            //  calc their joined tohelet for Reward(src.agentLoc,dst.agentLoc) + Ui(s')
//            // return new MDPModel.MDP recuresively for each stage
//            // until |Ui+1(s) - Ui(s)|<epsilon
//
//
//            //prevMDP = currentMDP;
//
//            Double previousUtility = currentState.getUtility();
//            currentState.setPreviousUtility(previousUtility);
//
//            // Ui+1<-R(S)+discount*Min(P(s|s') * U(s'))
//            Double minimalUtilityWithReward = minimalAction.getReward()+ discountFactor *minimalUtility;
//            currentState.setUtility(minimalUtilityWithReward);
//            currentState.setBestAction(minimalAction);
//
//            System.out.println("----current state:"+currentState+"-----");
//           // System.out.println("----Minimal action:"+minimalAction+"-----minimalUtility:"+minimalUtility);
//
//        }
//
//        System.out.println(currentMDP.states.values().stream().map(state->state.getBestAction()).collect(Collectors.toList()));
//        return currentMDP;

    public MDP setOptimalPolicy(MDP currentMDP) {

        Double stopCondition = epsilon * (1 - discountFactor) / discountFactor;
        // For each state:
        while (maxLambda >= stopCondition) {

            setUtilitiesForStatesIteration(currentMDP.actionsMap.values().stream().collect(Collectors.toList()),
                    currentMDP.states.values().stream().collect(Collectors.toSet()));


            // Check diff to stop...
            for(State state :currentMDP.states.values().stream().collect(Collectors.toSet())){

                Double minimalUtility = state.getUtility();
                Double prevUtility = state.getPreviousUtility();
                System.out.println("Utility found for state: and bestAction: is: ");
                Double diffUtility = Math.abs(minimalUtility - prevUtility);
                // max diff per ALL states ... //
                if (maxLambda > diffUtility) {
                    maxLambda = diffUtility;
                }
                if (maxLambda < stopCondition) {
                    System.out.println("Stopping at lambda:"+maxLambda);
                    break;
                }

            }

        }
        return currentMDP;

    }

//    private HashMap<Action,Double> findMinimalAction(HashMap<Action,Double> actionUtilities,State currentState){
//        Action minimalAction = null;
//        Double minimalUtility= null;
//
//        for(Action action: actionUtilities.keySet()) {
//            if (currentState.getAgentLocation().getId() == action.getSrc().getId()) {
//
//                Double actionUtility = actionUtilities.get(action);
//                if(minimalAction == null || minimalUtility > actionUtility){
//                    minimalAction = action;
//                    minimalUtility = actionUtility;
//                }
//
//            }
//        }
//
//        HashMap<Action,Double> results = new HashMap<Action,Double>();
//
//        System.out.println(minimalAction+"|||"+minimalUtility);
//        results.put(minimalAction,minimalUtility);
//        return results;
//    }

    /**
     * Method to return list of actions with updated utility.
     * @param allstates - states to calc utility from
     * @return updated utilities on actions with sorted list.
     * action_utility <-- 0 if final_state, else Sigma(p(s|s')*U'(s'))
     */
    private List<Action> setActionsUtility(List<Action> allActions, Collection<State> allstates){

            // Init & Build Map<ActionId,State>
            HashMap<String,List<State>> actionSourcesMap = new HashMap<String,List<State>>();
            HashMap<String,List<State>> actionDestinationsMap = new HashMap<String,List<State>>();
            for(Action action : allActions){
                actionSourcesMap.put(action.getSrc().getId(),new LinkedList<State>());
                actionDestinationsMap.put(action.getDst().getId(),new LinkedList<State>());
            }
            for(State state: allstates){
                String stateLocID = state.getAgentLocation().getId();
                if(actionSourcesMap.containsKey(stateLocID)){
                    actionSourcesMap.get(stateLocID).add(state);
                }
                if(actionDestinationsMap.containsKey(stateLocID)){
                    actionDestinationsMap.get(stateLocID).add(state);
                }
            }

            // Stage 1: create map<Action,List<State>> to  find all states belonging to an action.
            for(Action act: allActions){

                    List<State> actionSourceStates = actionSourcesMap.get(act.getSrc().getId());
                    List<State> actionDestStates = actionDestinationsMap.get(act.getDst().getId());
                    Double actionLocalUtility =0.0;
                    // Business Logic:
                    for(State sourceState : actionSourceStates){
                        for(State destState : actionDestStates){
                            actionLocalUtility+=calcStatesUtility(sourceState, destState);
                        }
                }
                    act.setActionUtility(actionLocalUtility);
            }

            allActions.sort(Action::compareTo);

            return allActions;
    }

    // U(s) <- R(s) + Sigma[ P(s|s')*U(s') ]
    private Double calcStatesUtility(State source, State dest){
        Double utility = 0.0;
        if(source.getAgentLocation().isFinal()){
            return utility;
        }
        else{
            Double prob = 0.0;
            Double sourceStProb = source.getStateProbability();
            Double destStProb = dest.getStateProbability();
            if(sourceStProb != 0.0 && destStProb != 0.0){
                prob = (double) Math.round((sourceStProb / destStProb)*100/100);
            }

            // Utility for the two states to add to the action.
            // U(action) <-- Sigma[ P(s|s')*U(s') ]
            Double actionSubUtility = prob*(dest.getUtility());
            // we DON'T set the source utility at this point YET! choosing minimum.
            return actionSubUtility;
        }
    }
    /**
     * Method to set utility per iteration for all states.
     * @param bestActions
     * @return
     */
    private Set<State> setUtilitiesForStatesIteration(List<Action> bestActions, Set<State> allStates){
        List<Action> updatedActionsUtility = setActionsUtility(bestActions,allStates);
//
//        updatedActionsUtility.stream().filter(action -> action.getSrc().getId().equals("v2")).collect(Collectors.toList()).forEach(action->
//            System.out.println("++++Actions FILTERED set utility:"+action));
        for(State state : allStates){
            setUtilitySingleState(state, updatedActionsUtility);
        }

        return allStates;
    }

    private void setUtilitySingleState(State state, List<Action> allActions){

        Set<Action> stateActionsFiltered =
                allActions.stream().filter(action -> action.getSrc().getId().equals(state.getAgentLocation().getId())).collect(Collectors.toSet());
        Action minimalUtilityAction = null;
        Double minimalUtility = 0.0;

       if(!stateActionsFiltered.isEmpty()){
           minimalUtilityAction = (Action) stateActionsFiltered.toArray()[stateActionsFiltered.size()-1];
           minimalUtility = minimalUtilityAction.actionUtility;
       }

        System.out.println("++++Minimal utility action to set for state:"+state.getStateId()+" is :"+minimalUtilityAction);
        state.setPreviousUtility(state.getUtility());
        state.setUtility(minimalUtility);
        state.setBestAction(minimalUtilityAction);
    }
}