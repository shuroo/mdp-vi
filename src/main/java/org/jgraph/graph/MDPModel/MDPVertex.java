package org.jgraph.graph.MDPModel;// Generic action source for mdp model

import org.jgrapht.graph.Vertex;

public class MDPVertex {

    public String getId() {
        return id;
    }

    private  String id;

    private boolean isFinal = false;

    public boolean isFinal() {
        return isFinal;
    }

    public MDPVertex(String id, boolean isFinal) {
        this.id = id;
        this.isFinal = isFinal;
    }

    public MDPVertex(Vertex vert) {
        this.id = vert.toString();
        this.isFinal = vert.isFinal();
    }

    @Override
    public java.lang.String toString(){
        return id;
    }
}
