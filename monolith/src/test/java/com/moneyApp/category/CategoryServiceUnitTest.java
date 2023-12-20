package com.moneyApp.category;

import com.moneyApp.category.dto.CategoryDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CategoryServiceUnitTest
{
//    @Test
//    void createCategory_shouldReturnCreatedCategoryIfNotAlreadyInDb()
//    {
////        given
//        var mainCategory = new MainCategory(5L, "Spożywka");
//        var subCategory = new SubCategory(4L, "Jedzenie", mainCategory);
//        var user = new User(1L, "foo@example.com");
//
//        var mockUserService = mock(UserService.class);
//        given(mockUserService.getUserByEmail(anyString())).willReturn(user);
//
//        var mockMainCategoryRepo = mock(MainCategoryRepository.class);
//        given(mockMainCategoryRepo.findByNameAndUserId(anyString(), anyLong())).willReturn(Optional.of(mainCategory));
//        given((mockMainCategoryRepo.findByIdAndUserId(anyLong(), anyLong()))).willReturn(Optional.of(mainCategory));
//
//        var mockSubCategoryRepo = mock(SubCategoryRepository.class);
//        given(mockSubCategoryRepo.findByNameAndMainCategoryIdAndUserId(anyString(), anyLong(), anyLong()))
//                .willReturn(Optional.of(subCategory));
//
//        var mockCategoryRepo = mock(CategoryRepository.class);
//        given(mockCategoryRepo.save(any())).willReturn(new Category(mainCategory, subCategory, CategoryType.EXPENSE,
//                "", user));
//        given(mockCategoryRepo.existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(anyLong(), anyLong(), any(), anyLong()))
//                .willReturn(false);
//
//        var dto = new CategoryDTO("Spożywka", "Jedzenie", CategoryType.EXPENSE, "foo");
//
////        system under test
//        var toTest = new CategoryService(mockCategoryRepo, mockMainCategoryRepo, mockSubCategoryRepo, mockUserService);
//
////        when
//        var result = toTest.createCategoryByUserEmail(dto, "foo@example.com");
//
////        then
//        assertNotNull(result);
//        assertEquals("Spożywka : Jedzenie", result.getCategoryName());
//        assertEquals(CategoryType.EXPENSE, result.getType());
//    }
//
//    @Test
//    void createCategory_shouldThrowIllegalArgumentExceptionIfCategoryAlreadyInDb()
//    {
////        given
//        var mainCategory = new MainCategory(5L, "Spożywka");
//        var subCategory = new SubCategory(4L, "Jedzenie", mainCategory);
//        var user = new User(1L, "foo@example.com");
//
//        var mockUserService = mock(UserService.class);
//        given(mockUserService.getUserByEmail(anyString())).willReturn(user);
//
//        var mockMainCategoryRepo = mock(MainCategoryRepository.class);
//        given(mockMainCategoryRepo.findByNameAndUserId(anyString(), anyLong())).willReturn(Optional.of(mainCategory));
//        given((mockMainCategoryRepo.findByIdAndUserId(anyLong(), anyLong()))).willReturn(Optional.of(mainCategory));
//
//        var mockSubCategoryRepo = mock(SubCategoryRepository.class);
//        given(mockSubCategoryRepo.findByNameAndMainCategoryIdAndUserId(anyString(), anyLong(), anyLong()))
//                .willReturn(Optional.of(subCategory));
//
//        var mockCategoryRepo = mock(CategoryRepository.class);
//        given(mockCategoryRepo.save(any())).willReturn(new Category(mainCategory, subCategory, CategoryType.EXPENSE,
//                "", user));
//        given(mockCategoryRepo.existsByMainCategoryIdAndSubCategoryIdAndTypeAndUserId(anyLong(), anyLong(), any(), anyLong()))
//                .willReturn(true);
//
//        var dto = new CategoryDTO("Spożywka", "Jedzenie", CategoryType.EXPENSE, "foo");
//
////        system under test
//        var toTest = new CategoryService(mockCategoryRepo, mockMainCategoryRepo, mockSubCategoryRepo, mockUserService);
//
////        when
//        var result = catchThrowable(() -> toTest.createCategoryByUserEmail(dto, "foo@example.com"));
//
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("already exists");
//    }
//
//    @Test
//    void getMainCategoryByNameAndUserId_shouldReturnMainCategoryIfFoundInDb()
//    {
////        given
//        var mainCategory = new MainCategory(5L, "Spożywka");
//
//        var mockMainCategoryRepo = mock(MainCategoryRepository.class);
//        given(mockMainCategoryRepo.findByNameAndUserId(anyString(), anyLong())).willReturn(Optional.of(mainCategory));
//
//        var user = new User(1L, "foo@example.com");
//
//        var mockUserService = mock(UserService.class);
//        given(mockUserService.getUserById(anyLong())).willReturn(user);
//
////        system under test
//        var toTest = new CategoryService(null, mockMainCategoryRepo, null, mockUserService);
//
////        when
//        var result = toTest.getMainCategoryByNameAndUserId("Spożywka", 5L);
//
////        then
//        assertNotNull(result);
//        assertEquals("Spożywka", result.getName());
//    }
//
//    @Test
//    void getMainCategoryByNameAndUserId_shouldCreateMainCategoryIfNotFoundInDb()
//    {
////        given
//        var mainCategory = new MainCategory(5L, "Spożywka");
//
//        var mockMainCategoryRepo = mock(MainCategoryRepository.class);
//        given(mockMainCategoryRepo.findByNameAndUserId(anyString(), anyLong())).willReturn(Optional.empty());
//        given(mockMainCategoryRepo.save(any())).willReturn(mainCategory);
//
//        var user = new User(1L, "foo@example.com");
//
//        var mockUserService = mock(UserService.class);
//        given(mockUserService.getUserById(anyLong())).willReturn(user);
//
////        system under test
//        var toTest = new CategoryService(null, mockMainCategoryRepo, null, mockUserService);
//
////        when
//        var result = toTest.getMainCategoryByNameAndUserId("Spożywka", 5L);
//
////        then
//        assertNotNull(result);
//        assertEquals("Spożywka", result.getName());
//    }
//
//    @Test
//    void getMainCategoryByIdAndUserId_shouldReturnMainCategoryIfFoundInDb()
//    {
////        given
//        var mainCategory = new MainCategory(5L, "Spożywka");
//
//        var mockMainCategoryRepo = mock(MainCategoryRepository.class);
//        given(mockMainCategoryRepo.findByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.of(mainCategory));
//
//        var user = new User(1L, "foo@example.com");
//
//        var mockUserService = mock(UserService.class);
//        given(mockUserService.getUserById(anyLong())).willReturn(user);
//
////        system under test
//        var toTest = new CategoryService(null, mockMainCategoryRepo, null, mockUserService);
//
////        when
//        var result = toTest.getMainCategoryById(5L, 1L);
//
////        then
//        assertNotNull(result);
//        assertEquals("Spożywka", result.getName());
//        assertEquals(5L, result.getId());
//    }
//
//    @Test
//    void getMainCategoryByIdAndUserId_shouldThrowIllegalArgumentExceptionIfNotFoundInDb()
//    {
////        given
//        var mockMainCategoryRepo = mock(MainCategoryRepository.class);
//        given(mockMainCategoryRepo.findByIdAndUserId(anyLong(), anyLong())).willReturn(Optional.empty());
//
//        var user = new User(1L, "foo@example.com");
//
//        var mockUserService = mock(UserService.class);
//        given(mockUserService.getUserById(anyLong())).willReturn(user);
////        system under test
//        var toTest = new CategoryService(null, mockMainCategoryRepo, null, mockUserService);
//
////        when
//        var result = catchThrowable(() -> toTest.getMainCategoryById(5L, 1L));
//
////        then
//        assertThat(result)
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("No main category found");
//    }

//    MainCategory createMainCategoryByNameAndUserId(String name, long userId)
//    {
//        return this.mainCategoryRepo.save(new MainCategory(name, this.userService.getUserById(userId)));
//    }
//
//    SubCategory getSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
//    {
//        return this.subCategoryRepo.findByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId)
//                .orElse(createSubCategoryByNameAndMainCategoryIdAndUserId(name, mainCategoryId, userId));
//    }
//
//    SubCategory createSubCategoryByNameAndMainCategoryIdAndUserId(String name, long mainCategoryId, long userId)
//    {
//        return this.subCategoryRepo.save(new SubCategory(name,
//                getMainCategoryByIdAndUserId(mainCategoryId, userId) , this.userService.getUserById(userId)));
//    }
//
//    public List<CategoryDTO> getAllCategoriesByUserEmailAsDto(String email)
//    {
//        return getAllCategoriesByUserId(this.userService.getUserIdByEmail(email))
//                .stream()
//                .map(Category::toDto)
//                .collect(Collectors.toList());
//    }
//
//    public List<Category> getAllCategoriesByUserId(long userId)
//    {
//        return this.repo.findByUserId(userId);
//    }
//
//    public List<Category> getCategoriesByTypeAndUserId(CategoryType categoryType, long userId)
//    {
//        return this.repo.findByTypeAndUserId(categoryType, userId);
//    }
//
//    public Category getCategoryByNameAndUserId(String name, Long userId)
//    {
//        if (name.contains(" : "))
//        {
//            var mainCategoryName = name.replaceAll(" .*", "");
//            var subCategoryName = name.replaceAll(".* : ", "");
//
//            return getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(mainCategoryName, subCategoryName, userId);
//        }
//        else
//            throw new IllegalArgumentException("Invalid category name!");
//
//    }
//
//    private Category getCategoryByMainCategoryNameAndSubcategoryNameAndUserId(String mainCategoryName, String subCategoryName, Long userId)
//    {
//        return this.repo.findCategoryByMainCategoryNameAndSubcategoryNameAndUserId(mainCategoryName, subCategoryName, userId)
//                .orElseThrow(() -> new IllegalArgumentException("No category for given data!"));
//    }

//        given
//        system under test
//        when
//        then
}