package org.example;

public class Item {
    Integer productionIndex;
    Integer pointIndex;
    Integer numItems;

    public Item(Integer productionIndex, Integer pointIndex, Integer numItems) {
        this.productionIndex = productionIndex;
        this.pointIndex = pointIndex;
        this.numItems = numItems;
    }

    public Integer getProductionIndex() {
        return productionIndex;
    }

    public void setProductionIndex(Integer productionIndex) {
        this.productionIndex = productionIndex;
    }

    public Integer getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(Integer pointIndex) {
        this.pointIndex = pointIndex;
    }

    public Integer getNumItems() {
        return numItems;
    }

    public void setNumItems(Integer numItems) {
        this.numItems = numItems;
    }
}
