package org.jgraph.graph.MDPModel;

import org.jgrapht.graph.Edge;
import org.jgrapht.graph.Vertex;

public class MDPEdge{

    MDPVertex src;
    MDPVertex dst;

    public String getId() {
        return id;
    }

    private String id;
    BlockingStatus status;
    Double blockingProbability;
    Double reward;

    public Double getReward() {
        return reward;
    }

    private void setID(){
         this.id=this.src.getId()+"_"+this.dst.getId();
    }

    public static MDPEdge mdpeFromEdge(Edge edge){
        return new MDPEdge(new MDPVertex(edge.getSource()),
                new MDPVertex((Vertex)edge.getTarget()),
                edge.getBlockingProbability(),
                edge.getReward(),
                null // status should be set on runtime!
                );
    }

    public MDPEdge(MDPVertex src,
                   MDPVertex dst,
                   Double prob,
                   Double reward,
                   BlockingStatus status){

            this.src = src;
            this.dst = dst;
            this.blockingProbability = prob;
            this.reward = reward;
            this.status = status;
            this.setID();
    }

    @Override
    public String toString(){
        return this.getId();
    }

}