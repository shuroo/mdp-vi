package GraphToMDP;

import org.jgraph.graph.MDPModel.*;
import org.jgrapht.graph.Edge;
import org.jgrapht.graph.EdgeStatus;
import org.jgrapht.graph.Graph;
import org.jgrapht.graph.Vertex;

import java.util.*;
import java.util.stream.Collectors;

public class GraphReader {

    HashMap<String, State> states;

    public static MDP GraphToMDP(Graph g) {
        HashMap<String, Action> actions = new HashMap<String, Action>();
        HashMap<String, State> states = new HashMap<String, State>();

        List<Edge> edges = (List<Edge>) g.getEdges().values().stream().collect(Collectors.toList());
        for (Edge edge : edges) {
            MDPEdge mdpe = MDPEdge.mdpeFromEdge(edge);
            Action action = new Action(mdpe);
            actions.put(action.getActionId(), action);
        }

        List<LinkedList<MDPStatusEdge>> allStatesNoAgentL = generateStatusesByEdges((List<Edge>) g.getEdges().values().stream().collect(Collectors.toList()));
               // new HashMap<String,State>());

        // Append agent locations...
        HashSet<State> allStates = new HashSet<State>();
//        for(Object v :  g.getVertices().values()) {
//            HashSet<State> allStates = allStatesNoAgentL.stream().map(
//                    state -> {
//                        state.setAgentLocation(new MDPVertex(v.toString()));
//                        return (state.getStateId(), state);
//                    }).collect(Collectors.toSet());
//        }


        for(LinkedList<MDPStatusEdge> id : allStatesNoAgentL){
            System.out.println(id);
            System.out.println(id.size());
        }
        System.out.println("States::"+allStatesNoAgentL.size()+"::" + allStates.size());
        // todo: convert list to hashmap
        return new MDP(actions, states);
    }

    private static  List<LinkedList<MDPStatusEdge>> generateStatusesByEdges(List<Edge> edges) {

        List<LinkedList<MDPStatusEdge>> edgeStatusesToCombine = new LinkedList< LinkedList<MDPStatusEdge> >();
        for(Edge edge : edges){
            //Edge edge = edges.remove(i);


                if(edgeStatusesToCombine.isEmpty()){

                    HashMap<String,State> edgeStatuses =  generateAllStatusesFromEdge(edge);
                    // create a list of single status element (*3)
                    for(State singleStatus : edgeStatuses.values()) {
                        LinkedList<MDPStatusEdge> edgeSingleStatusList = new LinkedList<MDPStatusEdge>();
                        Vector<MDPStatusEdge> status = singleStatus.getEdgeStatuses(); // edge+O OR edge+C OR edge+U
                        edgeSingleStatusList.addAll(status);// make sure this adds only one!! status elemen
                        edgeStatusesToCombine.add(edgeSingleStatusList);
                    }

                }
                else{
                    List<LinkedList<MDPStatusEdge>> statusestoCreate = new LinkedList<LinkedList<MDPStatusEdge>>();
                    for(int j=0;j<edgeStatusesToCombine.size();j++){
                        LinkedList<MDPStatusEdge> oldStatus = edgeStatusesToCombine.remove(j);
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


        // todo: write with states and probs instad of statuses only...
/*    private static HashMap<String,State> generateStatusesByEdges(List<Edge> edges, HashMap<String,State> statesUnderConstruction) {
        if (edges.isEmpty()) {
            return statesUnderConstruction;
        } else {


            Edge edge = edges.remove(0);
            HashMap<String,State> edgeStates =  generateAllStatusesFromEdge(edge);
            //for(Edge edge : edges){

            if(statesUnderConstruction.isEmpty()){
                return generateStatusesByEdges(edges, edgeStates);
            }

                // Find all combinations!
                // ()()

            Vector<MDPStatusEdge> updatedStatusSingleState = new Vector<MDPStatusEdge>();
            HashMap<String,State> resultingStates = new HashMap<String, State>();
            for(State edgeState : edgeStates.values()){

                    statesUnderConstruction.values().forEach(prevState -> {

                            updatedStatusSingleState.addAll(prevState.getEdgeStatuses());
                            updatedStatusSingleState.addAll(edgeState.getEdgeStatuses());
                            State mixedState = new State(null, updatedStatusSingleState, edgeState.getStateProbability() * prevState.getStateProbability());
                            resultingStates.put(mixedState.getStateId(),mixedState);
                        });


                }


                return generateStatusesByEdges(edges, resultingStates);

            }


    }*/



    private static HashMap<String,State> generateAllStatusesFromEdge(Edge edge){

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

    public static void main(String[] args) {

        /// My Default Graph Example
        // SnapshotRunner sr = new SnapshotRunner("default_graph_input.json");

        /// First Graph Example
        // SnapshotRunner sr = new SnapshotRunner("graphs_data/default_graph_input.json");

        /// Dror's first Graph Example
        //Graph gr = new Graph("graphs_data/default_graph_input.json");

        Graph gr = new Graph("graphs_data/very_basic_mdp_example_graphs/small_graph_81_states.json");
        // "default_graph_input.json"
        GraphReader.GraphToMDP(gr);
        /// Dror's second Graph Example
        // SnapshotRunner sr = new SnapshotRunner("graphs_data/dror_data/second_graph.json");

        /// Dror's third Graph Example
        // SnapshotRunner sr = new SnapshotRunner("graphs_data/dror_data/third_graph.json");


        System.out.println("---Today!!---");
    }

}
