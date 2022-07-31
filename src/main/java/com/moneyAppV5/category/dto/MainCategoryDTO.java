package com.moneyAppV5.category.dto;

import com.moneyAppV5.category.MainCategory;
import com.moneyAppV5.category.SubCategory;
import com.moneyAppV5.transaction.dto.TransactionDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MainCategoryDTO
{
    private String mainCategory;
//    private List<SubCategory> subCategories;
    private List<SubCategoryDTO> subCategories;
    private List<TransactionDTO> transactions;
    private Integer hash;

    private ActualDataWrapperDTO wrapper;

    public MainCategoryDTO()
    {
    }

    public MainCategoryDTO(String mainCategory)
    {
        this.mainCategory = mainCategory;
    }

//    public MainCategoryDTO(String mainCategory, List<SubCategory> subCategories)
//    {
//        this.mainCategory = mainCategory;
//        this.subCategories = subCategories;
//    }

    public MainCategoryDTO(MainCategory mainCategory)
    {
        this.mainCategory = mainCategory.getMainCategory();
        this.hash = mainCategory.getHash();
    }

    public MainCategoryDTO(MainCategory mainCategory, List<SubCategory> subCategories)
    {
        this.mainCategory = mainCategory.getMainCategory();
        this.subCategories = subCategories.stream().map(SubCategoryDTO::new).collect(Collectors.toList());
    }

    public MainCategory toMainCategory()
    {
        var result = new MainCategory();

        result.setMainCategory(this.mainCategory);
        result.setHash(result.hashCode());

        return result;
    }

    @Override
    public String toString()
    {
        return this.mainCategory;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public List<SubCategoryDTO> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoryDTO> subCategories) {
        this.subCategories = subCategories;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
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
