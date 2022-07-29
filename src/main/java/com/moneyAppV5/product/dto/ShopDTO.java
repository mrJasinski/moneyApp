package com.moneyAppV5.product.dto;

import com.moneyAppV5.product.Shop;

public class ShopDTO {
    private String name;
    private int hash;

    public ShopDTO()
    {
    }

    public ShopDTO(String name, int hash)
    {
        this.name = name;
        this.hash = hash;
    }

    public Shop toShop()
    {
        var result = new Shop();

        result.setName(this.name);
        result.setHash(this.hash);

        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
}
