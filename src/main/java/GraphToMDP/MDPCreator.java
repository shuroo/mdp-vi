package GraphToMDP;

import org.jgraph.graph.MDPModel.*;
import org.jgrapht.graph.Edge;
import org.jgrapht.graph.Graph;
import org.jgrapht.graph.Vertex;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MDPCreator {

    Graph graph;

    public MDPCreator(Graph g){
        graph = g;
    }

    protected Set<State> generateStatesFromStatus( List<MDPStatusEdge> statusList){

        Double blockingProb = calcStateBlockingProbability(statusList);
        Set<State> resultingStates = (Set<State>)graph.getVertices().keySet().stream().map(vert->{
                MDPVertex mdpVert = new MDPVertex((String)vert);
                Vector<MDPStatusEdge> statusVector = new Vector<MDPStatusEdge>();
                statusVector.addAll(statusList);
                return new State(mdpVert,statusVector,blockingProb);
        }).collect(Collectors.toSet());
        return resultingStates;
    }

    private Double calcStateBlockingProbability(List<MDPStatusEdge> statusList) {
        Double stateBlockingProbability = 1.0;
        for (MDPStatusEdge status : statusList) {
            Edge graphEdge = (Edge) graph.getEdges().get(status.getEdge().getId());
            BlockingStatus blStatus = status.getStatus();
            // business logic:
            if (blStatus == BlockingStatus.Opened) {
                stateBlockingProbability = stateBlockingProbability * (1.0 - graphEdge.getBlockingProbability());
            } else if (blStatus == BlockingStatus.Closed) {
                stateBlockingProbability = stateBlockingProbability * graphEdge.getBlockingProbability();
            } else continue;
        }

        // Round:
        stateBlockingProbability = (double)Math.round(stateBlockingProbability * 100 ) / 100;

        System.out.println("BlockingProb for list:"+statusList.toString()+" should be:"+stateBlockingProbability);
        return stateBlockingProbability;
    }

    protected  HashMap<String, Action> edgesToActions(){

        HashMap<String, Action> actions = new HashMap<String, Action>();
        List<Edge> edges = (List<Edge>) graph.getEdges().values().stream().collect(Collectors.toList());
        for (Edge edge : edges) {
            MDPEdge mdpe = MDPEdge.mdpeFromEdge(edge);
            Action action = new Action(mdpe);
            actions.put(action.getActionId(), action);
        }

        return actions;
    }

    protected   List<LinkedList<MDPStatusEdge>> generateStatusesByEdges(List<Edge> edges) {

        List<LinkedList<MDPStatusEdge>> edgeStatusesToCombine = new LinkedList< LinkedList<MDPStatusEdge> >();
        for(Edge edge : edges){
            //Edge edge = edges.remove(i);


            if(edgeStatusesToCombine.isEmpty()){

                HashMap<String, State> edgeStatuses =  generateAllStatusesFromEdge(edge);

                // create a list of single status element (*3)
                for(State singleStatus : edgeStatuses.values()) {
                    LinkedList<MDPStatusEdge> edgeSingleStatusList = new LinkedList<MDPStatusEdge>();
                    Vector<MDPStatusEdge> status = singleStatus.getEdgeStatuses(); // edge+O OR edge+C OR edge+U
                    edgeSingleStatusList.addAll(status);// make sure this adds only one!! status elemen
                    edgeStatusesToCombine.add(edgeSingleStatusList);
                }

                System.out.println("Initial edge statuses:"+edgeStatuses.size()+",,edgeStatusesToCombine:"+edgeStatusesToCombine.size());

            }
            else{
                List<LinkedList<MDPStatusEdge>> statusestoCreate = new LinkedList<LinkedList<MDPStatusEdge>>();
                for(LinkedList<MDPStatusEdge> oldStatus: edgeStatusesToCombine){
                    HashMap<String,State> edgeStatuses =  generateAllStatusesFromEdge(edge);

                    for(State singleStatus : edgeStatuses.values()) {
                        LinkedList<MDPStatusEdge> edgeSingleStatusList = new LinkedList<MDPStatusEdge>();
                        edgeSingleStatusList.addAll(oldStatus);
                        Vector<MDPStatusEdge> status = singleStatus.getEdgeStatuses(); // edge+O OR edge+C OR edge+U
                        edgeSingleStatusList.addAll(status);// make sure this adds only one!! status elemen
                        statusestoCreate.add(edgeSingleStatusList);
                    }
                }
                edgeStatusesToCombine = statusestoCreate;
            }
        }

        return edgeStatusesToCombine;
    }

    protected static HashMap<String,State> generateAllStatusesFromEdge(Edge edge){

        MDPEdge mdpedge = MDPEdge.mdpeFromEdge(edge);
        MDPStatusEdge es = new MDPStatusEdge(mdpedge, BlockingStatus.Closed);

        // (e1,c)p=...,(e1,o),(e1,u)

        Vector<MDPStatusEdge> st1_v = new Vector<MDPStatusEdge>();
        st1_v.add(es);
        State st1 = new State(null, st1_v, edge.getBlockingProbability());

        Vector<MDPStatusEdge> st2_v = new Vector<MDPStatusEdge>();
        MDPStatusEdge es2 = new MDPStatusEdge(mdpedge, BlockingStatus.Opened);
        st2_v.add(es2);
        State st2 = new State(null, st2_v, (1 - edge.getBlockingProbability()));

        Vector<MDPStatusEdge> st3_v = new Vector<MDPStatusEdge>();
        MDPStatusEdge es3 = new MDPStatusEdge(mdpedge, BlockingStatus.Unknown);
        st3_v.add(es3);
        State st3 = new State(null, st3_v, 1.0);


        HashMap<String,State> newStatesToConcat = new HashMap<String,State>();
        newStatesToConcat.put(st1.getStateId(),st1);
        newStatesToConcat.put(st2.getStateId(),st2);
        newStatesToConcat.put(st3.getStateId(),st3);

        return newStatesToConcat;
    }

}
