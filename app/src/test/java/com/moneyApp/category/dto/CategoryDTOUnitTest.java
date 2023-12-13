package com.moneyApp.category.dto;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryDTOUnitTest
{
    @Test
//    non standard letters means ą ż etc
    void getUrlName_shouldReturnCategoryNameWithoutWhiteSpacesAndNonStandardLetters()
    {
        //    given
        var toTest = new CategoryDTO("Spożywka : Woda minelna");

        //    when
        var result = toTest.getUrlName();

        //    then
        assertFalse(result.contains(" "));
        assertFalse(result.contains("ż"));
    }

}