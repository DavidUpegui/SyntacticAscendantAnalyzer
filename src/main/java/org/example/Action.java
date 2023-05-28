package org.example;

public class Action {
    private int initialState;
    private int entryIndex;
    private int actionType;
    private int nextState;

    public Action(int initialState, int entryIndex, int actionType, int nextState) {
        this.initialState = initialState;
        this.entryIndex = entryIndex;
        this.actionType = actionType;
        this.nextState = nextState;
    }

    public int getInitialState() {
        return initialState;
    }

    public void setInitialState(int initialState) {
        this.initialState = initialState;
    }

    public int getEntryIndex() {
        return entryIndex;
    }

    public void setEntryIndex(int entryIndex) {
        this.entryIndex = entryIndex;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getNextState() {
        return nextState;
    }

    public void setNextState(int nextState) {
        this.nextState = nextState;
    }
}
