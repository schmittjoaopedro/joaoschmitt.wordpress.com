package com.github.schmittjoaopedro;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        Application application = new Application();
        application.setRateSelic(6.4);
        application.setRateDI(6.4);
        application.setNumMonthMin(23);
        application.setNumMonthMax(24);
        application.setValueApplied(10000);
        application.setFgc(true);

        // Calculation variables
        String rootFolder = "C:\\projects\\joaoschmitt.wordpress.com\\investment-simulator\\src\\main\\resources\\requests";
        String cdbFile = "lc-cdb.json";
        String lciFile = "lci-lca.json";
        String tdFile = "tesouro-direto.json";
        List<Security> securities = new ArrayList<>();

        // Read CDB, LCI, Tesouro direto, etc... files
        securities.addAll(SecurityReader.getInvestments(Paths.get(rootFolder, cdbFile).toString()));
        securities.addAll(SecurityReader.getInvestments(Paths.get(rootFolder, lciFile).toString()));
        securities.addAll(SecurityReader.getInvestments(Paths.get(rootFolder, tdFile).toString()));

        securities = PosFixed.getPosFixedSecurityCalculated(securities, application);

        Security securityBest = securities.get(0);
        for (Security security : securities) {
            printSecurity(security);
            if (security.getIncomingAmount() > securityBest.getIncomingAmount()) {
                securityBest = security;
            }
        }
        System.out.println("BEST SO FAR");
        printSecurity(securityBest);
    }

    public static void printSecurity(Security security) {
        String msg = security.getIndex();
        msg += " , " + " Type = " + security.getSecurityType();
        msg += " , " + " Taxes = " + security.getIncomeTaxDescription();
        msg += " , " + " Name = " + security.getNickname();
        msg += " , " + " Rentability = " + security.getRentability();
        msg += " , " + " Tempo = " + security.getMaturityDays();
        msg += " , " + " Ganho = R$" + String.format("%.2f", security.getIncomingAmount());
        System.out.println(msg);
    }

}
