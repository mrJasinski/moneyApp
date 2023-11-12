package com.moneyApp.bill;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.vo.SimpleAccount;
import com.moneyApp.vo.SimpleBudgetPosition;
import com.moneyApp.budget.Budget;
import com.moneyApp.category.Category;
import com.moneyApp.payee.Payee;
import com.moneyApp.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "bills")
class Bill
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long number;  // numer kolejny w danym miesiącu
    private LocalDate billDate;
    @ManyToOne
    @JoinColumn(name = "payee_id")
    private Payee payee;
//    @ManyToOne
//    @JoinColumn(name = "account_id")
    private SimpleAccount account;
    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "bill")
    private Set<Position> billPositions;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    persistence constructor
    public Bill()
    {
    }

    public Bill(Long number, LocalDate billDate, Payee payee, SimpleAccount account, Budget budget, String description, User user)
    {
        this.number = number;
        this.billDate = billDate;
        this.payee = payee;
        this.account = account;
        this.budget = budget;
        this.description = description;
        this.user = user;
    }

    public Bill(Long id, Long number, LocalDate billDate, Payee payee, SimpleAccount account, Budget budget, String description, User user)
    {
        this(number, billDate, payee, account, budget, description, user);
        this.id = id;
    }

    public BillDTO toDto()
    {
        return BillDTO.builder()
                .withNumber(String.valueOf(this.number))
                .withDate(this.billDate)
                .withPayeeName(this.payee.getName())
                .withDescription(this.description)
                .withPositions(this.billPositions
                        .stream()
                        .map(Bill.Position::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Long getId()
    {
        return this.id;
    }

    public Long getNumber()
    {
        return this.number;
    }

    public LocalDate getBillDate()
    {
        return this.billDate;
    }

    public Payee getPayee()
    {
        return this.payee;
    }

    public SimpleAccount getAccount()
    {
        return this.account;
    }

    public Budget getBudget()
    {
        return this.budget;
    }

    public String getDescription()
    {
        return this.description;
    }

    public User getUser()
    {
        return this.user;
    }

    @Entity
    @Table(name = "bill_positions")
    static class Position
    {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private long number;  //TODO numer kolejny w danym rachunku
        private Double amount;
        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category category;
        @ManyToOne
        @JoinColumn(name = "gainer_id")
        private Payee gainer;
        private String description;
        @ManyToOne
        @JoinColumn(name = "bill_id")
        private Bill bill;
//        @ManyToOne
//        @JoinColumn(name = "position_id")
        private SimpleBudgetPosition budgetPosition;
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        //    persistence constructor
        protected Position()
        {
        }

        Position(long number, Double amount, Category category, Payee gainer, String description, Bill bill, User user)
        {
            this.number = number;
            this.amount = amount;
            this.category = category;
            this.gainer = gainer;
            this.description = description;
            this.bill = bill;
            this.user = user;
        }

        BillPositionDTO toSimpleDto()
        {
            return BillPositionDTO.builder()
                    .withAmount(this.amount)
                    .withCategoryName(this.category.getCategoryName())
                    .withGainerName(this.gainer.getName())
                    .withDescription(this.description)
                    .build();
        }

        BillPositionDTO toDto()
        {
            return BillPositionDTO.builder()
                    .withDate(this.bill.getBillDate())
                    .withPayeeName(this.bill.getPayee().getName())
                    .withAccountName(this.bill.getAccount().getName())
                    .withAmount(this.amount)
                    .withCategoryName(this.category.getCategoryName())
                    .withGainerName(this.gainer.getName())
                    .withDescription(this.description)
                    .build();
        }

        Long getId()
        {
            return this.id;
        }

        long getNumber()
        {
            return this.number;
        }

        Double getAmount()
        {
            return this.amount;
        }

        Category getCategory()
        {
            return this.category;
        }

        Payee getGainer()
        {
            return this.gainer;
        }

        String getDescription()
        {
            return this.description;
        }

        Bill getBill()
        {
            return this.bill;
        }

        SimpleBudgetPosition getBudgetPosition()
        {
            return this.budgetPosition;
        }

        void setBudgetPosition(SimpleBudgetPosition budgetPosition)
        {
            this.budgetPosition = budgetPosition;
        }

        User getUser()
        {
            return this.user;
        }
    }
}
