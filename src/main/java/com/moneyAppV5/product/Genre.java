package com.moneyAppV5.product;

import com.moneyAppV5.product.dto.GenreDTO;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "genres")
public class Genre
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private int hash;

    public GenreDTO toDto()
    {
        var dto = new GenreDTO();

        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setHash(this.hash);

        return dto;
    }

    @Override
    public int hashCode()
    {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
}
