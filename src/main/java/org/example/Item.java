package org.example;

public class Item {

    int[][] _item;
    int _noItems;

    // Metodos ----------------------------------------------------------
    public Item(int[][] arre, int len) {
        _noItems = len;
        _item = new int[12 + 1][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < 2; j++) {
                _item[i][j] = arre[i][j];
            }
        }
    }

    public Item() {
        _item = new int[12 + 1][2];
        _noItems = 0;
    }

    public int numItems() {
        return _noItems;
    }

    public int numProd(int i) {
        return _item[i][0];
    }

    public int posPto(int i) {
        return _item[i][1];
    }

    public boolean itemExist(int noProd, int posPto) {
        for (int i = 0; i < _noItems; i++) {
            if (_item[i][0] == noProd && _item[i][1] == posPto) {
                return true;
            }
        }
        return false;
    }

    public void add(int noProd, int posPto) {
        _item[_noItems][0] = noProd;
        _item[_noItems++][1] = posPto;
    }
}
