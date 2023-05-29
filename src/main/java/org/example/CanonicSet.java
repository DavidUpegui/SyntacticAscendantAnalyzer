package org.example;

public class CanonicSet {
    private ArrayList<Item> itemsArray;

    public CanonicSet(ArrayList<Item> itemsArray) {
        this.itemsArray = itemsArray;
    }

    public CanonicSet() {
    }

    public int noProd(int i) {
        return this.itemsArray[i].getProductionIndex();
    }

    public int posPto(int i) {
        return this.itemsArray[i].getPointIndex;
    }

    public boolean itemExist(int noProd, int posPto) {
        for(int i = 0; i < this.itemsArray.size(); ++i) {
            if (this.itemsArray[i].getProductionIndex == noProd && this.itemsArray[i].getPointIndex == posPto) {
                return true;
            }
        }
        return false;
    }

    public void add(int noProd, int posPto) {
        this.itemsArray.add(new Item(noProd, posPto));
    }
}
