package org.jgraph.graph.MDPModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
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


    public MDP setOptimalPolicy(MDP currentMDP) {

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
                    Double stateUtility;
                    // init minimal utility: if in destination, set the current reward..
                    if(currentState.getAgentLocation().isFinal()){
                        stateUtility = action.getReward();
                    }
                    else {

                        Double joinedprob = 0.0;
                        Double utilityOther;
                        Double rewardCurrentOther = action.getReward();
                        if(other.getStateProbability() == 0.0){
                            joinedprob = 0.0;
                        }
                        else {

                            joinedprob = currentState.stateProbability / other.stateProbability;
                        }
                         utilityOther = other.getUtility();
                        // U(s)i+1 <- max(MDPModel.Action)sigma(P(s/s',a)(R(s,s',a)+U(s'))
                        stateUtility = joinedprob *  (rewardCurrentOther +  utilityOther);

                    }

                    if (!actionUtilities.containsKey(action)) {
                        actionUtilities.put(action, 0.0);
                    }
                    //Double prevActionUtility = actionUtilities.get(action);
                    actionUtilities.put(action, stateUtility);

                }

            }


            // todo: put in method
            // find minimal utility

            Action minimalAction = null;
            Double minimalUtility= null;

            for(Action action: actionUtilities.keySet()) {
                if (currentState.getAgentLocation().getId() == action.getSrc().getId()) {

                    Double actionUtility = actionUtilities.get(action);
                    if(minimalAction == null || minimalUtility > actionUtility){
                        minimalAction = action;
                        minimalUtility = actionUtility;
                    }

                }
            }


            System.out.println("----current state:"+currentState+"-----");
            System.out.println("----Minimal action:"+minimalAction+"-----"+minimalUtility);

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

            // Ui+1<-R(S)+discount*Min(P(s|s') * U(s'))
            Double minimalUtilityWithReward = minimalAction.getReward()+ discountFactor *minimalUtility;
            currentState.setUtility(minimalUtilityWithReward);
            currentState.setBestAction(minimalAction);

        }

        System.out.println(currentMDP.states.values().stream().map(state->state.getBestAction()).collect(Collectors.toList()));
        return currentMDP;
    }
}