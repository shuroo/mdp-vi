package org.jgraph.graph.MDPModel;

import java.util.Vector;

public class State {


    public String getStateId() {
        return stateId;
    }

    private String stateId;

    public MDPVertex getAgentLocation() {
        return agentLocation;
    }

    private MDPVertex agentLocation;
    private Action bestAction = null;

    public Double getPreviousUtility() {
        return previousUtility;
    }

    public void setPreviousUtility(Double previousUtility) {
        this.previousUtility = previousUtility;
    }

    private Double previousUtility = 10000000000.0;
    private Double minimalUtility = 10000000000.0;

    public Double getStateProbability() {
        return stateProbability;
    }

    // The probability to occur - based on which edges are currently opened or closed in the current state and thier probsbilities.
    Double stateProbability;

    public Vector<MDPStatusEdge> getEdgeStatuses() {
        return edgeStatuses;
    }

    // Vector of edgeStatuses
    Vector<MDPStatusEdge> edgeStatuses;

    private void buildStateId() {
        StringBuilder uniqueStateStr = new StringBuilder();
        uniqueStateStr.append("Ag_Location::"+this.agentLocation+",");
        for (MDPStatusEdge status : edgeStatuses) {
            uniqueStateStr.append(status.toString()+",");
        }
        //UUID uuid = UUID.fromString(uniqueStateStr.toString());
        stateId = Constants.statePrefix + uniqueStateStr.toString();
    }


    public Double getUtility() {
        return minimalUtility;
    }

    public void setUtility(Double minimalUtility) {
        this.minimalUtility = minimalUtility;
    }

    public Action getBestAction() {
        return bestAction;
    }

    public void setBestAction(Action bestAction) {
        this.bestAction = bestAction;
    }


    public State(MDPVertex agentLocation, Vector<MDPStatusEdge> edgeStatusVector, Double stateProbability) {
        this.agentLocation = agentLocation;
        this.edgeStatuses = edgeStatusVector;
        this.stateProbability = stateProbability;
        buildStateId();
    }


}