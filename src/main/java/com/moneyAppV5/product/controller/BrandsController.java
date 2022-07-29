package com.moneyAppV5.product.controller;

import com.moneyAppV5.product.dto.BrandDTO;
import com.moneyAppV5.product.dto.ShopDTO;
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
@RequestMapping("/brands")
public class BrandsController
{
    private final ProductService service;

    public BrandsController(ProductService service)
    {
        this.service = service;
    }

    @GetMapping
    String showBrands(Model model)
    {
        model.addAttribute("brand", new BrandDTO());

        return "brands";
    }

    @PostMapping
    String addShop(@ModelAttribute("brand") @Valid BrandDTO current, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Błędne dane!");

            return "brands";
        }

        this.service.createBrand(current);

        model.addAttribute("brand", new BrandDTO());
        model.addAttribute("message", "Dodano!");

        return "brands";
    }

    @ModelAttribute("brandsList")
    List<BrandDTO> getAllBrandsAsDto()
    {
        return this.service.readAllBrandsAsDto();
    }
}
