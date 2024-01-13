package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.category.dto.CategoryWithIdAndNameAndTypeDTO;
import com.moneyApp.category.dto.CategoryWithIdAndNameDTO;
import com.moneyApp.user.UserService;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.UserSource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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

    CategoryService(
            final CategoryRepository categoryRepo
            , final CategoryQueryRepository categoryQueryRepo
            , final MainCategoryRepository mainCategoryRepo
            , final MainCategoryQueryRepository mainCategoryQueryRepo
            , final SubCategoryRepository subCategoryRepo
            , final SubCategoryQueryRepository subCategoryQueryRepo
    )
    {
        this.categoryRepo = categoryRepo;
        this.categoryQueryRepo = categoryQueryRepo;
        this.mainCategoryRepo = mainCategoryRepo;
        this.mainCategoryQueryRepo = mainCategoryQueryRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.subCategoryQueryRepo = subCategoryQueryRepo;
    }

    CategoryDTO createCategoryByUserId(CategoryDTO toSave, Long userId)
    {
        var user = new UserSource(userId);

        var mainCategory = getMainCategoryByNameAndUserId(toSave.getMainCategory(), userId);
        var subCategory = getSubCategoryByNameAndMainCategoryIdAndUserId(toSave.getSubCategory(),
                mainCategory.getId(), userId);

        if (checkIfCategoryExists(mainCategory.getId(), subCategory.getId(), toSave.getType(), userId))
            throw new IllegalArgumentException("Category with given data already exists!");

        return toDto(this.categoryRepo.save(new CategorySnapshot(
                0L
                , mainCategory
                , subCategory
                , toSave.getType()
                , toSave.getDescription()
                , user)));
    }

    CategoryDTO toDto(CategorySnapshot snap)
    {
        return new CategoryDTO(
                snap.getId()
                , snap.getMainCategoryName()
                , snap.getSubCategoryName()
                , snap.getType()
                , snap.getDescription()
        );
    }

    boolean checkIfCategoryExists(long mainCategoryId, long subCategoryId, CategoryType type, long userId)
    {
        return this.categoryQueryRepo.existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(mainCategoryId, subCategoryId, type, userId);
    }

    MainCategorySnapshot getMainCategoryByNameAndUserId(String name, long userId)
    {
        return this.mainCategoryQueryRepo.findByNameAndUserId(name, userId)
                .orElseGet(() -> createMainCategoryByNameAndUserId(name, userId));
    }

    MainCategorySnapshot getMainCategoryById(long id)
    {
        return this.mainCategoryQueryRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Main category for given id not found!"));
    }

    MainCategorySnapshot createMainCategoryByNameAndUserId(String name, long userId)
    {
        return this.mainCategoryRepo.save(new MainCategorySnapshot(0L, name, new HashSet<>(), new UserSource(userId)));
    }

    SubCategorySnapshot getSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryQueryRepo.findByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId)
                .orElseGet(() -> createSubCategoryByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId));
    }

    SubCategorySnapshot createSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryRepo.save(new SubCategorySnapshot(0L, name,
                getMainCategoryById(mainCategoryId), new UserSource(userId)));
    }

    public List<CategoryDTO> getCategoriesByUserIdAsDto(Long userId)
    {
        return getAllCategoriesByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    List<CategorySnapshot> getAllCategoriesByUserId(long userId)
    {
        return this.categoryQueryRepo.findByUserId(userId);
    }

    List<SubCategorySnapshot> getSubcategoriesByMainCategoryNameAndUserId(final String main, final Long userId)
    {
        return this.subCategoryQueryRepo.findByMainCategoryNameAndUserId(main, userId);
    }

    public String getCategoryNameById(final long categoryId)
    {
        var cat = this.categoryQueryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with given id not found!"));

        return String.format("%s : %s", cat.getMainCategory().getName(), cat.getSubCategory().getName());
    }

    public List<CategoryWithIdAndNameDTO> getCategoriesByNamesAndUserIdAsDto(final List<String> catNames, final Long userId)
    {
        return this.categoryQueryRepo.findCategoriesIdsAndNamesByNamesAndUserId(catNames, userId);
    }

    public List<CategoryDTO> getCategoriesByIdsAsDto(final List<Long> catIds)
    {
        return this.categoryQueryRepo.findCategoriesByIds(catIds)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public CategoryDTO getCategoryByIdAsDto(final Long categoryId)
    {
        return toDto(this.categoryQueryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with given id not found!")));
    }

    CategoryDTO getCategoryByUrlNameAndUserIdAsDto(final String urlName, final Long userId)
    {
       return toDto(this.categoryQueryRepo.findByNameAndUserId(getNameFromUrlName(urlName), userId)
               .orElseThrow(() -> new IllegalArgumentException("Category with given name not found!")));
    }

    String getNameFromUrlName(String urlName)
    {
        var result = urlName.replaceAll(":", " : ");

        result = result.replace("-", " ");

        return result;
    }

    public CategoryType getCategoryTypeById(final Long categoryId)
    {
        return this.categoryQueryRepo.findTypeById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category type for given id not found!"));
    }
}