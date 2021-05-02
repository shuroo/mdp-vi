package org.jgraph.graph.MDPModel;

import java.util.HashMap;

public class MDP   {

    // init with map: ActionId,MDPModel.Action
    HashMap<String,Action> actionsMap;

    HashMap<String, State> states;

    public HashMap<String, State> getStates() {
        return states;
    }

    public HashMap<String, Action> getActions() {
        return actionsMap;
    }

    public Action getActionByStates(State st, State other){
        return actionsMap.get(st.getAgentLocation().getId() + "_" + other.getAgentLocation().getId());
    }

    public boolean actionExists(State current, State other) {
        return actionsMap.containsKey(current.getAgentLocation().getId() + "_" + other.getAgentLocation().getId());
    }

    public MDP(HashMap<String, Action> actions, HashMap<String, State> states){
        this.actionsMap = actions;
        this.states = states;
    }

//
//    public static void main(String[] args){
//
//        // MDPModel.MDP for trivial graph..
//
//        MDPVertex s = new MDPVertex("s");
//        MDPVertex t = new MDPVertex("t");
//        HashSet<MDPVertex> vertices = new HashSet<MDPVertex> ();
//        vertices.add(s);
//        vertices.add(t);
//        HashMap<String,Action> actions = new HashMap<String,Action> ();
//
//        // todo: when moving to graph representation,
//        //  the reward and probability should derive from the edges.
//        MDPEdge[] edges = new MDPEdge[2];
//        MDPEdge e1 =new MDPEdge(s,t,0.3,5.0);
//        MDPEdge e2 =new MDPEdge(t,s,0.5,3.0);
//
//        edges[0]= e1;
//        edges[1] = e2;
//        Action a1 = new Action(e1);
//        Action a2 = new Action(e2);
//
//        actions.put(a1.getActionId(),a1);
//        actions.put(a2.getActionId(),a2);
//        MDPStatusEdge[][] edgesStatuses = new MDPStatusEdge[8][2];
//        edgesStatuses[0][0] = new MDPStatusEdge(e1, BlockingStatus.Opened);
//        edgesStatuses[0][1] = new MDPStatusEdge(e2,BlockingStatus.Unknown);
//
//        edgesStatuses[1][0] = new MDPStatusEdge(e1,BlockingStatus.Opened);
//        edgesStatuses[1][1] = new MDPStatusEdge(e2,BlockingStatus.Opened);
//
//        edgesStatuses[2][0] = new MDPStatusEdge(e2,BlockingStatus.Closed);
//        edgesStatuses[2][1] = new MDPStatusEdge(e2,BlockingStatus.Unknown);
//
//        edgesStatuses[3][0] = new MDPStatusEdge(e2,BlockingStatus.Closed);
//        edgesStatuses[3][1] = new MDPStatusEdge(e2,BlockingStatus.Closed);
//
//        edgesStatuses[4][0] = new MDPStatusEdge(e2,BlockingStatus.Unknown);
//        edgesStatuses[4][1] = new MDPStatusEdge(e2,BlockingStatus.Opened);
//
//        edgesStatuses[5][0] = new MDPStatusEdge(e2,BlockingStatus.Unknown);
//        edgesStatuses[5][1] = new MDPStatusEdge(e2,BlockingStatus.Unknown);
//
//        edgesStatuses[6][0] = new MDPStatusEdge(e2,BlockingStatus.Unknown);
//        edgesStatuses[6][1] = new MDPStatusEdge(e2,BlockingStatus.Closed);
//
//        edgesStatuses[7][0] = new MDPStatusEdge(e2,BlockingStatus.Opened);
//        edgesStatuses[7][1] = new MDPStatusEdge(e2,BlockingStatus.Closed);
//
//        // s,o,u
//        State s1 = new State(s,edgesStatuses[0],0.7);
//
//        // t,o,o
//        State s2= new State(t,edgesStatuses[1],0.35);
//
//        // t,o,u
//        State s3 = new State(t,edgesStatuses[0],0.7);
//
//        // s,o,o
//        State s4= new State(s,edgesStatuses[1],0.35);
//
//
//        // t,c,u
//        State s5 = new State(s,edgesStatuses[2],0.3);
//
//        // s,c,u
//        State s6= new State(t,edgesStatuses[2],0.3);
//
//        // s,c,c
//        State s7 = new State(s,edgesStatuses[3],0.15);
//
//        // t,c,c
//        State s8= new State(t,edgesStatuses[3],0.15);
//
//
//        // s,u,o
//        State s9 = new State(s,edgesStatuses[4],0.5);
//
//        // t,u,o
//        State s10= new State(t,edgesStatuses[4],0.5);
//
//        // s,u,u
//        State s11 = new State(s,edgesStatuses[5],1.0);
//
//        // t,u,u
//        State s12 = new State(t,edgesStatuses[5],1.0);
//
//
//        // s,u,c
//        State s13 = new State(s,edgesStatuses[6],0.5);
//
//        // t,u,c
//        State s14= new State(t,edgesStatuses[6],0.5);
//
//        // s,o,c
//        State s15 = new State(s,edgesStatuses[7],0.15);
//
//        // t,o,c
//        State s16= new State(t,edgesStatuses[7],0.15);
//
//
//        HashMap<String, State> statesMap = new HashMap<String, State>();
//        statesMap.put(s1.getStateId(),s1);
//        statesMap.put(s2.getStateId(),s2);
//        statesMap.put(s3.getStateId(),s3);
//        statesMap.put(s4.getStateId(),s4);
//        statesMap.put(s5.getStateId(),s5);
//        statesMap.put(s6.getStateId(),s6);
//        statesMap.put(s7.getStateId(),s7);
//        statesMap.put(s8.getStateId(),s8);
//        statesMap.put(s9.getStateId(),s9);
//        statesMap.put(s10.getStateId(),s10);
//        statesMap.put(s11.getStateId(),s11);
//        statesMap.put(s12.getStateId(),s12);
//        statesMap.put(s13.getStateId(),s13);
//        statesMap.put(s14.getStateId(),s14);
//        statesMap.put(s15.getStateId(),s15);
//        statesMap.put(s16.getStateId(),s16);
//
//        MDP newMD = new MDP(actions,statesMap);
//
//        //todo: static method!!
//        UtilityCalculator uc = new UtilityCalculator();
//        uc.findOptimalPolicy(newMD);
 //   }
}
