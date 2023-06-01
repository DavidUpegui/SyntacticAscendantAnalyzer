package org.example;

import java.util.ArrayList;

public class SyntacticSLR {
    String[] terminalVariables = {"", "id", "=", ";", "+", "-", "*", "/", "num", "(", ")", "$"};
    String[] nonTerminalVariables = {"", "B", "A", "E", "T", "F"};
    Production[] productions = {
            new Production(1, 1, new int[]{2}),
            new Production(2, 5, new int[]{2, -1, -2, 3, -3}),
            new Production(2, 4, new int[]{-1, -2, 3, -3}),
            new Production(3, 3, new int[]{3, -4, 4}),
            new Production(3, 3, new int[]{3, -5, 4}),
            new Production(3, 1, new int[]{4}),
            new Production(4, 3, new int[]{4, -6, 5}),
            new Production(4, 3, new int[]{4, -7, 5}),
            new Production(4, 1, new int[]{5}),
            new Production(5, 1, new int[]{-1}),
            new Production(5, 1, new int[]{-8}),
            new Production(5, 3, new int[]{-9, 3, -10})
    };



    int[][] followings = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Renglon que no se usa
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

    public void start() //---------------------------------------------
    {
        stack.clear();
        dd.clear();
        action.clear();
        gotos.clear();
        canonicColection.clear();


        //crea item 0 y calcula la cerradura del mismo---------------
        int[][] arre = {{-1, 0}};
        canonicColection.add(lock(new Item(arre, 1)));

        //crea item 1 y lo asigna ----------------------------------
        int[][] arreItem1 = {{-1, 1}};
        canonicColection.add(new Item(arreItem1, 1));

        //calcula la coleccion canonica de la gramatica-------------
        for (int i = 0; i < canonicColection.size(); i++) {
            if (i != 1) {
                addConjItems(i);
            }
        }

        //crear los goTos del item  S'->.S gramatica aumentada------------
        gotos.add(new GoTo(0, 1, 1));
//        _goTo[_noGoTos][0] = 0;
//        _goTo[_noGoTos][1] = 1;
//        _goTo[_noGoTos++][2] = 1;

        //genera cambios y reducciones de la tabla M----------------------
        for (int i = 0; i < canonicColection.size(); i++) {
            generateChanges(i);
            generateReductions(i);
        }

    }  // fin de Inicia() -------------------------------------------------------------------

    public Item lock(Item oItem) // Cerradura de un item-------------------------------------
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
    }  // Fin de Cerradura() ----------------------------------------------------------------------

    public void addConjItems(int i) //-------------------------------------------------------
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
//                    _goTo[_noGoTos][0] = i;    Este se define dentro del IF
//                    _goTo[_noGoTos][1] = indSimGoTo;
                    newItem = lock(newItem);
                    if (!isNewItem(newItem, edoExist))//verifica si el item no existe
                    {
                        gotos.add(new GoTo(i , indSimGoTo, canonicColection.size()));
//                        _goTo[_noGoTos++][2] = _noItems;
                        canonicColection.add(newItem);
                    } else {
                        gotos.add(new GoTo(i , indSimGoTo, edoExist[0]));
//                        _goTo[_noGoTos++][2] = edoExist[0];//calcular el goTo cuando el item no existe
                    }
                }
            }
        }
    }  // Fin de AgregarConjItems()--------------------------------------------------------------------

    public int addItems(int i, Item oItem) //--------------------------------------------------
    {
        int numItemsAdded = 0;
        int posPto = oItem.posPto(i);
        int noProd = oItem.numProd(i);
        int indVns = noProd == -1 ? 1 : (posPto == productions[noProd].getnTokens() ? 0 : (productions[noProd].getTokens()[posPto] < 0 ? 0 : productions[noProd].getTokens()[posPto]));
        if (indVns > 0) {
            for (int j = 0; j < productions.length; j++) {
                if (indVns == productions[j].getProducerIndex() && !oItem.itemExist(j, 0)) //busca si existe una produccion con
                {                                                    //ese indice y que no exista el item
                    oItem.add(j, 0);
                    numItemsAdded++;
                }
            }
        }
        return numItemsAdded;
    }  // Fin de AgregarItems() -------------------------------------------------------------------------

    public boolean isNewItem(Item oNewItem, int[] edoExist) //-----------------------------------
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
                if (succes == canonicColection.get(i).numItems()) //si numero de items son iguales a los succes, entonces ya existe
                {
                    edoExist[0] = i;
                    return true;
                }

            }
        }
        return false;
    }  // Fin de EstaNuevoItem()  ------------------------------------------------------------------

    public void generateReductions(int i) // reducciones del Item _c[i] ----------------------------
    {
        for (int j = 0; j < canonicColection.get(i).numItems(); j++) {
            int numProd = canonicColection.get(i).numProd(j);
            int posPto = canonicColection.get(i).posPto(j);
            if (i == 1) //cuando el item es 1 se realiza lo siguiente
            {
                action.add(new Action(i, terminalVariables.length - 1, 2, -1));
//                _action[_noActions][0] = i;
//                _action[_noActions][1] = _vts.length - 1;
//                _action[_noActions][2] = 2;
//                _action[_noActions++][3] = -1;
            } else if (numProd != -1 && posPto == productions[numProd].getnTokens()) {
                int indVns = productions[numProd].getProducerIndex();
                for (int k = 1; k <= followings[indVns][0]; k++) {
                    action.add(new Action(i, followings[indVns][k] , 1 , numProd));

//                    _action[_noActions][0] = i;
//                    _action[_noActions][1] = _sig[indVns][k];
//                    _action[_noActions][2] = 1;
//                    _action[_noActions++][3] = numProd;
                }
            }
        }
    }  // Fin de GeneraReducciones()----------------------------------------

    public void generateChanges(int i) // cambios del Item _c[i]-------------------------
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
//                        _action[_noActions][0] = i;
//                        _action[_noActions][1] = -indSim;
//                        _action[_noActions][2] = 0;
//                        _action[_noActions++][3] = edoTrans;
                    }
                }
            }
        }
    }  // Fin de GeneraCambios() --------------------------------------------------------------------

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
                    stack.stack.add(new GramSymbol(accion.substring(1)));  // caso en que la accion es un cambio
                    ae++;
                    break;
                case 'r':
                    takeOutTwoBeta(accion);//sacar dos veces Beta simbolos de la pila
                    addToGoTo(accion);  //meter Vns y goTos a la pila
                    dd.add(Integer.parseInt(accion.substring(1)));

                    //_dd[_noDds++] = Integer.parseInt(accion.substring(1));  // caso en que la accion es una
                    break;                                               // reduccion
                case 'a':
                    return 0;  // aceptacion
                case 'e':
                    return 1;  // error
            }
        }
    }  // Fin de Analiza() ----------------------------------------------------------------------------------

    public String action(String s, String a) // ------------------------------------------------------------
    {
        //metodo que determina que accion se realizara
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

    }  // Fin de Accion() ------------------------------------------------------------------

    public void takeOutTwoBeta(String accion) //--------------------------------------------
    {
        int numProd = Integer.parseInt(accion.substring(1));
        int numTimes = productions[numProd].getnTokens() * 2;
        for (int i = 1; i <= numTimes; i++) {
            stack.pop();
        }
    }  // Fin de SacarDosBeta() ------------------------------------------------------------

    public void addToGoTo(String action ) //-----------------------------------------------
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
    }  // Fin de MeterAGoTo() ---------------
}
