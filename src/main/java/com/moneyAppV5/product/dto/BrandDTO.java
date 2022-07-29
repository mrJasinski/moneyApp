package com.moneyAppV5.product.dto;

import com.moneyAppV5.product.Brand;

public class BrandDTO
{
    private String name;
    private int hash;

    public BrandDTO()
    {

    }

    public BrandDTO(String name, int hash)
    {
        this.name = name;
        this.hash = hash;
    }

    public Brand toBrand()
    {
        var result = new Brand();

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
