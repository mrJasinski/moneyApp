package com.moneyAppV5.product.dto;

import com.moneyAppV5.product.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class ProductWriteModel
{
    private Brand brand;
    private Genre genre;
    private String name;
    private String barCode;
    private float quantity;
    private Unit unit;
    private String description;
    private List<Price> prices = new ArrayList<>();
    private int hash;

    public ProductWriteModel()
    {
        this.prices.add(new Price());
    }


    public Product toProduct()
    {
        var result = new Product();

        result.setBrand(this.brand);
        result.setGenre(this.genre);
        result.setName(this.name);
        result.setBarCode(this.barCode);
        result.setQuantity(this.quantity);
        result.setUnit(this.unit);
        result.setDescription(this.description);
        result.setPrices(new HashSet<>(this.prices));
        result.setHash(this.hashCode());

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.brand, this.genre, this.name, this.barCode, this.quantity, this.unit);
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
}