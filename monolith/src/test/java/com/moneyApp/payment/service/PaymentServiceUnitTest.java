package com.moneyApp.payment.service;

import com.moneyApp.payment.Payment;
import com.moneyApp.payment.PaymentDate;
import com.moneyApp.payment.PaymentFrequency;
import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.payment.repository.PaymentDateRepository;
import com.moneyApp.payment.repository.PaymentRepository;
import com.moneyApp.user.User;
import com.moneyApp.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class PaymentServiceUnitTest
{
    @Test
    void getActualMonthPaymentsByUserId_shouldReturnPaymentsForGivenMonthAndYear()
    {
//        given
        var p1 = new Payment();
        var p2 = new Payment();
        var p3 = new Payment();

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findPaymentsByDatesAndUserId(any(), any(), anyLong())).willReturn(List.of(p1, p2, p3));

//        system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

//        when
        var result = toTest.getActualMonthPaymentsByUserId(1L);

//        then
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void getActualMonthPaymentsByUserIdAsDto_shouldReturnPaymentsListConvertedToDto()
    {
//        given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.MONTHLY, 3, "foo", 242.5, null,
                new HashSet<>(List.of(new PaymentDate(), new PaymentDate(), new PaymentDate())));
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo", 112, null,
                new HashSet<>(List.of(new PaymentDate())));

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findPaymentsByDatesAndUserId(any(), any(), anyLong())).willReturn(List.of(p1, p2));

//        system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

//        when
        var result = toTest.getActualMonthPaymentsByUserIdAsDto(1L);

//        then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertThat(result.get(0)).isInstanceOf(PaymentDTO.class);
    }

    @Test
    void getPaymentsFromNowTillDateByUserId_shouldReturnPaymentsListForGivenDate()
    {
//        given
        var p1 = new Payment();
        var p2 = new Payment();
        var p3 = new Payment();

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findPaymentsByDatesAndUserId(any(), any(), anyLong())).willReturn(List.of(p1, p2, p3));

//        system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

//        when
        var result = toTest.getPaymentsFromNowTillDateByUserId(LocalDate.now(), 1L);

//        then
        assertNotNull(result);
        assertEquals(3, result.size());

    }

    @Test
    void getPaymentsFromNowTillDateByUserIdAsDto_shouldReturnPaymentsListConvertedToDto()
    {
    //        given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.MONTHLY, 3, "foo", 242.5, null,
                new HashSet<>(List.of(new PaymentDate(), new PaymentDate(), new PaymentDate())));
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo", 112, null,
                new HashSet<>(List.of(new PaymentDate())));

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findPaymentsByDatesAndUserId(any(), any(), anyLong())).willReturn(List.of(p1, p2));

    //        system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

    //        when
        var result = toTest.getPaymentsFromNowTillDateByUserIdAsDto(LocalDate.now(), 1L);

    //        then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertThat(result.get(0)).isInstanceOf(PaymentDTO.class);
    }

    @Test
    void getPaymentsFromNowTillDateWithPreviousUnpaidByUserId_shouldReturnMergedLists()
    {
//    given
        var p1 = new Payment();
        var p2 = new Payment();
        var p3 = new Payment();
        var p4 = new Payment();
        var p5 = new Payment();

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findPaymentsByDatesAndUserId(any(), any(), anyLong())).willReturn(List.of(p1, p2, p3));
        given(mockPaymentRepo.findNotPaidTillDateByUserId(any(), anyLong())).willReturn(List.of(p4, p5));

//    system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

//    when
        var result = toTest.getPaymentsFromNowTillDateWithPreviousUnpaidByUserId(LocalDate.now(), 2L);

//    then
        assertNotNull(result);
        assertEquals(5 , result.size());
    }

    @Test
    void getPaymentsFromNowTillDateWithPreviousUnpaidByUserIdAsDto_shouldReturnMergedListsConvertedToDto()
    {
//    given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.MONTHLY, 3, "foo", 242.5, null,
                new HashSet<>(List.of(new PaymentDate(), new PaymentDate(), new PaymentDate())));
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo", 112, null,
                new HashSet<>(List.of(new PaymentDate())));
        var p3 = new Payment(LocalDate.now(), PaymentFrequency.WEEKLY, 2, "foo", 112, null,
                new HashSet<>(List.of(new PaymentDate())));

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findPaymentsByDatesAndUserId(any(), any(), anyLong())).willReturn(List.of(p1, p2));
        given(mockPaymentRepo.findNotPaidTillDateByUserId(any(), anyLong())).willReturn(List.of(p3));

//    system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

//    when
        var result = toTest.getPaymentsFromNowTillDateWithPreviousUnpaidByUserIdAsDto(LocalDate.now(), 3L);

//    then
        assertNotNull(result);
        assertEquals(5 , result.size());
        assertThat(result.get(0)).isInstanceOf(PaymentDTO.class);
    }

    @Test
    void getUnpaidPaymentsSumByList_shouldSumPaymentsWithUnpaidStatusFromGivenList()
    {
//    given
        var dto1 = new PaymentDTO(null, null, 100, false);
        var dto2 = new PaymentDTO(null, null, 50, true);
        var dto3 = new PaymentDTO(null, null, 120, false);

        var payments = new ArrayList<PaymentDTO>();
        payments.add(dto1);
        payments.add(dto2);
        payments.add(dto3);

//    system under test
        var toTest = new PaymentService(null, null, null);

//    when
        var result = toTest.getUnpaidPaymentsSumByList(payments);

//    then
        assertEquals(220, result);
    }

    @Test
    void convertPaymentsToSimpleDto_shouldConvertPaymentEntityToDtoForDashboard()
    {
//    given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.MONTHLY, 3, "foo", 242.5, null,
                new HashSet<>(List.of(new PaymentDate(), new PaymentDate(), new PaymentDate())));
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo", 112, null,
                new HashSet<>(List.of(new PaymentDate())));

//    system under test
        var toTest = new PaymentService(null, null, null);

//    when
        var result = toTest.convertPaymentsToSimpleDto(List.of(p1, p2));

//    then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertThat(result.get(0)).isInstanceOf(PaymentDTO.class);
    }

    @Test
    void createPaymentByUserEmail_shouldThrowExceptionWhenFrequencyTypeIsOtherThanOnceAndWrongFrequencyIsGiven()
    {
//        given
        var payment = new Payment(null, PaymentFrequency.MONTHLY, 1, null, 0, null);

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.save(any())).willReturn(payment);

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserByEmail(anyString())).willReturn(new User());

//        system under test
        var toTest = new PaymentService(mockPaymentRepo, null, mockUserService);

//        when
        var result = catchThrowable(() -> toTest.createPaymentByUserEmail(new PaymentDTO(), ""));

//        then
        assertThat(result)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wrong payment frequency");
    }

    @Test
    void createPaymentByUserEmail_shouldReturnPaymentWhenFrequencyTypeIsOnceAndWrongFrequencyIsGiven()
    {
//        given
        var payment = new Payment(null, PaymentFrequency.ONCE, -10, null, 0, null);

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.save(any())).willReturn(payment);

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserByEmail(anyString())).willReturn(new User());

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(new PaymentDate());

//        system under test
        var toTest = new PaymentService(mockPaymentRepo, mockPaymentDateRepo, mockUserService);

//        when
        var result = toTest.createPaymentByUserEmail(new PaymentDTO(), "");

//        then
        assertNotNull(result);
        assertEquals(1, result.getDates().size());
        assertThat(result).isInstanceOf(Payment.class);
    }

    @Test
    void createPaymentByUserEmail_shouldReturnPaymentWhenFrequencyTypeIsOtherThanOnceAndCorrectFrequencyIsGiven()
    {
//        given
        var payment = new Payment(LocalDate.now(), PaymentFrequency.WEEKLY, 3, null, 0, null);

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.save(any())).willReturn(payment);

        var mockUserService = mock(UserService.class);
        given(mockUserService.getUserByEmail(anyString())).willReturn(new User());

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(new PaymentDate(), new PaymentDate(), new PaymentDate());

//        system under test
        var toTest = new PaymentService(mockPaymentRepo, mockPaymentDateRepo, mockUserService);

//        when
        var result = toTest.createPaymentByUserEmail(new PaymentDTO(), "");

//        then
        assertNotNull(result);
        assertEquals(3, result.getDates().size());
        assertThat(result).isInstanceOf(Payment.class);
    }

    @Test
    void generatePaymentDates_shouldCreateOnePaymentDateForFrequencyTypeOnce()
    {
//    given
        var payment = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo", 242.5, null);

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(new PaymentDate());

//    system under test
        var toTest = new PaymentService(null, mockPaymentDateRepo, null);

//    when
        toTest.generatePaymentDates(payment);

//    then
        assertEquals(1, payment.getPositions().size());
    }

    @Test
    void generatePaymentDates_shouldCreateOnePaymentDateForFrequencyTypeOnceDespiteOfGivenFrequencyNumber()
    {
        //    given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo", 242.5, null);
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 10, "foo", 242.5, null);
        var p3 = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 100, "foo", 242.5, null);
        var p4 = new Payment(LocalDate.now(), PaymentFrequency.ONCE, 12, "foo", 242.5, null);

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(new PaymentDate());

//    system under test
        var toTest = new PaymentService(null, mockPaymentDateRepo, null);

//    when
        toTest.generatePaymentDates(p1);
        toTest.generatePaymentDates(p2);
        toTest.generatePaymentDates(p3);
        toTest.generatePaymentDates(p4);

//    then
        assertEquals(1, p1.getPositions().size());
        assertEquals(1, p2.getPositions().size());
        assertEquals(1, p3.getPositions().size());
        assertEquals(1, p4.getPositions().size());

    }

    @Test
    void generatePaymentDates_shouldCreateWeeklyPaymentDatesForGivenFrequencyNumber()
    {
//    given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.WEEKLY, 2, "foo", 242.5, null);
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.WEEKLY, 5, "foo", 242.5, null);

//        ilość dx ma się pokrywać z ilością wywołań na mocku (lub być większa)
        var d1 = new PaymentDate(LocalDate.now(), null);
        var d2 = new PaymentDate(LocalDate.now().plusWeeks(1), null);
        var d3 = new PaymentDate(LocalDate.now().plusWeeks(2), null);
        var d4 = new PaymentDate(LocalDate.now().plusWeeks(3), null);
        var d5 = new PaymentDate(LocalDate.now().plusWeeks(4), null);
        var d6 = new PaymentDate(LocalDate.now().plusWeeks(5), null);
        var d7 = new PaymentDate(LocalDate.now().plusWeeks(6), null);

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(d1, d2, d3, d4, d5, d6, d7);

//    system under test
        var toTest = new PaymentService(null, mockPaymentDateRepo, null);

//    when
        toTest.generatePaymentDates(p1);
        toTest.generatePaymentDates(p2);

//    then
        assertEquals(2, p1.getPositions().size());
        assertEquals(5, p2.getPositions().size());
    }

    @Test
    void generatePaymentDates_shouldCreateMonthlyPaymentDatesForGivenFrequencyNumber()
    {
//    given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.MONTHLY, 2, "foo", 242.5, null);
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.MONTHLY, 5, "foo", 242.5, null);

//        ilość dx ma się pokrywać z ilością wywołań na mocku (lub być większa)
        var d1 = new PaymentDate(LocalDate.now(), null);
        var d2 = new PaymentDate(LocalDate.now().plusMonths(1), null);
        var d3 = new PaymentDate(LocalDate.now().plusMonths(2), null);
        var d4 = new PaymentDate(LocalDate.now().plusMonths(3), null);
        var d5 = new PaymentDate(LocalDate.now().plusMonths(4), null);
        var d6 = new PaymentDate(LocalDate.now().plusMonths(5), null);
        var d7 = new PaymentDate(LocalDate.now().plusMonths(6), null);

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(d1, d2, d3, d4, d5, d6, d7);

//    system under test
        var toTest = new PaymentService(null, mockPaymentDateRepo, null);

//    when
        toTest.generatePaymentDates(p1);
        toTest.generatePaymentDates(p2);

//    then
        assertEquals(2, p1.getPositions().size());
        assertEquals(5, p2.getPositions().size());
    }

    @Test
    void generatePaymentDates_shouldCreateQuarterlyPaymentDatesForGivenFrequencyNumber()
    {
//    given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.QUARTERLY, 2, "foo", 242.5, null);
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.QUARTERLY, 5, "foo", 242.5, null);

//        ilość dx ma się pokrywać z ilością wywołań na mocku (lub być większa)
        var d1 = new PaymentDate(LocalDate.now(), null);
        var d2 = new PaymentDate(LocalDate.now().plusMonths(3), null);
        var d3 = new PaymentDate(LocalDate.now().plusMonths(6), null);
        var d4 = new PaymentDate(LocalDate.now().plusMonths(9), null);
        var d5 = new PaymentDate(LocalDate.now().plusMonths(12), null);
        var d6 = new PaymentDate(LocalDate.now().plusMonths(15), null);
        var d7 = new PaymentDate(LocalDate.now().plusMonths(18), null);

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(d1, d2, d3, d4, d5, d6, d7);

//    system under test
        var toTest = new PaymentService(null, mockPaymentDateRepo, null);

//    when
        toTest.generatePaymentDates(p1);
        toTest.generatePaymentDates(p2);

//    then
        assertEquals(2, p1.getPositions().size());
        assertEquals(5, p2.getPositions().size());
    }

    @Test
    void generatePaymentDates_shouldCreateYearlyPaymentDatesForGivenFrequencyNumber()
    {
//    given
        var p1 = new Payment(LocalDate.now(), PaymentFrequency.YEARLY, 2, "foo", 242.5, null);
        var p2 = new Payment(LocalDate.now(), PaymentFrequency.YEARLY, 5, "foo", 242.5, null);

//        ilość dx ma się pokrywać z ilością wywołań na mocku (lub być większa)
        var d1 = new PaymentDate(LocalDate.now(), null);
        var d2 = new PaymentDate(LocalDate.now().plusYears(1), null);
        var d3 = new PaymentDate(LocalDate.now().plusYears(2), null);
        var d4 = new PaymentDate(LocalDate.now().plusYears(3), null);
        var d5 = new PaymentDate(LocalDate.now().plusYears(4), null);
        var d6 = new PaymentDate(LocalDate.now().plusYears(5), null);
        var d7 = new PaymentDate(LocalDate.now().plusYears(6), null);

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(d1, d2, d3, d4, d5, d6, d7);

//    system under test
        var toTest = new PaymentService(null, mockPaymentDateRepo, null);

//    when
        toTest.generatePaymentDates(p1);
        toTest.generatePaymentDates(p2);

//    then
        assertEquals(2, p1.getPositions().size());
        assertEquals(5, p2.getPositions().size());
    }

    @Test
    void createPaymentDateByDate_shouldReturnCreatedPaymentDate()
    {
//    given
        var payment = new Payment();

        var mockPaymentDateRepo = mock(PaymentDateRepository.class);
        given(mockPaymentDateRepo.save(any())).willReturn(new PaymentDate(LocalDate.now(), payment));

//    system under test
        var toTest = new PaymentService(null, mockPaymentDateRepo, null);

//    when
        var result = toTest.createPaymentDateByDate(LocalDate.now(), payment);

//    then
        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getPaymentDate());
        assertNotNull(result.getPayment());
    }

    @Test
    void getPaymentsByUserIdAsDto_shouldReturnPaymentListConvertedToDto()
    {
//    given
        var p1 = new Payment(null, null, 0, null, 0, null, new HashSet<>());
        var p2 = new Payment(null, null, 0, null, 0, null, new HashSet<>());
        var p3 = new Payment(null, null, 0, null, 0, null, new HashSet<>());

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findByUserId(anyLong())).willReturn(List.of(p1, p2, p3));

//    system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

//    when
        var result = toTest.getPaymentsByUserIdAsDto(1L);

//    then
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertThat(result.get(0)).isInstanceOf(PaymentDTO.class);

    }

    @Test
    void getPaymentsByUserId_shouldReturnPaymentListFromDb()
    {
//    given
        var p1 = new Payment();
        var p2 = new Payment();
        var p3 = new Payment();

        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findByUserId(anyLong())).willReturn(List.of(p1, p2, p3));

//    system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

//    when
        var result = toTest.getPaymentsByUserId(1L);

//    then
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
    }

    @Test
    void getPaymentsByUserId_shouldReturnEmptyListIfPaymentsNotFoundInDb()
    {
//    given
        var mockPaymentRepo = mock(PaymentRepository.class);
        given(mockPaymentRepo.findByUserId(anyLong())).willReturn(List.of());

//    system under test
        var toTest = new PaymentService(mockPaymentRepo, null, null);

//    when
        var result = toTest.getPaymentsByUserId(1L);

//    then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

//    given
//    system under test
//    when
//    then
}