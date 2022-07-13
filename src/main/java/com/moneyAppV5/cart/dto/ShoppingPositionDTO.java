package com.moneyAppV5.cart.dto;

import com.moneyAppV5.product.Genre;
import com.moneyAppV5.product.Unit;

public class ShoppingPositionDTO
{
    private Genre genre;
    private float quantity;
    private Unit unit;

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
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
