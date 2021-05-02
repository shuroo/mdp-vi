package org.jgraph.graph.MDPModel;

public class MDPStatusEdge {
    MDPEdge edge;
    BlockingStatus status;

    public MDPStatusEdge(MDPEdge edge, BlockingStatus status){
        this.edge = edge;
        this.status = status;
    }

    @Override
    public String toString(){
        return  ","+edge.toString()+"::"+status.toString();
    }
}
