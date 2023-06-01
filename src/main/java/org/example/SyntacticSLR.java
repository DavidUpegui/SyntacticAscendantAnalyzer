package org.example;

import java.util.ArrayList;

public class SyntacticSLR {
    String[] terminalVariables = {"", "id", "=", ";", "+", "-", "*", "/", "num", "(", ")", "$"};
    String[] nonTerminalVariables = {"", "B", "A", "E", "T", "F"};
    Production[] productions = {
            new Production(1, 1, new int[]{2}),                 // B -> A
            new Production(2, 5, new int[]{2, -1, -2, 3, -3}),  // A -> A id = E;
            new Production(2, 4, new int[]{-1, -2, 3, -3}),     // A -> id = E;
            new Production(3, 3, new int[]{3, -4, 4}),          // E -> E + T
            new Production(3, 3, new int[]{3, -5, 4}),          // E -> E - T
            new Production(3, 1, new int[]{4}),                 // E -> T
            new Production(4, 3, new int[]{4, -6, 5}),          // T -> T * F
            new Production(4, 3, new int[]{4, -7, 5}),          // T -> T / F
            new Production(4, 1, new int[]{5}),                 // T -> F
            new Production(5, 1, new int[]{-1}),                // F -> id
            new Production(5, 1, new int[]{-8}),                // F -> num
            new Production(5, 3, new int[]{-9, 3, -10})         // F -> ( E )
    };



    int[][] followings = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // SIG(B)={ $  }
            {2, 1, 11, 0, 0, 0, 0, 0, 0, 0, 0}, // SIG(A)={ id $  }
            {4, 3, 4, 5, 10, 0, 0, 0, 0, 0, 0}, // SIG(E)={ ; + - )  }
            {6, 6, 7, 3, 4, 5, 10, 0, 0, 0, 0}, // SIG(T)={ * / ; + - )  }
            {6, 6, 7, 3, 4, 5, 10, 0, 0, 0, 0} // SIG(F)={ * / ; + - )  }
    };


    Stack stack = new Stack();
    ArrayList<Action> action = new ArrayList<Action>();
    ArrayList<GoTo> gotos = new ArrayList<GoTo>();
    ArrayList<Integer> dd = new ArrayList<Integer>();
    ArrayList<Item> canonicColection = new ArrayList<Item>();


    public SyntacticSLR() {
    }

    public void start()
    {
        stack.clear();
        dd.clear();
        action.clear();
        gotos.clear();
        canonicColection.clear();


        //Create first item (I0) and calculates the lock
        int[][] arre = {{-1, 0}};
        canonicColection.add(lock(new Item(arre, 1)));

        //Creates I1 and calculates the lock
        int[][] arreItem1 = {{-1, 1}};
        canonicColection.add(new Item(arreItem1, 1));

        //Calculates the canonic conection of the grammar
        for (int i = 0; i < canonicColection.size(); i++) {
            if (i != 1) {
                addConjItems(i);
            }
        }

        //Create the gotos if the item S' -> S. The augmented grammar.
        gotos.add(new GoTo(0, 1, 1));

        //Generate the tabla M (changes and reductions)
        for (int i = 0; i < canonicColection.size(); i++) {
            generateChanges(i);
            generateReductions(i);
        }

    }

    public Item lock(Item oItem)
    {
        boolean changes = true;
        while (changes) {
            for (int i = 0; i < oItem.numItems(); i++) {
                int numItemsAdded = addItems(i, oItem);
                if (numItemsAdded > 0) {
                    changes = true;
                    break;
                } else {
                    changes = false;
                }
            }
        }
        return oItem;
    }

    public void addConjItems(int i)
    {
        boolean[] itemsMark = new boolean[productions.length + 1];
        for (int j = 0; j < productions.length + 1; j++) {
            itemsMark[j] = false;
        }
        itemsMark[0] = i == 0;
        for (int j = 0; j < canonicColection.get(i).numItems(); j++) {
            if (!itemsMark[j]) {
                int numProd = canonicColection.get(i).numProd(j);
                int posPto = canonicColection.get(i).posPto(j);
                if (posPto != productions[numProd].getnTokens()) {
                    Item newItem = new Item();
                    int indSimGoTo = productions[numProd].getTokens()[posPto];
                    for (int k = 0; k < canonicColection.get(i).numItems(); k++) {
                        if (!itemsMark[k]) {
                            int nP = canonicColection.get(i).numProd(k);
                            int pP = canonicColection.get(i).posPto(k);
                            try {
                                if (indSimGoTo == productions[nP].getTokens()[pP]) {
                                    newItem.add(nP, pP + 1);
                                    itemsMark[k] = true;
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                    int[] edoExist = {-1};
                    newItem = lock(newItem);
                    if (!isNewItem(newItem, edoExist)) // Verify if the items already exist
                    {
                        gotos.add(new GoTo(i , indSimGoTo, canonicColection.size()));
                        canonicColection.add(newItem);
                    } else {
                        gotos.add(new GoTo(i , indSimGoTo, edoExist[0]));
                    }
                }
            }
        }
    }

    public int addItems(int i, Item oItem)
    {
        int numItemsAdded = 0;
        int posPto = oItem.posPto(i);
        int noProd = oItem.numProd(i);
        int indVns = noProd == -1 ? 1 : (posPto == productions[noProd].getnTokens() ? 0 : (productions[noProd].getTokens()[posPto] < 0 ? 0 : productions[noProd].getTokens()[posPto]));
        if (indVns > 0) {
            for (int j = 0; j < productions.length; j++) {
                if (indVns == productions[j].getProducerIndex() && !oItem.itemExist(j, 0))
                {
                    oItem.add(j, 0);
                    numItemsAdded++;
                }
            }
        }
        return numItemsAdded;
    }

    public boolean isNewItem(Item oNewItem, int[] edoExist)
    {
        edoExist[0] = -1;
        for (int i = 0; i < canonicColection.size(); i++) {
            if (canonicColection.get(i).numItems() == oNewItem.numItems()) {
                int succes = 0;
                for (int j = 0; j < canonicColection.get(i).numItems(); j++) {
                    for (int k = 0; k < oNewItem.numItems(); k++) {
                        if (canonicColection.get(i).numProd(j) == oNewItem.numProd(k) && canonicColection.get(i).posPto(j) == oNewItem.posPto(k)) {
                            succes++;
                            break;
                        }
                    }
                }
                if (succes == canonicColection.get(i).numItems())
                {
                    edoExist[0] = i;
                    return true;
                }

            }
        }
        return false;
    }

    public void generateReductions(int i) //
    {
        for (int j = 0; j < canonicColection.get(i).numItems(); j++) {
            int numProd = canonicColection.get(i).numProd(j);
            int posPto = canonicColection.get(i).posPto(j);
            if (i == 1) //cuando el item es 1 se realiza lo siguiente
            {
                action.add(new Action(i, terminalVariables.length - 1, 2, -1));
            } else if (numProd != -1 && posPto == productions[numProd].getnTokens()) {
                int indVns = productions[numProd].getProducerIndex();
                for (int k = 1; k <= followings[indVns][0]; k++) {
                    action.add(new Action(i, followings[indVns][k] , 1 , numProd));
                }
            }
        }
    }

    public void generateChanges(int i)
    {
        for (int j = 0; j < canonicColection.get(i).numItems(); j++) {
            int numProd = canonicColection.get(i).numProd(j);
            int posPto = canonicColection.get(i).posPto(j);
            if (numProd != -1) {
                if (posPto != productions[numProd].getnTokens()) {
                    int indSim = productions[numProd].getTokens()[posPto];
                    if (indSim < 0) {
                        int edoTrans = -1;
                        for (int k = 0; k < gotos.size(); k++) {
                            if (gotos.get(k).getInitialState() == i && gotos.get(k).getEntryIndex() == indSim) {
                                edoTrans = gotos.get(k).getNextState();
                                break;
                            }
                        }
                        action.add(new Action(i,-indSim, 0,edoTrans ));
                    }
                }
            }
        }
    }

    public int analyze(Lexicon oAnalex) {
        int ae = 0;
        oAnalex.add("$", "$");
        stack.stack.add(new GramSymbol("0"));
        while (true) {
            String s = stack.top().getElem();
            String a = oAnalex.getTokens()[ae];
            String accion = action(s, a);
            switch (accion.charAt(0)) {
                case 's':
                    stack.stack.add(new GramSymbol(a));
                    stack.stack.add(new GramSymbol(accion.substring(1)));
                    ae++;
                    break;
                case 'r':
                    takeOutTwoBeta(accion);
                    addToGoTo(accion);
                    dd.add(Integer.parseInt(accion.substring(1)));

                    break;
                case 'a':
                    return 0;
                case 'e':
                    return 1;
            }
        }
    }

    public String action(String s, String a)
    {
        int type = -1, no = -1;
        int edo = Integer.parseInt(s);
        int inda = 0;
        boolean enc = false;
        for (int i = 1; i < terminalVariables.length; i++) {
            if (terminalVariables[i].equals(a)) {
                inda = i;
                break;
            }
        }
        for (int i = 0; i < action.size(); i++) {
            if (action.get(i).getInitialState() == edo && action.get(i).getEntryIndex() == inda) {
                type = action.get(i).getActionType();
                no = action.get(i).getNextState();
                enc = true;
            }
        }
        if (!enc) {
            return "error";
        } else {
            switch (type) {
                case 0:
                    return "s" + Integer.toString(no);
                case 1:
                    return "r" + Integer.toString(no);
                case 2:
                    return "acc";
                default:
                    return "error";
            }
        }

    }

    public void takeOutTwoBeta(String accion)
    {
        int numProd = Integer.parseInt(accion.substring(1));
        int numTimes = productions[numProd].getnTokens() * 2;
        for (int i = 1; i <= numTimes; i++) {
            stack.pop();
        }
    }

    public void addToGoTo(String action )
    {
        int sPrima = Integer.parseInt(stack.top().getElem());
        int noProd = Integer.parseInt(action.substring(1));
        stack.push(new GramSymbol(nonTerminalVariables[productions[noProd].getProducerIndex()]));
        for (int i = 0; i < gotos.size(); i++) {
            if (sPrima == gotos.get(i).getInitialState() && productions[noProd].getProducerIndex() == gotos.get(i).getEntryIndex()) {
                stack.push(new GramSymbol(Integer.toString(gotos.get(i).getNextState())));
                break;
            }
        }
    }
}
