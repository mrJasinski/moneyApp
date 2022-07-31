package com.moneyAppV5.product.controller;

import com.moneyAppV5.product.*;
import com.moneyAppV5.product.dto.ProductDTO;
import com.moneyAppV5.product.dto.ProductWriteModel;
import com.moneyAppV5.product.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ProductsController.class);

    public ProductsController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    String showProducts(Model model)
    {
        model.addAttribute("product", new ProductWriteModel());
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

        model.addAttribute("message", "Dodano!");

        return "products";
    }

    @PostMapping(params = "addPrice")
    String addPriceToProduct(@ModelAttribute("product") ProductWriteModel current, Model model)
    {
        current.getPrices().add(new Price());

        model.addAttribute("product", current);

        return "products";
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

    @ModelAttribute("productsListAsDto")
    List<ProductDTO> getAllProductsAsDto()
    {
        return this.service.readAllProductsAsDto();
    }
}