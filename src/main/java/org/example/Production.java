package org.example;

public class Production {
    private int producerIndex;
    private int nTokens;
    private int[] tokens;

    public Production(int producerIndex, int nTokens, int[] tokens) {
        this.producerIndex = producerIndex;
        this.nTokens = nTokens;
        this.tokens = tokens;
    }

    public int getProducerIndex() {
        return producerIndex;
    }

    public void setProducerIndex(int producerIndex) {
        this.producerIndex = producerIndex;
    }

    public int getnTokens() {
        return nTokens;
    }

    public void setnTokens(int nTokens) {
        this.nTokens = nTokens;
    }

    public int[] getTokens() {
        return tokens;
    }

    public void setTokens(int[] tokens) {
        this.tokens = tokens;
    }
}
