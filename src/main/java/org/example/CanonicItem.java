package org.example;

public class CanonicItem {
    private int productionIndex;
    private int  pointIndex;

    public CanonicItem(int productionIndex, int pointIndex) {
        this.productionIndex = productionIndex;
        this.pointIndex = pointIndex;
    }

    public int getProductionIndex() {
        return productionIndex;
    }

    public void setProductionIndex(int productionIndex) {
        this.productionIndex = productionIndex;
    }

    public int getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(int pointIndex) {
        this.pointIndex = pointIndex;
    }
}
