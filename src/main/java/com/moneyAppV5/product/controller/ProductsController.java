package com.moneyAppV5.product.controller;

import com.moneyAppV5.product.Brand;
import com.moneyAppV5.product.Genre;
import com.moneyAppV5.product.Shop;
import com.moneyAppV5.product.Unit;
import com.moneyAppV5.product.dto.BrandDTO;
import com.moneyAppV5.product.dto.ProductDTO;
import com.moneyAppV5.product.dto.ProductWriteModel;
import com.moneyAppV5.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController
{
    private final ProductService service;

    public ProductsController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    String showProducts()
    {
        return "products";
    }

    @PostMapping
    String addProduct(@ModelAttribute("product") @Valid ProductWriteModel current, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Błędne dane!");

            return "products";
        }

        this.service.createProduct(current);



        return "products";
    }

//    TODO postmapping z paramtrem (dodawanie cen)

    @ModelAttribute("product")
    ProductDTO getNewProductAsDto()
    {
        return new ProductDTO();
    }

    @ModelAttribute("brands")
    List<Brand> getAllBrands()
    {
        return this.service.readAllBrands();
    }

    @ModelAttribute("genres")
    List<Genre> getAllGenres()
    {
        return this.service.readAllGenres();
    }

    @ModelAttribute("units")
    List<Unit> getAllUnits()
    {
        return this.service.readAllUnits();
    }

    @ModelAttribute("shops")
    List<Shop> getAllShops()
    {
        return this.service.readAllShops();
    }
}