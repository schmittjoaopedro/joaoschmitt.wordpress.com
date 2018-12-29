package com.github.schmittjoaopedro;

public class IRTable {

    public static double getIRByInterval(Security security) {
        String taxDesc = security.getIncomeTaxDescription();
        if (security.isIncomeTaxFree()) {
            return 0.0;
        } else if (taxDesc.endsWith("% de IR")) {
            double irRate = Double.valueOf(taxDesc.substring(0, taxDesc.indexOf("%")).replace(",", "."));
            return irRate / 100.0;
        } else {
            throw new RuntimeException("No IR defined");
        }
    }

    public static double getIRByNumMonths(double numMonths) {
        if (numMonths < 6) {
            return 0.225;
        } else if (numMonths < 12) {
            return 0.20;
        } else if (numMonths < 24) {
            return 0.175;
        } else {
            return 0.15;
        }
    }

}
