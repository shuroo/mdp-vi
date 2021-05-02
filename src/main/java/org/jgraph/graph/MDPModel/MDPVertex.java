package org.jgraph.graph.MDPModel;// Generic action source for mdp model

public class MDPVertex {

    public java.lang.String getId() {
        return id;
    }

    private java.lang.String id;

    public MDPVertex(String id) {
        this.id = id;
    }

    @Override
    public java.lang.String toString(){
        return id;
    }
}
