package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.category.dto.CategoryWithIdAndNameAndTypeDTO;
import com.moneyApp.category.dto.CategoryWithIdAndNameDTO;
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

        var mainCategory = getMainCategoryByNameAndUserId(toSave.getMainCategory(), userId);
        var subCategory = getSubCategoryByNameAndMainCategoryIdAndUserId(toSave.getSubCategory(),
                mainCategory.getId(), userId);

        if (checkIfCategoryExists(mainCategory, subCategory, toSave.getType(), userId))
            throw new IllegalArgumentException("Category with given data already exists!");

        return toDto(this.categoryRepo.save(new CategorySnapshot(
                null
                , mainCategory
                , subCategory
                , toSave.getType()
                , toSave.getDescription()
                , user)));
    }

    CategoryDTO toDto(CategorySnapshot snap)
    {
        return new CategoryDTO(
                snap.getMainCategory().getName()
                , snap.getSubCategory().getName()
                , snap.getType()
                , snap.getDescription()
        );
    }

    boolean checkIfCategoryExists(MainCategorySnapshot mainCategory, SubCategorySnapshot subCategory, CategoryType type, long userId)
    {
        return this.categoryQueryRepo.existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(mainCategory.getId(), subCategory.getId(),
                type, userId);
    }

    MainCategorySnapshot getMainCategoryByNameAndUserId(String name, long userId)
    {
//        TODO zmienić wszystkie orElse na orElseGet
//        orElse ma wartość domyślną którą zawsze wypluwa
        return this.mainCategoryQueryRepo.findByNameAndUserId(name, userId)
                .orElseGet(() -> createMainCategoryByNameAndUserId(name, userId));
    }

//    TODO id jest jednoznaczne więc mozna usunąc userId
    MainCategorySnapshot getMainCategoryById(long id)
    {
        return this.mainCategoryQueryRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Main category for given id not found!"));
    }

    MainCategorySnapshot createMainCategoryByNameAndUserId(String name, long userId)
    {
//        TODO set<subcats>
        return this.mainCategoryRepo.save(new MainCategorySnapshot(null, name, null, new UserSource(userId)));
//        return this.mainCategoryRepo.save(MainCategory.restore(new MainCategorySnapshot(null, name, null, new UserSource(String.valueOf(userId)))));
    }

    SubCategorySnapshot getSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryQueryRepo.findByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId)
                .orElseGet(() -> createSubCategoryByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId));
    }

    SubCategorySnapshot createSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
    {
        return this.subCategoryRepo.save(new SubCategorySnapshot(null, name,
                getMainCategoryById(mainCategoryId), new UserSource(userId)));
//                getMainCategoryByIdAndUserId(mainCategoryId, userId).getSnapshot(), new UserSource(String.valueOf(userId)))));
    }

    public List<CategoryDTO> getCategoriesByUserIdAsDto(Long userId)
    {
        return getAllCategoriesByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<CategoryWithIdAndNameDTO> getCategoriesIdsAndNamesByUserIdAsDto(long userId)
    {
        return this.categoryQueryRepo.findCategoriesIdsAndNamesByUserId(userId);
    }

    List<CategorySnapshot> getAllCategoriesByUserId(long userId)
    {
        return this.categoryQueryRepo.findByUserId(userId);
    }

     List<CategorySnapshot> getCategoriesByTypeAndUserId(CategoryType categoryType, long userId)
    {
        return this.categoryQueryRepo.findByTypeAndUserId(categoryType, userId);
    }

    CategorySnapshot getCategoryByNameAndUserId(String name, Long userId)
    {
        var names = splitCategoryNameIntoMainAndSubNames(name);

        return getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(names[0], names[1], userId);
    }

    CategorySnapshot getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName, String subCategoryName, Long userId)
    {
        return this.categoryQueryRepo.findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(mainCategoryName, subCategoryName, userId)
                .orElseThrow(() -> new IllegalArgumentException("Category with given data not found!"));
    }

    List<SubCategorySnapshot> getSubcategoriesByMainCategoryNameAndUserId(final String main, final Long userId)
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

        var snapshots = getSubcategoriesByNameAndUserId(names[1], userId);

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

    List<SubCategorySnapshot> getSubcategoriesByNameAndUserId(final String name, final long userId)
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
//        TODO optional?
        return this.categoryQueryRepo.getTypeById(categoryId);
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

    public List<CategoryWithIdAndNameAndTypeDTO> getCategoriesByIdsAsDto(final List<Long> catIds)
    {
        return this.categoryQueryRepo.findCategoriesIdsAndNamesAndTypesByIds(catIds);
    }

    public List<Long> getCategoriesIdsByUserId(final Long userId)
    {
        return this.categoryQueryRepo.findIdsByUserId(userId);
    }
}