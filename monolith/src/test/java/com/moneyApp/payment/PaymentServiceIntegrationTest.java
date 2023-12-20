package com.moneyApp.payment;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class PaymentServiceIntegrationTest
{
//    @Autowired
//    private PaymentRepository paymentRepo;
//
//    @Autowired
//    private UserService userService;
//
//    User user;
//
//    @BeforeEach
//    void createUser()
//    {
//        if (this.user == null)
//            this.user = this.userService.createUser(new UserDTO("foo@bar.com", "1a@Z!s7y" +
//                "", "bar"));
//    }
//
//    @Test
//    void getActualMonthPaymentsByUserId_shouldReturnPaymentsList()
//    {
////        given
////        var user = this.userService.getUserByEmail("foo");
//
//
//        var p1 = this.paymentRepo.save(new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo",
//                23, user));
//        this.paymentDateRepo.save(new PaymentDate(p1.getStartDate(), p1));
//        var p2 = this.paymentRepo.save(new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "bar",
//                55, user));
//        this.paymentDateRepo.save(new PaymentDate(p2.getStartDate(), p2));
//        var p3 = this.paymentRepo.save(new Payment(LocalDate.now().plusMonths(2), PaymentFrequency.ONCE,
//                1, "foobar", 123, user));
//        this.paymentDateRepo.save(new PaymentDate(p3.getStartDate(), p3));
//
//        var p4 = this.paymentRepo.save(new Payment(LocalDate.now().minusMonths(2), PaymentFrequency.MONTHLY,
//                2, "foobar", 37, user));
//        this.paymentDateRepo.save(new PaymentDate(p4.getStartDate(), p4));
//        this.paymentDateRepo.save(new PaymentDate(p4.getStartDate().plusMonths(1), p4));
//
//        var p5 = this.paymentRepo.save(new Payment(LocalDate.now(), PaymentFrequency.WEEKLY,
//                2, "barfoo", 12, user));
//        this.paymentDateRepo.save(new PaymentDate(p5.getStartDate(), p5));
//        this.paymentDateRepo.save(new PaymentDate(p5.getStartDate().plusWeeks(1), p5));
//
////        system under test
//        var toTest = new PaymentService(this.paymentRepo, null, null);
//
////        when
//        var result = toTest.getActualMonthPaymentsByUserId(user.getId());
//
////        TODO test
//        System.out.println("~~~~~~~~~~~~");
//        for (Payment p : result)
//            System.out.println(p);
//
////        then
//        assertTrue(result.size() > 0);
//        assertEquals(3, result.size());
//        assertThat(result.get(0)).isInstanceOf(Payment.class);
//    }
//
//    @Test
//    void getPaymentsFromNowTillDateByUserId_shouldReturnPaymentsList()
//    {
////        given
//        var user = this.userService.getUserByEmail("foo");
//
////        system under test
//        var toTest = new PaymentService(this.paymentRepo, null, null);
//
////        when
//        var result = toTest.getPaymentsFromNowTillDateByUserId(LocalDate.now().plusWeeks(2), user.getId());
//
////        then
//        assertTrue(result.size() > 0);
//        assertEquals(3, result.size());
//        assertThat(result.get(0)).isInstanceOf(Payment.class);
//    }
//
//    @Test
//    void getUnpaidPaymentsTillDateByUserId_shouldReturnListOfUnpaidPaymentsTillGivenDate()
//    {
////        given
//        var user = this.userService.getUserByEmail("foo");
//
////        system under test
//        var toTest = new PaymentService(this.paymentRepo, null, null);
//
////        when
//        var result = toTest.getUnpaidPaymentsTillDateByUserId(LocalDate.now(), user.getId());
//
////        then
//        assertTrue(result.size() > 0);
//        assertEquals(4, result.size());
//        assertThat(result.get(0)).isInstanceOf(Payment.class);
//    }
//
//    @Test
//    void createPaymentByUserEmail_shouldReturnCreatedPayment()
//    {
////        given
//        var dto1 = new PaymentDTO(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo", 23);
//        var dto2 = new PaymentDTO(LocalDate.now(), PaymentFrequency.WEEKLY, 3, "bar", 55);
//
//        var user = this.userService.getUserByEmail("foo");
//
////        system under test
//        var toTest = new PaymentService(this.paymentRepo, this.paymentDateRepo, this.userService);
//
////        when
//        var result1 = toTest.createPaymentByUserId(dto1, "foo");
//        var result2 = toTest.createPaymentByUserId(dto2, "foo");
//
////        then
//        assertNotNull(result1);
//        assertEquals(1, result1.getDates().size());
//        assertEquals(LocalDate.now(), result1.getStartDate());
//        assertEquals(PaymentFrequency.ONCE, result1.getFrequencyType());
//        assertEquals("foo", result1.getDescription());
//        assertEquals(user.getEmail(), result1.getUser().getEmail());
//        assertEquals(23, result1.getAmount());
//        assertThat(result1).isInstanceOf(Payment.class);
//
//        assertNotNull(result2);
//        assertTrue(result2.getDates().size() > 1);
//        assertEquals(3, result2.getDates().size());
//
//    }
//
//    @Test
//    void createPaymentByUserEmail_shouldThrowExceptionIfFrequencyTypeIsOtherThanOnceAndWrongFrequencyIsGiven()
//    {
////        given
//        var dto = new PaymentDTO(LocalDate.now(), PaymentFrequency.WEEKLY, 1, "bar", 55);
//
////        system under test
//        var toTest = new PaymentService(this.paymentRepo, this.paymentDateRepo, this.userService);
//
////        when
//        var result = catchThrowable(() -> toTest.createPaymentByUserId(dto, "foo"));
//
////        then
//        assertThat(result).isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Wrong payment frequency");
//    }
////
////    public void setPaymentAsPaid()
////    {
//////        TODO określenie warunku na odnalezienie płatności
//////        this.paymentDateRepo.setPaymentAsPaid();
////    }
////
//
////    void generatePaymentDates(Payment payment)
////    {
////        var result = payment.getDates();
////
////        switch (payment.getFrequencyType())
////        {
////            case ONCE -> result.add(createPaymentDateByDate(payment.getStartDate(), payment));
////            case WEEKLY ->
////            {
////                for (int i = 0; i < payment.getFrequency(); i++)
////                    result.add(createPaymentDateByDate(payment.getStartDate().plusWeeks(i), payment));
////            }
////            case MONTHLY ->
////            {
////                for (int i = 0; i < payment.getFrequency(); i++)
////                    result.add(createPaymentDateByDate(payment.getStartDate().plusMonths(i), payment));
////            }
////            case QUARTERLY ->
////            {
////                for (int i = 0; i < payment.getFrequency(); i++)
////                    result.add(createPaymentDateByDate(payment.getStartDate().plusMonths(3L * i), payment));
////            }
////            case YEARLY ->
////            {
////                for (int i = 0; i < payment.getFrequency(); i++)
////                    result.add(createPaymentDateByDate(payment.getStartDate().plusYears(i), payment));
////            }
////        }
////    }
//
//
//    @Test
//    void generatePaymentDates_shouldGeneratePaymentDatesAccordingToGivenFrequency()
//    {
////        given
//        var user = this.userService.getUserByEmail("foo");
//
//        var p1 = this.paymentRepo.save(new Payment(LocalDate.now(), PaymentFrequency.ONCE, 1, "foo", 76, user));
//        var p2 = this.paymentRepo.save(new Payment(LocalDate.now(), PaymentFrequency.WEEKLY, 2, "foo", 76, user));
//        var p3 = this.paymentRepo.save(new Payment(LocalDate.now(), PaymentFrequency.MONTHLY, 3, "foo", 76, user));
//        var p4 = this.paymentRepo.save(new Payment(LocalDate.now(), PaymentFrequency.QUARTERLY, 4, "foo", 76, user));
//        var p5 = this.paymentRepo.save(new Payment(LocalDate.now(), PaymentFrequency.YEARLY, 5, "foo", 76, user));
//
////        system under test
//        var toTest = new PaymentService(null, this.paymentDateRepo, null);
//
////        then
//        toTest.generatePaymentPositions(p1);
//        toTest.generatePaymentPositions(p2);
//        toTest.generatePaymentPositions(p3);
//        toTest.generatePaymentPositions(p4);
//        toTest.generatePaymentPositions(p5);
//
////        when
//        assertEquals(1, p1.getDates().size());
//
//        assertEquals(2, p2.getDates().size());
//
//        assertEquals(3, p3.getDates().size());
//
//        assertEquals(4, p4.getDates().size());
//
//        assertEquals(5, p5.getDates().size());
//
//    }
//
//    @Test
//    void createPaymentDateByDate_shouldReturnCreatedPaymentDate()
//    {
////        given
//        var user = this.userService.getUserByEmail("foo");
//
//        var payment = this.paymentRepo.save(new Payment(LocalDate.now().plusMonths(5), PaymentFrequency.ONCE, 1,
//                 "foo", 45, user));
//
////        system under test
//        var toTest = new PaymentService(null, this.paymentDateRepo, null);
//
////        when
//        var result = toTest.createPaymentPositionByDate(payment.getStartDate(), payment);
//
////        then
//        assertNotNull(result);
//        assertThat(result).isInstanceOf(PaymentDate.class);
//        assertEquals(LocalDate.now().plusMonths(5), result.getPaymentDate());
//        assertNotNull(result.getPayment());
//        assertFalse(result.isPaid());
//    }
//
//    @Test
//    void getPaymentsByUserId_shouldReturnAllPaymentsForGivenUser()
//    {
////        given
//        var user = this.userService.getUserByEmail("foo");
//
////        system under test
//        var toTest = new PaymentService(this.paymentRepo, null, null);
//
////        when
//        var result = toTest.getPaymentsByUserId(user.getId());
//
////        then
//        assertTrue(result.size() > 0);
//        assertEquals(11, result.size());
//        assertThat(result.get(0)).isInstanceOf(Payment.class);
//    }
}
