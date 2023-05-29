package org.example;

import java.util.ArrayList;

public class CanonicSet {
    private ArrayList<CanonicItem> itemsArray;

    public CanonicSet(ArrayList<CanonicItem> itemsArray) {
        this.itemsArray = itemsArray;
    }

    public CanonicSet() {
    }

    public int noProd(int i) {
        return this.itemsArray.get(i).getProductionIndex();
    }

    public int posPto(int i) {
        return this.itemsArray.get(i).getPointIndex();
    }

    public boolean itemExist(int noProd, int posPto) {
        for(int i = 0; i < this.itemsArray.size(); ++i) {
            if (this.itemsArray.get(i).getProductionIndex() == noProd && this.itemsArray.get(i).getPointIndex() == posPto) {
                return true;
            }
        }
        return false;
    }

    public void add(int noProd, int posPto) {
        this.itemsArray.add(new CanonicItem(noProd, posPto));
    }
}
