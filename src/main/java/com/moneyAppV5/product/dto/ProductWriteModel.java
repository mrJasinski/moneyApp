package com.moneyAppV5.product.dto;

import com.moneyAppV5.product.*;

import java.util.HashSet;
import java.util.List;

public class ProductWriteModel
{
    private Brand brand;
    private Genre genre;
    private String name;
    private String barCode;
    private float quantity;
    private Unit unit;
    private String description;
    private List<Price> prices;
    private int hash;

    public ProductWriteModel()
    {
        prices.add(new Price());
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
        result.setHash(this.hash);

        return result;
    }
}