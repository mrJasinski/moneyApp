package com.moneyApp.payment;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceUnitTest
{
    @Test
    void generatePaymentPositions_shouldReturnSetWithOnePositionForFrequencyTypeOnce()
    {
//        given
        var payment = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.ONCE
                , 5
                , null
                , 0L
                , null
                , new HashSet<>());

//        system under test
        var toTest = new PaymentService(null, null, null);

//        when
        var result = toTest.generatePaymentPositions(payment);

//        then
        assertEquals(1, result.size());
    }

    @Test
    void generatePaymentPositions_shouldReturnSetWithGivenPositionsAmountForFrequencyTypeOtherThanOnce()
    {
//        given
        var payment1 = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.WEEKLY
                , 3
                , null
                , 0L
                , null
                , new HashSet<>());

        var payment2 = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.MONTHLY
                , 5
                , null
                , 0L
                , null
                , new HashSet<>());

        var payment3 = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.QUARTERLY
                , 4
                , null
                , 0L
                , null
                , new HashSet<>());

        var payment4 = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.YEARLY
                , 2
                , null
                , 0L
                , null
                , new HashSet<>());

//        system under test
        var toTest = new PaymentService(null, null, null);

//        when
        var result1 = toTest.generatePaymentPositions(payment1);
        var result2 = toTest.generatePaymentPositions(payment2);
        var result3 = toTest.generatePaymentPositions(payment3);
        var result4 = toTest.generatePaymentPositions(payment4);

//        then
        assertEquals(3, result1.size());
        assertEquals(5, result2.size());
        assertEquals(4, result3.size());
        assertEquals(2, result4.size());

    }

    @Test
    void generatePaymentPositions_shouldReturnPositionsSetWithWeeklyPaymentDatesSpacingForFrequencyTypeWeekly()
    {
//        given
        var payment = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.WEEKLY
                , 3
                , null
                , 0L
                , null
                , new HashSet<>());

//        system under test
        var toTest = new PaymentService(null, null, null);

//        when
        var result = toTest.generatePaymentPositions(payment);

//        then
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now()));
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now().plusWeeks(1)));
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now().plusWeeks(2)));
    }

    @Test
    void generatePaymentPositions_shouldReturnPositionsSetWithMonthlyPaymentDatesSpacingForFrequencyTypeMonthly()
    {
//        given
        var payment = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.MONTHLY
                , 3
                , null
                , 0L
                , null
                , new HashSet<>());

//        system under test
        var toTest = new PaymentService(null, null, null);

//        when
        var result = toTest.generatePaymentPositions(payment);

//        then
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now()));
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now().plusMonths(1)));
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now().plusMonths(2)));
    }

    @Test
    void generatePaymentPositions_shouldReturnPositionsSetWithThreeMonthPaymentDatesSpacingForFrequencyTypeQuarterly()
    {
//        given
        var payment = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.QUARTERLY
                , 3
                , null
                , 0L
                , null
                , new HashSet<>());

//        system under test
        var toTest = new PaymentService(null, null, null);

//        when
        var result = toTest.generatePaymentPositions(payment);

//        then
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now()));
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now().plusMonths(3)));
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now().plusMonths(6)));
    }

    @Test
    void generatePaymentPositions_shouldReturnPositionsSetWithYearlyPaymentDatesSpacingForFrequencyTypeYearly()
    {
//        given
        var payment = new PaymentSnapshot(
                null
                , LocalDate.now()
                , PaymentFrequency.YEARLY
                , 3
                , null
                , 0L
                , null
                , new HashSet<>());

//        system under test
        var toTest = new PaymentService(null, null, null);

//        when
        var result = toTest.generatePaymentPositions(payment);

//        then
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now()));
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now().plusYears(1)));
        assertTrue(result.stream().map(PaymentPositionSnapshot::getPaymentDate).toList().contains(LocalDate.now().plusYears(2)));
    }


//    given
//    system under test
//    when
//    then
}