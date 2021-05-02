package org.jgraph.graph.MDPModel;

public class Transition {

    // get the states conditioned prob
    public Double getTransition(MDP mdp, State s1, State s2){
        if(mdp.actionExists(s1,s2) && s1.stateProbability != null && s2.stateProbability != null){
            return Math.floor(s1.stateProbability / s2.stateProbability);
        }
        else return 0.0;
    }
}
