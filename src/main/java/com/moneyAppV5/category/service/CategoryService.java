package com.moneyAppV5.category.service;

import com.moneyAppV5.category.Category;
import com.moneyAppV5.category.MainCategory;
import com.moneyAppV5.category.SubCategory;
import com.moneyAppV5.category.Type;
import com.moneyAppV5.category.dto.*;
import com.moneyAppV5.category.repository.CategoryRepository;
import com.moneyAppV5.category.repository.MainCategoryRepository;
import com.moneyAppV5.category.repository.SubCategoryRepository;
import com.moneyAppV5.transaction.dto.TransactionDTO;
import com.moneyAppV5.transaction.service.TransactionService;
import com.moneyAppV5.utils.UtilService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService
{
    private final CategoryRepository repository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TransactionService transactionService;
    private final UtilService utilService;

    CategoryService(CategoryRepository repository, MainCategoryRepository mainCategoryRepository, SubCategoryRepository subCategoryRepository,
                    TransactionService transactionService, UtilService utilService)
    {
        this.repository = repository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.transactionService = transactionService;
        this.utilService = utilService;
    }

    public MainCategory createMainCategory(final MainCategoryDTO toSave)
    {
        return this.mainCategoryRepository.save(toSave.toMainCategory());
    }
//    public SubCategory createSubCategory(final SubCategory toSave)
//    {
//        return this.subCategoryRepository.save(toSave);
//    }
    public SubCategory createSubCategory(final SubCategoryDTO toSave)
    {
//        TODO obsługa wyjścia z optionala
        return this.subCategoryRepository.save(toSave.toSubCategory(this.mainCategoryRepository.findByMainCategory(toSave.getMainCategory().getMainCategory()).orElseThrow()));
    }

    MainCategory readMainCategoryByName(String name)
    {
        return this.mainCategoryRepository.findByMainCategory(name).orElse(null);
    }

    SubCategory readSubCategoryByName(String name)
    {
        return this.subCategoryRepository.findBySubCategory(name).orElse(null);
    }

    public Category createCategory(final CategoryDTO toSave)
    {
        MainCategory main = this.readMainCategoryByName(toSave.getMain());
        if (main == null)
            main = createMainCategory(new MainCategoryDTO(toSave.getMain()));

        SubCategory sub = readSubCategoryByName(toSave.getSub());
        if (sub == null)
            sub = createSubCategory(new SubCategoryDTO(toSave.getSub(), main));

        var result = new CategoryDTO(main , sub, toSave.getType(), toSave.getDescription());

        return this.repository.save(result.toCategory());
    }

    public Optional<Category> readCategoryById(int id)
    {
        return this.repository.findById(id);
    }

    public boolean existsById(int id)
    {
        return this.repository.existsById(id);
    }

    public List<Category> readAllCategories()
    {
        return this.repository.findAll();
    }

    public List<CategoryDTO> readAllCategoriesAsDto()
    {
        return readAllCategories().stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    public List<Category> readCategoriesByType(Type type)
    {
        return this.repository.findCategoriesByType(type.name());
    }

    public List<CategoryDTO> readCategoriesByTypeAsDto(Type type)
    {
        return readCategoriesByType(type).stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    public List<MainCategoryDTO> readMainCategoriesDtoByType(Type type)
    {
        return readMainCategoriesByType(type).stream().map(MainCategoryDTO::new).collect(Collectors.toList());
    }

    private List<MainCategoryDTO> readMainCategoriesAsDto(List<MainCategory> list)
    {
        List<MainCategoryDTO> dtos = new ArrayList<>();

        for(MainCategory cat : list)
        {
            var dto = new MainCategoryDTO(cat, readSubCategoriesByMainId(cat.getId()));

            if (!dtos.contains(dto))
                dtos.add(dto);
        }

        return dtos;
    }

    public List<MainCategory> readAllMainCategories()
    {
        return this.mainCategoryRepository.findAll();
    }

    public List<MainCategoryDTO> readAllMainCategoriesAsDto()
    {
        return readAllMainCategories().stream().map(MainCategoryDTO::new).collect(Collectors.toList());
    }

    public List<SubCategory> readAllSubCategories()
    {
        return this.subCategoryRepository.findAll();
    }

    public List<SubCategoryDTO> readAllSubCategoriesAsDto()
    {
        return readAllSubCategories().stream().map(SubCategoryDTO::new).collect(Collectors.toList());
    }

    public List<SubCategory> readSubCategoriesByMainId(Integer id)
    {
        return this.subCategoryRepository.findSubCategoriesByMainId(id);
    }

    public List<SubCategoryDTO> readSubCategoriesDtoByMainId(int mainCatId)
    {
        return readSubCategoriesByMainId(mainCatId).stream().map(SubCategoryDTO::new).collect(Collectors.toList());
    }

    public List<Category> readExpenseCategories()
    {
        return this.repository.findAllExpenses();
    }

    public List<Category> readIncomeCategories()
    {
        return this.repository.findAllIncomes();
    }

    List<MainCategory> readMainCategoriesByType(Type type)
    {
        return this.mainCategoryRepository.findByType(type.name());
    }

    public Category readCategoryByHash(Integer hash)
    {
        return this.repository.findCategoryByHash(hash);
    }

    public CategoryDTO readCategoryDtoByHash(Integer hash)
    {
        return new CategoryDTO(readCategoryByHash(hash));
    }

    public boolean existsByCategory(String category)
    {
        return this.repository.existsByCategory(category);
    }

    public boolean existsInDatabase(CategoryDTO dto)
    {
        return this.repository.existsByMainCategoryAndSubCategoryAndType(readMainCategoryByName(dto.getMain()), readSubCategoryByName(dto.getSub()), dto.getType());
    }

    public double sumTransactionsByActualMonthAndCategory(Category category, Integer month, Integer year)
    {
        return this.transactionService.sumActualMonthTransactionsByCategory(category, month, year);
    }

    public double sumOverallTransactionsByCategory(Category category)
    {
        return this.transactionService.sumOverallTransactionsByCategory(category);
    }

    public MainCategory readMainCategoryByHash(int hash)
    {
        return this.mainCategoryRepository.findByHash(hash).orElseThrow();
    }

    public MainCategoryDTO readMainCategoryAsDto(MainCategory main)
    {
        var mainCat = main.toDto();

        mainCat.setWrapper(setActualDataWrapperDto(mainCat.getTransactions()));

        return mainCat;
    }

    private ActualDataWrapperDTO setActualDataWrapperDto(List<TransactionDTO> transactions)
    {
        var wrapper = new ActualDataWrapperDTO();

        wrapper.setActualMonthSum(this.utilService.sumByListAndMonth(transactions, this.utilService.getActualMonthValue(), this.utilService.getActualYear()));
        wrapper.setActualMonthMinusOneSum(this.utilService.sumByListAndMonth(transactions, this.utilService.getActualMonthValue() - 1, this.utilService.getActualYear()));
        wrapper.setActualMonthMinusTwoSum(this.utilService.sumByListAndMonth(transactions, this.utilService.getActualMonthValue() - 2, this.utilService.getActualYear()));
        wrapper.setActualYearSum(this.utilService.sumByListAndYear(transactions, this.utilService.getActualYear()));
        wrapper.setOverallSum(this.utilService.sumByList(transactions));

        wrapper.setActualMonthCount(this.utilService.countByListAndMonth(transactions, this.utilService.getActualMonthValue(), this.utilService.getActualYear()));
        wrapper.setActualMonthMinusOneCount(this.utilService.countByListAndMonth(transactions, this.utilService.getActualMonthValue() - 1, this.utilService.getActualYear()));
        wrapper.setActualMonthMinusTwoCount(this.utilService.countByListAndMonth(transactions, this.utilService.getActualMonthValue() - 2, this.utilService.getActualYear()));
        wrapper.setActualYearCount(this.utilService.countByListAndYear(transactions, this.utilService.getActualYear()));
        wrapper.setOverallCount(this.utilService.countByList(transactions));

        return wrapper;
    }

    public SubCategoryDTO readSubCategoryAsDto(SubCategory sub)
    {
        var subCat = sub.toDto();

//TODO dlaczego wyrzuca numberformat exception?
        //        mainCat.setSubCategoriesDto(main.getSubCategories().stream().map(SubCategoryDTO::new).collect(Collectors.toList()));
        var transactions = this.transactionService.readTransactionsBySubCategoryIdAsDto(sub.getId());
        subCat.setTransactions(transactions);

        subCat.setWrapper(setActualDataWrapperDto(transactions));

        return subCat;
    }

    private List<SubCategory> readSubCategoriesByMainCategoryId(int mainCatId)
    {
        return this.subCategoryRepository.findSubCategoriesByMainId(mainCatId);
    }

    public SubCategoryDTO readSubCategoryByHashAsDto(Integer hash)
    {
        return new SubCategoryDTO(this.subCategoryRepository.findSubCategoryByHash(hash).orElseThrow());
    }

    public SubCategory readSubCategoryByHash(int hash)
    {
       return this.subCategoryRepository.findSubCategoryByHash(hash).orElseThrow();
    }

    public TypeDTO readTypeAsDto(String t)
    {
        var type = new TypeDTO();

        type.setType(t);

        var transactions = this.transactionService.readTransactionsByTypeNameAsDto(t);
        type.setTransactions(transactions);

        type.setWrapper(setActualDataWrapperDto(transactions));

        return  type;
    }
}
