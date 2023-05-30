package org.example;

import java.util.ArrayList;

public class SyntacticSLR {

    String[] _vts = {"", "id", "=", ";", "+", "-", "*", "/", "num", "(", ")", "$"};
    String[] _vns = {"", "B", "A", "E", "T", "F"};
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



    int[][] _sig = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Renglon que no se usa
            {1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // SIG(B)={ $  }
            {2, 1, 11, 0, 0, 0, 0, 0, 0, 0, 0}, // SIG(A)={ id $  }
            {4, 3, 4, 5, 10, 0, 0, 0, 0, 0, 0}, // SIG(E)={ ; + - )  }
            {6, 6, 7, 3, 4, 5, 10, 0, 0, 0, 0}, // SIG(T)={ * / ; + - )  }
            {6, 6, 7, 3, 4, 5, 10, 0, 0, 0, 0} // SIG(F)={ * / ; + - )  }
    };


    ArrayList<GramSymbol> stack = new Stack().stack;
    ArrayList<Action> action;
    ArrayList<GoTo> gotos;
    ArrayList<Integer> dd;
    ArrayList<Item> canonicColection;


    public SyntacticSLR() {
    }

    public void Inicia() //---------------------------------------------
    {
        stack.clear();
        dd.clear();
        action.clear();
        gotos.clear();
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
        for (int i = 0; i < 1000; i++) {
            generateChanges(i);
            GeneraReducciones(i);
        }

    }  // fin de Inicia() -------------------------------------------------------------------

    public Item lock(Item oItem) // Cerradura de un item-------------------------------------
    {
        boolean cambios = true;
        while (cambios) {
            for (int i = 0; i < oItem.NoItems(); i++) {
                int noItemsAgregado = addItems(i, oItem);
                if (noItemsAgregado > 0) {
                    cambios = true;
                    break;
                } else {
                    cambios = false;
                }
            }
        }
        return oItem;
    }  // Fin de Cerradura() ----------------------------------------------------------------------

    public void addConjItems(int i) //-------------------------------------------------------
    {
        boolean[] marcaItems = new boolean[productions.length + 1];
        for (int j = 0; j < productions.length + 1; j++) {
            marcaItems[j] = false;
        }
        marcaItems[0] = i == 0;
        for (int j = 0; j < canonicColection.get(i).NoItems(); j++) {
            if (!marcaItems[j]) {
                int noProd = canonicColection.get(i).NoProd(j);
                int posPto = canonicColection.get(i).PosPto(j);
                if (posPto != productions[noProd].getnTokens()) {
                    Item oNuevoItem = new Item();
                    int indSimGoTo = productions[noProd].getTokens()[posPto];
                    for (int k = 0; k < canonicColection.get(i).NoItems(); k++) {
                        if (!marcaItems[k]) {
                            int nP = canonicColection.get(i).NoProd(k);
                            int pP = canonicColection.get(i).PosPto(k);
                            try {
                                if (indSimGoTo == productions[nP].getTokens()[pP]) {
                                    oNuevoItem.Agregar(nP, pP + 1);
                                    marcaItems[k] = true;
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                    int[] edoYaExiste = {-1};
//                    _goTo[_noGoTos][0] = i;    Este se define dentro del IF
//                    _goTo[_noGoTos][1] = indSimGoTo;
                    oNuevoItem = lock(oNuevoItem);
                    if (!isNewItem(oNuevoItem, edoYaExiste))//verifica si el item no existe
                    {
                        gotos.add(new GoTo(i , indSimGoTo, canonicColection.size()));
//                        _goTo[_noGoTos++][2] = _noItems;
                        canonicColection.add(oNuevoItem);
                    } else {
                        gotos.add(new GoTo(i , indSimGoTo, edoYaExiste[0]));
//                        _goTo[_noGoTos++][2] = edoYaExiste[0];//calcular el goTo cuando el item no existe
                    }
                }
            }
        }
    }  // Fin de AgregarConjItems()--------------------------------------------------------------------

    public int addItems(int i, Item oItem) //--------------------------------------------------
    {
        int noItemsAgregado = 0;
        int posPto = oItem.PosPto(i);
        int noProd = oItem.NoProd(i);
        int indVns = noProd == -1 ? 1 : (posPto == productions[noProd].getnTokens() ? 0 : (productions[noProd].getTokens()[posPto] < 0 ? 0 : productions[noProd].getTokens()[posPto]));
        if (indVns > 0) {
            for (int j = 0; j < productions.length; j++) {
                if (indVns == productions[j].getProducerIndex() && !oItem.ExisteItem(j, 0)) //busca si existe una produccion con
                {                                                    //ese indice y que no exista el item
                    oItem.Agregar(j, 0);
                    noItemsAgregado++;
                }
            }
        }
        return noItemsAgregado;
    }  // Fin de AgregarItems() -------------------------------------------------------------------------

    public boolean isNewItem(Item oNuevoItem, int[] edoYaExiste) //-----------------------------------
    {
        edoYaExiste[0] = -1;
        for (int i = 0; i < canonicColection.size(); i++) {
            if (canonicColection.get(i).NoItems() == oNuevoItem.NoItems()) {
                int aciertos = 0;
                for (int j = 0; j < canonicColection.get(i).NoItems(); j++) {
                    for (int k = 0; k < oNuevoItem.NoItems(); k++) {
                        if (canonicColection.get(i).NoProd(j) == oNuevoItem.NoProd(k) && canonicColection.get(i).PosPto(j) == oNuevoItem.PosPto(k)) {
                            aciertos++;
                            break;
                        }
                    }
                }
                if (aciertos == canonicColection.get(i).NoItems()) //si numero de items son iguales a los aciertos, entonces ya existe
                {
                    edoYaExiste[0] = i;
                    return true;
                }

            }
        }
        return false;
    }  // Fin de EstaNuevoItem()  ------------------------------------------------------------------

    public void GeneraReducciones(int i) // reducciones del Item _c[i] ----------------------------
    {
        for (int j = 0; j < canonicColection.get(i).NoItems(); j++) {
            int noProd = canonicColection.get(i).NoProd(j);
            int posPto = canonicColection.get(i).PosPto(j);
            if (i == 1) //cuando el item es 1 se realiza lo siguiente
            {
                action.add(new Action(i, _vts.length - 1, 2, -1));
//                _action[_noActions][0] = i;
//                _action[_noActions][1] = _vts.length - 1;
//                _action[_noActions][2] = 2;
//                _action[_noActions++][3] = -1;
            } else if (noProd != -1 && posPto == productions[noProd].getnTokens()) {
                int indVns = productions[noProd].getProducerIndex();
                for (int k = 1; k <= _sig[indVns][0]; k++) {
                    action.add(new Action(i, _sig[indVns][k] , 1 , noProd));

//                    _action[_noActions][0] = i;
//                    _action[_noActions][1] = _sig[indVns][k];
//                    _action[_noActions][2] = 1;
//                    _action[_noActions++][3] = noProd;
                }
            }
        }
    }  // Fin de GeneraReducciones()----------------------------------------

    public void generateChanges(int i) // cambios del Item _c[i]-------------------------
    {
        for (int j = 0; j < canonicColection.get(i).NoItems(); j++) {
            int noProd = canonicColection.get(i).NoProd(j);
            int posPto = canonicColection.get(i).PosPto(j);
            if (noProd != -1) {
                if (posPto != productions[noProd].getnTokens()) {
                    int indSim = productions[noProd].getTokens()[posPto];
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

    public int Analiza(Lexico oAnalex) {
        int ae = 0;
        oAnalex.Anade("$", "$");
        _pila.Push(new SimbGram("0"));
        while (true) {
            String s = _pila.Tope().Elem();
            String a = oAnalex.Tokens()[ae];
            String accion = Accion(s, a);
            switch (accion.charAt(0)) {
                case 's':
                    _pila.Push(new SimbGram(a));
                    _pila.Push(new SimbGram(accion.substring(1)));  // caso en que la accion es un cambio
                    ae++;
                    break;
                case 'r':
                    SacarDosBeta(accion);//sacar dos veces Beta simbolos de la pila
                    MeterAGoTo(accion);  //meter Vns y goTos a la pila
                    _dd[_noDds++] = Integer.parseInt(accion.substring(1));  // caso en que la accion es una
                    break;                                               // reduccion
                case 'a':
                    return 0;  // aceptacion
                case 'e':
                    return 1;  // error
            }
        }
    }  // Fin de Analiza() ----------------------------------------------------------------------------------

    public String Accion(String s, String a) // ------------------------------------------------------------
    {
        //metodo que determina que accion se realizara
        int tipo = -1, no = -1;
        int edo = Integer.parseInt(s);
        int inda = 0;
        boolean enc = false;
        for (int i = 1; i < _vts.length; i++) {
            if (_vts[i].equals(a)) {
                inda = i;
                break;
            }
        }
        for (int i = 0; i < _noActions; i++) {
            if (_action[i][0] == edo && _action[i][1] == inda) {
                tipo = _action[i][2];
                no = _action[i][3];
                enc = true;
            }
        }
        if (!enc) {
            return "error";
        } else {
            switch (tipo) {
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

    public void SacarDosBeta(String accion) //--------------------------------------------
    {
        int noProd = Integer.parseInt(accion.substring(1));
        int noVeces = _prod[noProd][1] * 2;
        for (int i = 1; i <= noVeces; i++) {
            _pila.Pop();
        }
    }  // Fin de SacarDosBeta() ------------------------------------------------------------

    public void MeterAGoTo(String accion) //-----------------------------------------------
    {
        int sPrima = Integer.parseInt(_pila.Tope().Elem());
        int noProd = Integer.parseInt(accion.substring(1));
        _pila.Push(new SimbGram(_vns[_prod[noProd][0]]));
        for (int i = 0; i < _noGoTos; i++) {
            if (sPrima == _goTo[i][0] && _prod[noProd][0] == _goTo[i][1]) {
                _pila.Push(new SimbGram(Integer.toString(_goTo[i][2])));
                break;
            }
        }
    }  // Fin de MeterAGoTo() ---------------
}
