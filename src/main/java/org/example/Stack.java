package org.example;

import java.util.ArrayList;

public class Stack {
    ArrayList<GramSymbol> stack;

    public Stack(){
        this.stack = new ArrayList<GramSymbol>();
    }

    public boolean empty() {return this.stack.isEmpty();}

    public void push(GramSymbol element) {this.stack.add(element);}
    public void clear(){
        this.stack.clear();
    }
    public GramSymbol pop() {

            if (!this.empty()) {
                int lastIndex = stack.size() - 1;
                GramSymbol poppedValue = stack.get(lastIndex);
                stack.remove(lastIndex);
                return poppedValue;
            } else {
                throw new IllegalStateException("Stack is empty. Cannot perform pop operation.");

        }
    }

    public GramSymbol top() {return this.stack.get(this.stack.size()-1);}

    public ArrayList<GramSymbol> getStack() {
        return stack;
    }

    public void setStack(ArrayList<GramSymbol> stack) {
        this.stack = stack;
    }
}
