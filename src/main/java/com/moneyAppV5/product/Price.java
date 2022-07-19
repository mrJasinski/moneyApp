package com.moneyAppV5.product;

import javax.persistence.*;

@Entity
@Table(name = "prices")
public class Price
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    private float price;
}
