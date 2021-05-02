package org.jgraph.graph.MDPModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UtilityCalculator {


    private Double maxLambda = 10000000000.0;
    private Double epsilon = 0.1;

    public void setDiscountFactor(Double discountFactor) {
        this.discountFactor = discountFactor;
    }

    // 0 to 1...
    Double discountFactor = 0.1;
    Integer iterationCounter = 0;

    public MDP findOptimalPolicy(MDP currentMDP) {

        // For each state:

        HashMap<Action, Double> actionUtilities = new HashMap();
        for (State currentState : currentMDP.getStates().values()) {

            iterationCounter++;

            String stateId = currentState.getStateId();


            for (State other : currentMDP.getStates().values()) {
                if (other.getStateId() == stateId || (!currentMDP.actionExists(currentState, other))) {
                    continue;
                } else {
                    Action action = currentMDP.getActionByStates(currentState, other);
                    Double rewardCurrentOther = action.getReward();
                    Double utilityOther = other.getUtility();
                    Double joinedprob = 0.0;
                    try {
                        joinedprob = currentState.stateProbability / other.stateProbability;
                    } catch (Exception e) {
                        System.out.println("Failed to fetch probability for States:" + currentState.stateProbability + "," + other.stateProbability);
                    }
                    // U(s)i+1 <- max(MDPModel.Action)sigma(P(s/s',a)(R(s,s',a)+U(s'))
                    Double statesUtility = joinedprob * (rewardCurrentOther + utilityOther);
                    if (!actionUtilities.containsKey(action)) {
                        actionUtilities.put(action, 0.0);
                    }
                    Double prevActionUtility = actionUtilities.get(action);
                    actionUtilities.put(action, prevActionUtility + statesUtility);

                }

            }

            // todo: sort and return minimalActionUtility.

            // LinkedHashMap preserve the ordering of elements in which they are inserted
            LinkedHashMap<Action, Double> sortedActionsAsc = new LinkedHashMap<>();

            actionUtilities.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEachOrdered(x -> sortedActionsAsc.put(x.getKey(), x.getValue()));

            //System.out.println("Sorted Map   : " + sortedActionsAsc);
            Action minimalAction = (Action) sortedActionsAsc.keySet().toArray()[0];
            Double minimalUtility = (Double) sortedActionsAsc.values().toArray()[0];

            Double stopCondition = epsilon * (1 - discountFactor) / discountFactor;


            Double prevUtility = currentState.getPreviousUtility();
            Double diffUtility = Math.abs(minimalUtility - prevUtility);
            // max diff per ALL states ... //
            if (maxLambda > diffUtility) {
                maxLambda = diffUtility;
            }
            if (maxLambda < stopCondition) {
                break;
            }

            // todo: set utility for state per iteration. Ui+1(s) how?...
            // U(s)i+1 <- max(MDPModel.Action)sigma(P(s/s',a)(R(s,s',a)+U(s'))
            // todo: calc delta. how?


            //  find all actions of this source;
            //  calc their joined tohelet for Reward(src.agentLoc,dst.agentLoc) + Ui(s')
            // return new MDPModel.MDP recuresively for each stage
            // until |Ui+1(s) - Ui(s)|<epsilon


            //prevMDP = currentMDP;

            Double previousUtility = currentState.getUtility();
            currentState.setPreviousUtility(previousUtility);
            currentState.setUtility(minimalUtility);
            currentState.setBestAction(minimalAction);

        }

        System.out.println(currentMDP.states.values().stream().map(state->state.getBestAction()).collect(Collectors.toList()));
        return currentMDP;
    }
}