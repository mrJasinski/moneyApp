package com.moneyApp.budget.dto;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BudgetDTOTest
{
    @Test
    void roundToTwoDecimals_shouldReturnDoubleRoundedUpToTwoDecimals()
    {
//        given

//        system under test
        var toTest = new BudgetDTO();

//        when
        var result = toTest.roundToTwoDecimals(12.34564567);

//        then
        System.out.println("result " + result);
    }

}