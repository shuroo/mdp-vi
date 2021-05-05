package logic;

import org.jgraph.graph.MDPModel.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionUtils<T> {


    public  List<T> flattenList(List<Set<T>> nestedList){
        List<T> flatList = new ArrayList<T>();
        nestedList.forEach(flatList::addAll);
        return flatList;
    }

    public  Map<String,State> listToMap(List<State> states){
        return states.stream()
                .collect(Collectors.toMap(State::getStateId, state -> state));
    }
}
