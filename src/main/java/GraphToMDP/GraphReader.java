package GraphToMDP;

import org.jgraph.graph.MDPModel.*;
import org.jgrapht.graph.Edge;
import org.jgrapht.graph.Graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
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

        List<State> allStates = generateStatusesByEdges((List<Edge>) g.getEdges().values().stream().collect(Collectors.toList()),
                new LinkedList<State>());

        System.out.println("States::::" + allStates.size());
        // todo: convert list to hashmap
        return new MDP(actions, states);
    }

    // todo: write with states and probs instad of statuses only...
    private static List<State> generateStatusesByEdges(List<Edge> edges, List<State> statesUnderConstruction) {
        if (edges.isEmpty()) {
            return statesUnderConstruction;
        } else {

            List<State> initialStates = new LinkedList<State>();
            Edge edge = edges.remove(0);
            //for(Edge edge : edges){

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

            if (statesUnderConstruction.isEmpty()) {
                initialStates.add(st1);
                initialStates.add(st2);
                initialStates.add(st3);
                return generateStatusesByEdges(edges, initialStates);
            } else {

                // Find all combinations!
                // ()()
                for (State prevState : statesUnderConstruction) {
                    for (MDPStatusEdge prevStatuses : prevState.getEdgeStatuses()) {
                        Vector<MDPStatusEdge> updatedStatus = new Vector<MDPStatusEdge>();
                        updatedStatus.add(prevStatuses);
                        updatedStatus.addAll(st1.getEdgeStatuses());
                        State newSt = new State(null, updatedStatus, st1.getStateProbability() * prevState.getStateProbability());

                        Vector<MDPStatusEdge> updatedStatus2 = new Vector<MDPStatusEdge>();
                        updatedStatus2.add(prevStatuses);
                        updatedStatus2.addAll(st2.getEdgeStatuses());
                        State newSt2 = new State(null, updatedStatus2, st2.getStateProbability() * prevState.getStateProbability());

                        Vector<MDPStatusEdge> updatedStatus3 = new Vector<MDPStatusEdge>();
                        updatedStatus3.add(prevStatuses);
                        updatedStatus3.addAll(st3.getEdgeStatuses());
                        State newSt3 = new State(null, updatedStatus3, st3.getStateProbability() * prevState.getStateProbability());


                        initialStates.add(newSt);
                        initialStates.add(newSt2);
                        initialStates.add(newSt3);

                    }
                }
                return generateStatusesByEdges(edges, initialStates);

            }

        }
    }

    public static void main(String[] args) {

        /// My Default Graph Example
        // SnapshotRunner sr = new SnapshotRunner("default_graph_input.json");

        /// First Graph Example
        // SnapshotRunner sr = new SnapshotRunner("graphs_data/default_graph_input.json");

        /// Dror's first Graph Example
        //Graph gr = new Graph("graphs_data/default_graph_input.json");

        Graph gr = new Graph("graphs_data/very_basic_mdp_example_graphs/very_simple_example_18_states.json");
        // "default_graph_input.json"
        GraphReader.GraphToMDP(gr);
        /// Dror's second Graph Example
        // SnapshotRunner sr = new SnapshotRunner("graphs_data/dror_data/second_graph.json");

        /// Dror's third Graph Example
        // SnapshotRunner sr = new SnapshotRunner("graphs_data/dror_data/third_graph.json");


        System.out.println("---Today!!---");
    }

}
