package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.user.User;
import com.moneyApp.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepo;
    private final CategoryQueryRepository categoryQueryRepo;
    private final MainCategoryRepository mainCategoryRepo;
    private final MainCategoryQueryRepository mainCategoryQueryRepo;
    private final SubCategoryRepository subCategoryRepo;
    private final SubCategoryQueryRepository subCategoryQueryRepo;
    private final UserService userService;

    CategoryService(
            final CategoryRepository categoryRepo
            , final CategoryQueryRepository categoryQueryRepo
            , final MainCategoryRepository mainCategoryRepo
            , final MainCategoryQueryRepository mainCategoryQueryRepo
            , final SubCategoryRepository subCategoryRepo
            , final SubCategoryQueryRepository subCategoryQueryRepo
            , final UserService userService)
    {
        this.categoryRepo = categoryRepo;
        this.categoryQueryRepo = categoryQueryRepo;
        this.mainCategoryRepo = mainCategoryRepo;
        this.mainCategoryQueryRepo = mainCategoryQueryRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.subCategoryQueryRepo = subCategoryQueryRepo;
        this.userService = userService;
    }

    Category createCategoryByUserEmail(CategoryDTO toSave, String email)
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
        return this.categoryQueryRepo.existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(mainCategory.getId(), subCategory.getId(),
                type, user.getId());
    }

    MainCategory getMainCategoryByNameAndUserId(String name, long userId)
    {
        return this.mainCategoryQueryRepo.findByNameAndUserId(name, userId)
                .orElse(createMainCategoryByNameAndUserId(name, userId));
    }

    MainCategory getMainCategoryByIdAndUserId(long id, long userId)
    {
        return this.mainCategoryQueryRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("No main category found for given id and user id!"));
    }

    MainCategory createMainCategoryByNameAndUserId(String name, long userId)
    {
        return this.mainCategoryRepo.save(new MainCategory(name, this.userService.getUserById(userId)));
    }

    SubCategory getSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryQueryRepo.findByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId)
                .orElse(createSubCategoryByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId));
    }

    SubCategory createSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryRepo.save(new SubCategory(name,
                getMainCategoryByIdAndUserId(mainCategoryId, userId), this.userService.getUserById(userId)));
    }

    List<CategoryDTO> getAllCategoriesByUserIdAsDto(Long userId)
    {
        return getAllCategoriesByUserId(userId)
                .stream()
                .map(Category::toDto)
                .collect(Collectors.toList());
    }

    List<Category> getAllCategoriesByUserId(long userId)
    {
        return this.categoryQueryRepo.findByUserId(userId);
    }

    List<Category> getCategoriesByTypeAndUserId(CategoryType categoryType, long userId)
    {
        return this.categoryQueryRepo.findByTypeAndUserId(categoryType, userId);
    }

    Category getCategoryByNameAndUserId(String name, Long userId)
    {
        if (!name.contains(" : "))
            throw new IllegalArgumentException("Invalid category name!");

        var mainCategoryName = name.replaceAll(" .*", "");
        var subCategoryName = name.replaceAll(".* : ", "");

        return getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(mainCategoryName, subCategoryName, userId);
    }

    Category getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName, String subCategoryName, Long userId)
    {
        return this.categoryQueryRepo.findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(mainCategoryName, subCategoryName, userId)
                .orElseThrow(() -> new IllegalArgumentException("No category found for given data!"));
    }

    List<SubCategory> getSubcategoriesByMainCategoryNameAndUSerId(final String main, final Long userId)
    {
        return this.subCategoryQueryRepo.findByMainCategoryNameAndUserId(main, userId);
    }
}