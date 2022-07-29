package com.moneyAppV5.product;

import com.moneyAppV5.product.dto.ShopDTO;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "shops")
public class Shop
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int hash;



    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ShopDTO toDto()
    {
        return new ShopDTO(this.name, this.hash);
    }
}
