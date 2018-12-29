package com.github.schmittjoaopedro;

import java.util.Date;

public class Security {

    private String index;

    private boolean isGuaranteedByFgc;

    private String issuerName;

    private int liquidity;

    private String liquidityDescription;

    private int maturityDays;

    private Date maturity;

    private String nickname;

    private String securityType;

    private String rentability;

    private double minTick;

    private double incomingAmount;

    private String incomeTaxDescription;

    private boolean incomeTaxFree;

    public Security() {
        super();
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public boolean isGuaranteedByFgc() {
        return isGuaranteedByFgc;
    }

    public void setGuaranteedByFgc(boolean guaranteedByFgc) {
        isGuaranteedByFgc = guaranteedByFgc;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public int getLiquidity() {
        return liquidity;
    }

    public void setLiquidity(int liquidity) {
        this.liquidity = liquidity;
    }

    public String getLiquidityDescription() {
        return liquidityDescription;
    }

    public void setLiquidityDescription(String liquidityDescription) {
        this.liquidityDescription = liquidityDescription;
    }

    public int getMaturityDays() {
        return maturityDays;
    }

    public void setMaturityDays(int maturityDays) {
        this.maturityDays = maturityDays;
    }

    public Date getMaturity() {
        return maturity;
    }

    public void setMaturity(Date maturity) {
        this.maturity = maturity;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getRentability() {
        return rentability;
    }

    public void setRentability(String rentability) {
        this.rentability = rentability;
    }

    public double getMinTick() {
        return minTick;
    }

    public void setMinTick(double minTick) {
        this.minTick = minTick;
    }

    public double getIncomingAmount() {
        return incomingAmount;
    }

    public void setIncomingAmount(double incomingAmount) {
        this.incomingAmount = incomingAmount;
    }

    public String getIncomeTaxDescription() {
        return incomeTaxDescription;
    }

    public void setIncomeTaxDescription(String incomeTaxDescription) {
        this.incomeTaxDescription = incomeTaxDescription;
    }

    public boolean isIncomeTaxFree() {
        return incomeTaxFree;
    }

    public void setIncomeTaxFree(boolean incomeTaxFree) {
        this.incomeTaxFree = incomeTaxFree;
    }
}
