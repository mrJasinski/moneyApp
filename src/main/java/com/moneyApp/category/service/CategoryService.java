package com.moneyApp.category.service;

import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;
import com.moneyApp.category.MainCategory;
import com.moneyApp.category.SubCategory;
import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.category.repository.CategoryRepository;
import com.moneyApp.category.repository.MainCategoryRepository;
import com.moneyApp.category.repository.SubCategoryRepository;
import com.moneyApp.user.User;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepo;
    private final MainCategoryRepository mainCategoryRepo;
    private final SubCategoryRepository subCategoryRepo;
    private final UserService userService;

    CategoryService(CategoryRepository categoryRepo, MainCategoryRepository mainCategoryRepo, SubCategoryRepository subCategoryRepo, UserService userService)
    {
        this.categoryRepo = categoryRepo;
        this.mainCategoryRepo = mainCategoryRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.userService = userService;
    }

    public Category createCategoryByUserEmail(CategoryDTO toSave, String email)
    {
        var user = this.userService.getUserByEmail(email);

        var mainCategory = getMainCategoryByNameAndUserId(toSave.getMainCategory(), user.getId());
        var subCategory = getSubCategoryByNameAndMainCategoryIdAndUserId(toSave.getSubCategory(),
                mainCategory.getId(), user.getId());

        if (!checkIfCategoryExists(mainCategory, subCategory, toSave.getType(), user))
            return this.categoryRepo.save(new Category(mainCategory, subCategory, toSave.getType(), toSave.getDescription(), user));
        else
            throw new IllegalArgumentException("Category already exists!");
    }

    boolean checkIfCategoryExists(MainCategory mainCategory, SubCategory subCategory, CategoryType type, User user)
    {
        return this.categoryRepo.existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(mainCategory.getId(), subCategory.getId(),
                type, user.getId());
    }

    MainCategory getMainCategoryByNameAndUserId(String name, long userId)
    {
        return this.mainCategoryRepo.findByNameAndUserId(name, userId)
                .orElse(createMainCategoryByNameAndUserId(name, userId));
    }

    MainCategory getMainCategoryByIdAndUserId(long id, long userId)
    {
        return this.mainCategoryRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("No main category found for given id and user id!"));
    }

    MainCategory createMainCategoryByNameAndUserId(String name, long userId)
    {
        return this.mainCategoryRepo.save(new MainCategory(name, this.userService.getUserById(userId)));
    }

    SubCategory getSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryRepo.findByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId)
                .orElse(createSubCategoryByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId));
    }

    SubCategory createSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryRepo.save(new SubCategory(name,
               getMainCategoryByIdAndUserId(mainCategoryId, userId) , this.userService.getUserById(userId)));
    }

    public List<CategoryDTO> getAllCategoriesByUserEmailAsDto(String email)
    {
        return getAllCategoriesByUserId(this.userService.getUserIdByEmail(email))
                .stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
    }

    public List<Category> getAllCategoriesByUserId(long userId)
    {
        return this.categoryRepo.findByUserId(userId);
    }

    public List<Category> getCategoriesByTypeAndUserId(CategoryType categoryType, long userId)
    {
        return this.categoryRepo.findByTypeAndUserId(categoryType, userId);
    }

    public Category getCategoryByNameAndUserId(String name, Long userId)
    {
        if (name.contains(" : "))
        {
            var mainCategoryName = name.replaceAll(" .*", "");
            var subCategoryName = name.replaceAll(".* : ", "");

            return getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(mainCategoryName, subCategoryName, userId);
        }
        else
            throw new IllegalArgumentException("Invalid category name!");

    }

    private Category getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName, String subCategoryName, Long userId)
    {
        return this.categoryRepo.findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(mainCategoryName, subCategoryName, userId)
                .orElseThrow(() -> new IllegalArgumentException("No category found for given data!"));
    }
}
