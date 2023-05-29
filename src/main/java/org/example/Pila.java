package org.example;

import java.util.ArrayList;

public class Pila {
    ArrayList<GramSymbol> stack;

    public Pila(ArrayList<GramSymbol> stack) {
        this.stack = stack;
    }

    public boolean Empty() {return this.stack.isEmpty();}

    public void push(GramSymbol element) {this.stack.add(element);}

    public GramSymbol pop() {

            if (!this.Empty()) {
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
