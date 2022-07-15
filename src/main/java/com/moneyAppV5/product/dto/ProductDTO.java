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
//    TODO price? czy float czy obiekt?

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
}