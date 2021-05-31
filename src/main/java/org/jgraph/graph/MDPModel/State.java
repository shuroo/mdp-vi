package org.jgraph.graph.MDPModel;

import logic.CollectionUtils;

import java.util.Iterator;
import java.util.Vector;

public class State  {


    public String getStateId() {
        return stateId;
    }

    private String stateId;

    public void setAgentLocation(MDPVertex agentLocation) {
        this.agentLocation = agentLocation;
        setStateId();

    }

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

    private Double previousUtility = 1000.0;
    private Double minimalUtility = 0.0;

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

    private void setStateId() {
        StringBuilder uniqueStateStr = new StringBuilder();
        uniqueStateStr.append("Ag_Location::"+this.agentLocation+",");
        Iterator<MDPStatusEdge> statusIterator  = edgeStatuses.iterator();
        while(statusIterator.hasNext()) {
            uniqueStateStr.append(statusIterator.next().toString());
            if(statusIterator.hasNext()){
                uniqueStateStr.append(",");
            }
        }
        stateId = uniqueStateStr.toString();
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
        CollectionUtils cu = new CollectionUtils();
        this.edgeStatuses = edgeStatusVector;
        this.stateProbability = stateProbability;
        setStateId();
    }

    // print states properly
    @Override
    public String toString(){

        return "<"+this.getStateId()+">";
    }

}
