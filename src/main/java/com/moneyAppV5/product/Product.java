package com.moneyAppV5.product;

import com.moneyAppV5.product.dto.ProductDTO;
import com.moneyAppV5.product.dto.ShopDTO;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;
    private String name;
    private String barCode;
    private float quantity;
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
    private String description;
    @OneToMany(mappedBy = "product")
    private Set<Price> prices;
    private int hash;

    public ProductDTO toDto()
    {
//        TODO
        return null;
    }

    public ProductDTO toDto(ShopDTO shop, float price)
    {
//        TODO
        var result = new ProductDTO();

        result.setBrand(this.brand.toDto());
        result.setGenre(this.genre.toDto());
        result.setName(this.name);
        result.setBarCode(this.barCode);
        result.setQuantity(this.quantity);
        result.setUnit(this.unit.toDto());
        result.setDescription(this.description);
        result.setPrice(price);
        result.setShop(shop);
        result.setHash(this.hash);

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.brand, this.genre, this.name, this.barCode, this.quantity, this.unit);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<Price> getPrices() {
        return prices;
    }

    public void setPrices(Set<Price> prices) {
        this.prices = prices;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
}
