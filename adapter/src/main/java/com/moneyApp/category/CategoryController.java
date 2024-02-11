package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryDTO;
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
//        no specific endpoints for creating sub and main categories because if they not exist they will be created during this process
        var result = this.service.createCategoryByUserIdAsDto(toSave, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.created(URI.create("/" + result.getUrlName())).body(result);
    }

    @GetMapping("/{urlName}")
    ResponseEntity<?> getCategoryByUrlNameAndUserId(@PathVariable String urlName, HttpServletRequest request)
    {
        return ResponseEntity.ok(this.service.getCategoryByUrlNameAndUserIdAsDto(urlName, this.jwtService.getUserIdFromToken(request)));
    }

    @GetMapping("/{main}/subcategories")
    ResponseEntity<?> getSubcategoriesByMainCategoryAndUser(@PathVariable String main, HttpServletRequest request)
    {
        var result = this.service.getSubcategoriesByMainCategoryNameAndUserId(main, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.ok(result);
    }

    @GetMapping
    ResponseEntity<?> getCategoriesByUser(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.service.getCategoriesByUserIdAsDto(this.jwtService.getUserIdFromToken(request)));
    }

//    view/name
//    edit
//    delete
}
