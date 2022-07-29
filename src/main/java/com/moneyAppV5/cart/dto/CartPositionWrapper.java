package com.moneyAppV5.cart.dto;

import com.moneyAppV5.product.Product;
import com.moneyAppV5.product.Unit;
import com.moneyAppV5.product.dto.ProductDTO;

public class CartPositionWrapper
{
    private Product product;
    private float amount;
    private float quantity;
    private Unit unit;
    private float price;

    public CartPositionWrapper()
    {
    }

    public CartPositionWrapper(Product product)
    {
        this.product = product;
        this.amount = 1;
    }

    public CartPositionWrapper(Product product, float amount, float price)
    {
        this.product = product;
        this.amount = amount;
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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
}
