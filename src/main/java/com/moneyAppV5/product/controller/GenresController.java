package com.moneyAppV5.product.controller;

import com.moneyAppV5.product.dto.GenreDTO;
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
@RequestMapping("/genres")
public class GenresController
{
    private final ProductService productService;

    public GenresController(ProductService productService)
    {
        this.productService = productService;
    }

    @GetMapping()
    String showGenres(Model model)
    {
        model.addAttribute("genre", new GenreDTO());

        return "genres";
    }

    @PostMapping()
    String addGenre(@ModelAttribute("genre") @Valid GenreDTO current, BindingResult bindingResult, Model model)
    {

        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Błędne dane!");

            return "genres";
        }

        if (this.productService.genreExistsInDatabase(current.getName()))
        {
            model.addAttribute("message", "Podana kategoria już istnieje!");

            return "genres";
        }

        this.productService.createGenre(current);

        model.addAttribute("genre", new GenreDTO());
        model.addAttribute("message", "Dodano!");

        return "genres";
    }

    @ModelAttribute("genres")
    List<GenreDTO> getGenres()
    {
        return this.productService.readAllGenresAsDto();
    }

}
