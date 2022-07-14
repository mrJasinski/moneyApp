package com.moneyAppV5.product;

import javax.persistence.*;

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

}
