package org.example;

public class GoTo {
    private int initialState;
    private int entryIndex;
    private int nextState;

    public GoTo(int initialState, int entryIndex, int nextState) {
        this.initialState = initialState;
        this.entryIndex = entryIndex;
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

    public int getNextState() {
        return nextState;
    }

    public void setNextState(int nextState) {
        this.nextState = nextState;
    }
}
