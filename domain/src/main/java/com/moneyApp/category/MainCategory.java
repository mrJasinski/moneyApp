package com.moneyApp.category;

import com.moneyApp.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "main_categories")
class MainCategory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    persistence constructor
    protected MainCategory()
    {
    }

    public MainCategory(String name, User user)
    {
        this.name = name;
        this.user = user;
    }
//tylko na potrzeby testów
    public MainCategory(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }
}
