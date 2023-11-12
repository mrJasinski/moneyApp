package com.moneyApp.category;

import com.moneyApp.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "sub_categories")
class SubCategory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //    persistence constructor
    protected SubCategory()
    {
    }

    public SubCategory(String name, MainCategory mainCategory, User user)
    {
        this.name = name;
        this.mainCategory = mainCategory;
        this.user = user;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public MainCategory getMainCategory()
    {
        return this.mainCategory;
    }

    public User getUser()
    {
        return this.user;
    }
}

