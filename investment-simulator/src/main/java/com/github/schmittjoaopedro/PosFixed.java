package com.github.schmittjoaopedro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PosFixed {

    public static List<Security> getPosFixedSecurityCalculated(List<Security> securities, Application application) {
        List<Security> posFixedSecurities = getSecuritiesFiltered(application, securities);
        for (Security security : posFixedSecurities) {
            switch (security.getIndex()) {
                case "CDI":
                    calculateCDIIncomingAmount(security, application);
                    break;
                case "SELIC":
                    calculateSelicIncomingAmount(security, application);
                default:
                    break;
            }
        }
        return posFixedSecurities;
    }


    private static List<Security> getSecuritiesFiltered(Application application, List<Security> securities) {
        List<Security> validTitles = new ArrayList<>();
        validTitles.addAll(securities.stream()
                .filter(security -> security.getIndex().contains("CDI"))
                .filter(security -> security.getMinTick() <= application.getValueApplied())
                .filter(security -> Utils.getNumDaysAfterNumMonths(application.getNumMonthMin()) <= security.getMaturityDays())
                .filter(security -> security.getMaturityDays() <= Utils.getNumDaysAfterNumMonths(application.getNumMonthMax()))
                .filter(security -> application.isFgc() ? security.isGuaranteedByFgc() : true)
                .filter(security -> security.getMinTick() <= application.getValueApplied())
                .collect(Collectors.toList()));
        validTitles.addAll(securities.stream()
                .filter(security -> security.getIndex().contains("SELIC"))
                .filter(security -> Utils.getNumDaysAfterNumMonths(application.getNumMonthMin()) <= security.getMaturityDays())
                .collect(Collectors.toList()));
        return validTitles;
    }

    public static void calculateCDIIncomingAmount(Security security, Application application) {
        double cdiRate;
        double appliedValue = application.getValueApplied();
        double diRate = application.getRateDI();
        double numMonths = Utils.getNumMonthsAfterNumDays(security.getMaturityDays());

        if (security.getRentability().endsWith("% do CDI")) {
            cdiRate = Double.valueOf(security.getRentability().substring(0, security.getRentability().indexOf("%")).replace(",", "."));
            cdiRate /= 100.0;
        } else {
            throw new RuntimeException("Does not have CDI rate");
        }

        // Calculate the incoming value
        double workingDaysFactor = (1.0 / 252.0);
        double diRateValue = 1.0 + (diRate / 100.0);
        double diFactor = Math.pow(diRateValue, workingDaysFactor) - 1.0;
        double cdiFactor = (diFactor * cdiRate) + 1.0;
        double cdiByDays = Math.pow(cdiFactor, 252.0 / 12.0 * numMonths);
        double rawIncomingValue = cdiByDays * appliedValue - appliedValue;

        // Calculate the IR
        double ir = 1.0 - IRTable.getIRByNumMonths(numMonths);
        if (security.isIncomeTaxFree()) {
            ir = 1.0;
        }

        // Final value
        double incomingAmount = appliedValue + rawIncomingValue * ir;

        security.setIncomingAmount(incomingAmount);
    }

    public static void calculateSelicIncomingAmount(Security security, Application application) {
        double appliedValue = application.getValueApplied();
        double selicRate = application.getRateSelic() / 100.0;
        double numMonths = application.getNumMonthMax();
        double ir = IRTable.getIRByNumMonths(numMonths);
        double numYears = numMonths / 12.0;
        double b3CostByYear = 0.003;

        double selicFactor = Math.pow(1.0 + selicRate, numYears);
        double incomingValue = appliedValue * selicFactor - appliedValue;
        double irFactor = 1.0 - ir;
        double b3Discount = (appliedValue + incomingValue) * b3CostByYear * numYears;
        double incomingTotal = incomingValue * irFactor - b3Discount + appliedValue;
        security.setIncomingAmount(incomingTotal);
    }


}
