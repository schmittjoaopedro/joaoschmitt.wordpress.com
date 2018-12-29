package com.github.schmittjoaopedro;

import org.junit.Assert;
import org.junit.Test;

public class AppTest {

    @Test
    public void testPosFixedCDBTest() {
        Security security = new Security();
        Application application = new Application();

        security.setIndex("CDI");
        security.setSecurityType("CDB");
        security.setRentability("100% do CDI");
        security.setMaturityDays(731);
        security.setIncomeTaxFree(false);
        security.setIncomeTaxDescription("15% de IR");
        security.setGuaranteedByFgc(true);

        application.setRateDI(6.89);
        application.setNumMonthMin(12);
        application.setNumMonthMax(25);
        application.setValueApplied(1000);
        application.setFgc(true);

        PosFixed.calculateCDIIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 1121.17, 0.01);

        application.setValueApplied(10000);
        PosFixed.calculateCDIIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 11211.65, 0.01);

        security.setIncomeTaxDescription("17.5% de IR");
        security.setMaturityDays(366);
        PosFixed.calculateCDIIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 10568.43, 0.01);

    }

    @Test
    public void testPosFixedLCITest() {
        Security security = new Security();
        Application application = new Application();

        security.setIndex("CDI");
        security.setSecurityType("LCI");
        security.setRentability("85% do CDI");
        security.setMaturityDays(731);
        security.setIncomeTaxFree(true);
        security.setIncomeTaxDescription("Isento de IR");
        security.setGuaranteedByFgc(true);

        application.setRateDI(6.89);
        application.setNumMonthMin(12);
        application.setNumMonthMax(25);
        application.setValueApplied(1000);
        application.setFgc(true);

        PosFixed.calculateCDIIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 1119.94, 0.01);

        application.setValueApplied(10000);
        PosFixed.calculateCDIIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 11199.38, 0.01);

        security.setMaturityDays(366);
        PosFixed.calculateCDIIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 10582.71, 0.01);

    }

    @Test
    public void testPosFixedSelicTest() {
        Security security = new Security();
        Application application = new Application();

        security.setIndex("SELIC");

        application.setRateSelic(6.9);
        application.setNumMonthMin(12);
        application.setNumMonthMax(24);
        application.setValueApplied(1000);
        application.setFgc(true);

        PosFixed.calculateSelicIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 1114.49, 0.01);

        application.setValueApplied(10000);
        PosFixed.calculateSelicIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 11144.9, 0.01);

        application.setNumMonthMax(12);
        PosFixed.calculateSelicIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 10537.18, 0.01);
    }

    @Test
    public void testCdb114CDI() {
        Security security = new Security();
        Application application = new Application();

        security.setIndex("CDI");
        security.setSecurityType("CDB");
        security.setRentability("114% do CDI");
        security.setMaturityDays(723);
        security.setIncomeTaxFree(false);
        security.setIncomeTaxDescription("15% de IR");
        security.setGuaranteedByFgc(true);

        application.setRateDI(6.4);
        application.setNumMonthMin(12);
        application.setNumMonthMax(25);
        application.setValueApplied(10000);
        application.setFgc(true);

        PosFixed.calculateCDIIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 11197.58, 0.01);
    }

    @Test
    public void testCdb123CDI() {
        Security security = new Security();
        Application application = new Application();

        security.setIndex("CDI");
        security.setSecurityType("CDB");
        security.setRentability("123% do CDI");
        security.setMaturityDays(1800);
        security.setIncomeTaxFree(false);
        security.setIncomeTaxDescription("15% de IR");
        security.setGuaranteedByFgc(true);

        application.setRateDI(6.4);
        application.setNumMonthMin(12);
        application.setNumMonthMax(60);
        application.setValueApplied(10000);
        application.setFgc(true);

        PosFixed.calculateCDIIncomingAmount(security, application);
        Assert.assertEquals(security.getIncomingAmount(), 13869.26, 0.01);
    }
}
