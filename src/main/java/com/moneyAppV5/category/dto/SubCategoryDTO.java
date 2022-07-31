package com.moneyAppV5.category.dto;

import com.moneyAppV5.category.MainCategory;
import com.moneyAppV5.category.SubCategory;
import com.moneyAppV5.transaction.dto.TransactionDTO;

import java.util.List;

public class SubCategoryDTO
{
    private String subCategory;
    private MainCategoryDTO mainCategory;
    private Integer hash;
    private List<TransactionDTO> transactions;

    private ActualDataWrapperDTO wrapper;

    public SubCategoryDTO()
    {
    }

    public SubCategoryDTO(SubCategory subCategory)
    {
        this.subCategory = subCategory.getSubCategory();
        this.mainCategory = new MainCategoryDTO(subCategory.getMainCategory());
        this.hash = subCategory.getHash();
    }

    public SubCategoryDTO(String subCategory, MainCategory mainCategory) {
        this.subCategory = subCategory;
        this.mainCategory = new MainCategoryDTO(mainCategory);
    }

//    SubCategoryDTO(String subCategory, String description)
//    {
//        this.subCategory = subCategory;
////        this.description = description;
//    }



    public SubCategory toSubCategory()
    {
        var result = new SubCategory();
        result.setSubCategory(this.subCategory);
//        result.setDescription(this.description);
        result.setMainCategory(this.mainCategory.toMainCategory());
        result.setHash(result.hashCode());

        return result;
    }

    public SubCategory toSubCategory(MainCategory mainCategory)
    {
        var result = new SubCategory();
        result.setSubCategory(this.subCategory);
//        result.setDescription(this.description);
        result.setMainCategory(mainCategory);
        result.setHash(result.hashCode());

        return result;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public MainCategoryDTO getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(MainCategoryDTO mainCategory) {
        this.mainCategory = mainCategory;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    @Override
    public String toString()
    {
        return this.subCategory;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }

    public ActualDataWrapperDTO getWrapper() {
        return wrapper;
    }

    public void setWrapper(ActualDataWrapperDTO wrapper) {
        this.wrapper = wrapper;
    }
}
