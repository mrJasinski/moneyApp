package com.moneyAppV5.product.controller;

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
@RequestMapping("/shops")
public class ShopsController
{
    private final ProductService service;

    public ShopsController(ProductService service)
    {
        this.service = service;
    }

    @GetMapping
    String showShops(Model model)
    {
        model.addAttribute("shop", new ShopDTO());

        return "shops";
    }

    @PostMapping
    String addShop(@ModelAttribute("shop") @Valid ShopDTO current, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Błędne dane!");

            return "shops";
        }

        this.service.createShop(current);

        model.addAttribute("shop", new ShopDTO());

        return "shops";
    }

    @ModelAttribute("shopsList")
    List<ShopDTO> getAllShopsAsDto()
    {
        return this.service.readAllShopsAsDto();
    }
}
