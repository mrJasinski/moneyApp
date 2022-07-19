package com.moneyAppV5.product.dto;

import com.moneyAppV5.product.Product;

public class ProductDTO
{
    private BrandDTO brand;
    private GenreDTO genre;
    private String name;
    private String barCode;
    private float quantity;
    private UnitDTO unit;
    private String description;
    private float price;
    private int hash;
    private ShopDTO shop;

    public Product toProduct()
    {
        var result = new Product();

        result.setBrand(this.brand.toBrand());
        result.setGenre(this.genre.toGenre());
        result.setName(this.name);
        result.setBarCode(this.barCode);
        result.setQuantity(this.quantity);
        result.setUnit(this.unit.toUnit());
        result.setDescription(this.description);

        return result;
    }

    public BrandDTO getBrand() {
        return brand;
    }

    public void setBrand(BrandDTO brand) {
        this.brand = brand;
    }

    public GenreDTO getGenre() {
        return genre;
    }

    public void setGenre(GenreDTO genre) {
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

    public UnitDTO getUnit() {
        return unit;
    }

    public void setUnit(UnitDTO unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}