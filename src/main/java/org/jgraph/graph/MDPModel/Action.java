package org.jgraph.graph.MDPModel;

import logic.CollectionUtils;

public class Action implements Comparable<Action> {

    public Double getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return "utility:" + getActionUtility() + ", src:" + src + ",dst:" + dst + ",reward:" + reward;
    }

    public MDPEdge getActionEdge() {
        return actionEdge;
    }

    // utility per iteration to summerise on action during utility calculation

    public Double actionUtility = 0.0;

    public Double getActionUtility() {
        return CollectionUtils.roundTwoDigits(actionUtility);
    }

    public void setActionUtility(Double actionUtility) {
        this.actionUtility = actionUtility;
    }

    private MDPEdge actionEdge;  /// The 1:1 mdp related edge ///

    private Double reward;

    public MDPVertex getSrc() {
        return src;
    }

    private MDPVertex src;
    private MDPVertex dst;

    private String actionId;

    public static String generateActionId(MDPVertex src, MDPVertex dst) {
        return src.getId() + "_" + dst.getId();
    }

    public MDPVertex getDst() {
        return dst;
    }

//    public MDPModel.Action(MDPModel.MDPVertex src, MDPModel.MDPVertex dst, Double reward){
//            this.src = src;
//            this.dst = dst;
//            this.reward = reward;
////            actionEdge = new MDPModel.MDPEdge(src,dst,reward,)
//            actionId = generateActionId(src,dst);
//    }


    public String getActionId() {
        return actionId;
    }

    public Action(MDPEdge edge) {
        this.src = edge.src;
        this.dst = edge.dst;
        this.reward = edge.reward;
        this.actionEdge = edge;
        actionId = generateActionId(edge.src, edge.dst);
    }

    public MDPVertex move(MDPVertex src) {
        if (src == this.src) {
            return this.dst;
        }
        return null;
    }

    /**
     * Sort by utility DESC.
     *
     * @param action
     * @return
     */
    @Override
    public int compareTo(Action action) {
        if (getActionUtility() == null || action.getActionUtility() == null) {
            return 0;
        }
        return action.getActionUtility().compareTo(getActionUtility());
    }
}
