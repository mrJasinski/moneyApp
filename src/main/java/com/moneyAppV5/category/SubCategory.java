package com.moneyAppV5.category;

import com.moneyAppV5.category.dto.SubCategoryDTO;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "sub_categories")
public class SubCategory
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String subCategory;
    @OneToMany(mappedBy = "subCategory")
    private Set<Category> categories;
    @ManyToOne
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;
    private Integer hash;

    @Override
    public int hashCode()
    {
        return Objects.hash(this.subCategory);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory(String mainCategory)
    {
        this.subCategory = mainCategory;
    }

    public MainCategory getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    public SubCategoryDTO toDto()
    {
        var sub = new SubCategoryDTO();

        sub.setSubCategory(this.subCategory);
        sub.setMainCategory(this.mainCategory.toDto());
        sub.setHash(this.hash);

        return sub;
    }
}
