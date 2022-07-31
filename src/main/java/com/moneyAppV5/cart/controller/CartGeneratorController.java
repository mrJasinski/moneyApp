package com.moneyAppV5.cart.controller;

import com.moneyAppV5.cart.dto.ShoppingListWrapperDTO;
import com.moneyAppV5.cart.dto.ShoppingPositionDTO;
import com.moneyAppV5.product.Genre;
import com.moneyAppV5.product.Unit;
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
@RequestMapping("/cartGenerator")
public class CartGeneratorController
{
    private final ProductService service;

    public CartGeneratorController(ProductService service)
    {
        this.service = service;
    }

    @GetMapping
    String getCarGenerator(Model model)
    {
        model.addAttribute("shoppingList", new ShoppingListWrapperDTO());

        return "cartGenerator";
    }

    @PostMapping()
    String generateCarts(@ModelAttribute("shoppingList") @Valid ShoppingListWrapperDTO current, Model model, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Błędne dane!");

            return "cartGenerator";
        }

//        TODO generowanie wózków zakupowych
//        TODO po wysłaniu formularza lista sie czyści czy zostaje?
        model.addAttribute("shoppingList", current);
        model.addAttribute("message", "Lista utworzona!");

        //        TODO na początek nieinteraktywna mapa<shop list<product>>
        var carts = this.service.generateCarts(current);

        model.addAttribute("shops", carts.keySet());
        model.addAttribute("carts", carts);

        return "cartGenerator";
    }

    @PostMapping(params = "addPosition")
    String addPositionToList(@ModelAttribute("shoppingList") ShoppingListWrapperDTO current, Model model)
    {
        current.getPositions().add(new ShoppingPositionDTO());

        model.addAttribute("shoppingList", current);

        return "cartGenerator";
    }

    @ModelAttribute("genresList")
    List<Genre> getGenresList()
    {
        return this.service.readAllGenres();
    }

    @ModelAttribute("unitsList")
    List<Unit> getUnitsList()
    {
        return this.service.readAllUnits();
    }


}
