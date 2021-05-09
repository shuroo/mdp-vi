package logic;

import org.jgraph.graph.MDPModel.MDPStatusEdge;
import org.jgraph.graph.MDPModel.State;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtils<T> {


    public  List<T> flattenList(List<Set<T>> nestedList){
        List<T> flatList = new ArrayList<T>();
        nestedList.forEach(flatList::addAll);
        return flatList;
    }

    public static Map<String,State> stateToMap(Collection<State> states){
        return states.stream()
                .collect(Collectors.toMap(State::getStateId, state -> state));
    }

    public static Map<String, MDPStatusEdge> mdpEdgeToMap(Collection<MDPStatusEdge> edges){
        return edges.stream()
                .collect(Collectors.toMap(MDPStatusEdge::getId, state -> state));
    }
}
