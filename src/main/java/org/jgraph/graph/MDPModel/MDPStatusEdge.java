package org.jgraph.graph.MDPModel;

public class MDPStatusEdge {

    private MDPEdge edge;
    private BlockingStatus status;


    public MDPEdge getEdge() {
        return edge;
    }

    public String getId() {
        return edge.getId();
    }

    public BlockingStatus getStatus() {
        return status;
    }


    public MDPStatusEdge(MDPEdge edge, BlockingStatus status){
        this.edge = edge;
        this.status = status;
    }

    @Override
    public String toString(){
        return  "|"+edge.toString()+"::"+status.toString()+"|";
    }

}
