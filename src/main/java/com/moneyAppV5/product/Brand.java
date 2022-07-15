package com.moneyAppV5.product;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "brands")
public class Brand
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Integer hash;
    @OneToMany(mappedBy = "brand")
    private Set<Product> products;

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
}
