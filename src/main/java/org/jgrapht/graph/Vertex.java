package org.jgrapht.graph;

import java.util.HashSet;

public class Vertex<T> {

    private java.lang.String identifier;

    private Boolean isInitial;

    private Boolean isFinal;

    public Boolean isInitial() {
        return isInitial;
    }

    public Boolean isFinal() {
        return isFinal;
    }

    public java.lang.String getIdentifier() {
        return identifier;
    }

    public Vertex(String id){
        this.identifier = id;
    }

    public HashSet<Edge> vertexEdges = null;

    public Vertex(java.lang.String identifier, Boolean isInitial, Boolean isFinal){

        this.identifier = identifier;
        this.isInitial = isInitial;
        this.isFinal = isFinal;
        this.vertexEdges = new HashSet<Edge>();
    }

}
