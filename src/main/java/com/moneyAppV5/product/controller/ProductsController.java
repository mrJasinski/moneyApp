package com.moneyAppV5.product.controller;

import com.moneyAppV5.product.dto.ProductDTO;
import com.moneyAppV5.product.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
    String addProduct(@ModelAttribute("product") @Valid ProductDTO current, BindingResult bindingResult, Model model)
    {


        return "products";
    }


}
