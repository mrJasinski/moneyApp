package com.moneyApp.category.controller;

import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.category.service.CategoryService;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/categories")
public class CategoryController
{
    private final CategoryService service;
    private final JwtService jwtService;

    public CategoryController(CategoryService service, JwtService jwtService)
    {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping("/addCategory")
    ResponseEntity<?> createCategory(@RequestBody CategoryDTO toSave, HttpServletRequest request)
    {
        var result = this.service.createCategoryByUserEmail(toSave, this.jwtService.getUserEmail(request));

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getCategoriesByUserEmail(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.service.getAllCategoriesByUserEmailAsDto(this.jwtService.getUserEmail(request)));
    }
}
