package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.user.UserService;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.UserSource;
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

    CategoryDTO createCategoryByUserId(CategoryDTO toSave, Long userId)
    {
        var user = new UserSource(userId);
//        var user = new UserSource(String.valueOf(userId));

        var mainCategory = getMainCategoryByNameAndUserId(toSave.getMainCategory(), userId);
        var subCategory = getSubCategoryByNameAndMainCategoryIdAndUserId(toSave.getSubCategory(),
                mainCategory.getSnapshot().getId(), userId);

        if (checkIfCategoryExists(mainCategory, subCategory, toSave.getType(), userId))
            throw new IllegalArgumentException("Category with given data already exists!");

        return toDto(this.categoryRepo.save(Category.restore(new CategorySnapshot(
                null
                , mainCategory.getSnapshot()
                , subCategory.getSnapshot()
                , toSave.getType()
                , toSave.getDescription()
                , user))));
    }

    CategoryDTO toDto(Category category)
    {
        var snap = category.getSnapshot();

        return new CategoryDTO(
                snap.getMainCategory().getName()
                , snap.getSubCategory().getName()
                , snap.getType()
                , snap.getDescription()
        );
    }

    boolean checkIfCategoryExists(MainCategory mainCategory, SubCategory subCategory, CategoryType type, long userId)
    {
        return this.categoryQueryRepo.existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(mainCategory.getSnapshot().getId(), subCategory.getSnapshot().getId(),
                type, userId);
    }

    MainCategory getMainCategoryByNameAndUserId(String name, long userId)
    {
        return this.mainCategoryQueryRepo.findByNameAndUserId(name, userId)
                .orElse(createMainCategoryByNameAndUserId(name, userId));
    }

    MainCategory getMainCategoryByIdAndUserId(long id, long userId)
    {
        return this.mainCategoryQueryRepo.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Main category for given id and user id not found!"));
    }

    MainCategory createMainCategoryByNameAndUserId(String name, long userId)
    {
//        TODO set<subcats>
        return this.mainCategoryRepo.save(MainCategory.restore(new MainCategorySnapshot(null, name, null, new UserSource(userId))));
//        return this.mainCategoryRepo.save(MainCategory.restore(new MainCategorySnapshot(null, name, null, new UserSource(String.valueOf(userId)))));
    }

    SubCategory getSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryQueryRepo.findByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId)
                .orElse(createSubCategoryByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId));
    }

    SubCategory createSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryRepo.save(SubCategory.restore(new SubCategorySnapshot(null, name,
                getMainCategoryByIdAndUserId(mainCategoryId, userId).getSnapshot(), new UserSource(userId))));
//                getMainCategoryByIdAndUserId(mainCategoryId, userId).getSnapshot(), new UserSource(String.valueOf(userId)))));
    }

    List<CategoryDTO> getCategoriesByUserIdAsDto(Long userId)
    {
        return getAllCategoriesByUserId(userId)
                .stream()
                .map(this::toDto)
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
        var names = splitCategoryNameIntoMainAndSubNames(name);

        return getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(names[0], names[1], userId);
    }

    Category getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName, String subCategoryName, Long userId)
    {
        return this.categoryQueryRepo.findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(mainCategoryName, subCategoryName, userId)
                .orElseThrow(() -> new IllegalArgumentException("Category with given data not found!"));
    }

    List<SubCategory> getSubcategoriesByMainCategoryNameAndUserId(final String main, final Long userId)
    {
        return this.subCategoryQueryRepo.findByMainCategoryNameAndUserId(main, userId);
    }

    public CategorySource getCategorySourceByNameAndUserId(final String categoryName, final long userId)
    {
        var ids = getMainSubCategoryIdsByCategoryNameAndUserId(categoryName, userId);

        return new CategorySource(this.categoryQueryRepo.findIdByMainCategoryIdAndSubCategoryIdAndUserId(ids[0], ids[1], userId)
                .orElseThrow(() -> new IllegalArgumentException("Category for given data not found!")));
    }

    long[] getMainSubCategoryIdsByCategoryNameAndUserId(String categoryName, long userId)
    {
        var names = splitCategoryNameIntoMainAndSubNames(categoryName);

        var mainId = getMainCategoryIdByName(names[0], userId);

        var snapshots = getSubcategoriesByNameAndUserId(names[1], userId).stream().map(SubCategory::getSnapshot).toList();

        for (SubCategorySnapshot sub : snapshots)
            if (sub.getMainCategory().getId() == mainId)
                return new long[] {mainId, sub.getId()};

        throw new IllegalArgumentException("Sub category for given data not found!");
    }

    private long getMainCategoryIdByName(final String name, long userId)
    {
        return this.mainCategoryQueryRepo.findIdByNameAndUserId(name, userId)
            .orElseThrow(() -> new IllegalArgumentException("Main category for given data not found!"));
    }

    List<SubCategory> getSubcategoriesByNameAndUserId(final String name, final long userId)
    {
        return this.subCategoryQueryRepo.findByNameAndUserId(name, userId);
    }

    String[] splitCategoryNameIntoMainAndSubNames(String categoryName)
    {
        if (!categoryName.contains(" : "))
            throw new IllegalArgumentException("Invalid category name!");

        return new String[] {categoryName.replaceAll(" .*", ""), categoryName.replaceAll(".* : ", "")};
    }

    public CategoryType getCategoryTypeByCategoryId(final Long categoryId)
    {
        return this.categoryQueryRepo.getTypeById(categoryId);
    }

    public String getCategoryNameById(final long categoryId)
    {
        var cat = this.categoryQueryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category with given id not found!"));

        var snap = cat.getSnapshot();

        return String.format("%s : %s", snap.getMainCategory().getName(), snap.getSubCategory().getName());
    }
}