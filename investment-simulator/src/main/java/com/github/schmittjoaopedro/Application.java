package com.github.schmittjoaopedro;

public class Application {

    private double valueApplied;

    private int numMonthMin;

    private int numMonthMax;

    private double rateDI;

    private double rateSelic;

    private boolean isFgc;

    public double getValueApplied() {
        return valueApplied;
    }

    public void setValueApplied(double valueApplied) {
        this.valueApplied = valueApplied;
    }

    public double getRateDI() {
        return rateDI;
    }

    public void setRateDI(double rateDI) {
        this.rateDI = rateDI;
    }

    public int getNumMonthMin() {
        return numMonthMin;
    }

    public void setNumMonthMin(int numMonthMin) {
        this.numMonthMin = numMonthMin;
    }

    public int getNumMonthMax() {
        return numMonthMax;
    }

    public void setNumMonthMax(int numMonthMax) {
        this.numMonthMax = numMonthMax;
    }

    public boolean isFgc() {
        return isFgc;
    }

    public void setFgc(boolean fgc) {
        isFgc = fgc;
    }

    public double getRateSelic() {
        return rateSelic;
    }

    public void setRateSelic(double rateSelic) {
        this.rateSelic = rateSelic;
    }

}
