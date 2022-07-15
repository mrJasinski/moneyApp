package com.moneyAppV5.product.dto;

import com.moneyAppV5.product.MainUnit;
import com.moneyAppV5.product.Unit;

public class UnitDTO
{
    private String name;
    private String symbol;
    private int multiplier;
    private String baseUnitSymbol;


    public void createName(String prefix, String unit)
    {
        this.name = String.format("%s%s", prefix, unit);
    }

    public void createSymbol(String prefixSymbol, String unitSymbol)
    {
        this.symbol = String.format("%s%s", prefixSymbol, unitSymbol);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public String getBaseUnitSymbol() {
        return baseUnitSymbol;
    }

    public void setBaseUnitSymbol(String baseUnitSymbol) {
        this.baseUnitSymbol = baseUnitSymbol;
    }

    public Unit toUnit()
    {
//        TODO
        return null;
    }
}
