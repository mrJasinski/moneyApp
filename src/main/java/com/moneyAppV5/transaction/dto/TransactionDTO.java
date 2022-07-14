package com.moneyAppV5.transaction.dto;

import com.moneyAppV5.account.Account;
import com.moneyAppV5.account.dto.AccountDTO;
import com.moneyAppV5.budget.dto.BudgetDTO;
import com.moneyAppV5.budget.dto.BudgetPositionDTO;
import com.moneyAppV5.category.Category;
import com.moneyAppV5.category.dto.CategoryDTO;
import com.moneyAppV5.transaction.Payee;
import com.moneyAppV5.transaction.Transaction;

import java.time.LocalDate;

public class TransactionDTO
{
    private String date;
    private AccountDTO account;
    private double amount;
    private CategoryDTO category;
    private PayeeDTO payee;
    private PayeeDTO gainer;
    private String description;
    private Integer hash;

    private BudgetDTO budget;
    private BudgetPositionDTO budgetPosition;

    private int day;
    private int month;
    private int year;

     public TransactionDTO()
    {
    }

    public TransactionDTO(Transaction transaction)
    {
        this.day = transaction.getBill().getDay();
        this.month = transaction.getBill().getBudget().getMonth();
        this.year = transaction.getBill().getBudget().getYear();
        this.date = createDateString(this.day, this.month, this.year);
//        this.account = transaction.getAccount();
        this.amount = transaction.getAmount();
//        this.category = transaction.getCategory();
//        this.isPaid = transaction.getIsPaid();
        this.gainer = transaction.getGainer().toDto();
        this.description = transaction.getDescription();
        this.hash = transaction.getHash();
    }
//
//
//     public TransactionDTO(String date, Account account, String amount, Category category, Payee isPaid, Payee forWhom, String description)
//    {
//        this.date = date;
//        this.account = account;
//        this.amount = amount;
//        this.category = category;
//        this.payee = payee;
//        this.gainer = gainer;
//        this.description = description;
//    }

    public TransactionDTO(int day, int month, int year, Account account, double amount, Category category, Payee isPaid, Payee forWhom, String description)
    {
        this.day = day;
        this.month = month;
        this.year = year;
        this.date = createDateString(day, month, year);
//        this.account = account;
        this.amount = amount;
        this.category = category.toDto();
//        this.isPaid = isPaid;
//        this.forWhom = forWhom;
        this.description = description;
    }

//    public TransactionDTO(String date, Account account, double amount, Category category, Payee payee, Gainer gainer, String description)
//    {
//        this.date = date;
//        this.account = account;
//        this.amount = amount;
//        this.category = category;
//        this.payee = payee;
//        this.gainer = gainer;
//        this.description = description;
//    }

    public TransactionDTO(LocalDate date, Account account, double amount, Category category, Payee isPaid, Payee forWhom, String description)
    {
        this.date = String.valueOf(date);
//        this.date = date;
//        this.account = account;
        this.amount = amount;
//        this.amount = String.valueOf(amount);
//        this.category = category;
//        this.isPaid = isPaid;
//        this.forWhom = forWhom;
        this.description = description;
    }

     public TransactionDTO(LocalDate date, double amount, String description, int accountId, int categoryId, int payeeId, int gainerId)
    {
        this.date = String.valueOf(date);
        this.amount = amount;
//        this.amount = String.valueOf(amount);
        this.description = description;
//        this.accountId = accountId;
//        this.categoryId = categoryId;
//        this.payeeId = payeeId;
//        this.gainerId = gainerId;
    }

    private TransactionDTO(TransactionDtoBuilder builder)
    {
        this.date = builder.date;
        this.account = builder.account;
        this.amount = builder.amount;
        this.category = builder.category;
        this.payee = builder.payee;
        this.gainer = builder.gainer;
        this.description = builder.description;
        this.hash = builder.hash;

        this.budget = builder.budget;
        this.budgetPosition = builder.budgetPosition;

        this.day = builder.day;
        this.month = builder.month;
        this.year = builder.year;
    }

    public Transaction toTransaction()
    {
        var result = new Transaction();

//        result.setDate(LocalDate.parse(this.date, DateTimeFormatter.ISO_DATE));
//        result.setDay(this.day);
//        result.setMonth(this.month);
//        result.setYear(this.year);
//        result.setAccount(this.account);
//        result.setAmount(this.amount);
//        result.setCategory(this.category);
//        result.setIsPaid(this.isPaid);
//        result.setForWhom(this.forWhom);
        result.setDescription(this.description);
//        result.setBudget(this.budget);
        result.setHash(this.hash);

        return result;
    }

    String createDateString(int day, int month, int year)
    {
        String d = String.valueOf(day);

        if (day < 10)
            d = "0" + d;

        String m = String.valueOf(month);

        if (month < 10)
            m = "0" + m;

        return String.format("%s-%s-%s", year, m, d);
    }

//    public LocalDate getDate()
//    {
//        return date;
//    }
//
//    void setDate(LocalDate date)
//    {
//        this.date = date;
//    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public String getDescription()
    {
        return description;
    }

    void setDescription(String description)
    {
        this.description = description;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public PayeeDTO getPayee() {
        return payee;
    }

    public void setPayee(PayeeDTO payee) {
        this.payee = payee;
    }

    public PayeeDTO getGainer() {
        return gainer;
    }

    public void setGainer(PayeeDTO gainer) {
        this.gainer = gainer;
    }

    public BudgetDTO getBudget() {
        return budget;
    }

    public void setBudget(BudgetDTO budget) {
        this.budget = budget;
    }

    public BudgetPositionDTO getBudgetPosition() {
        return budgetPosition;
    }

    public void setBudgetPosition(BudgetPositionDTO budgetPosition) {
        this.budgetPosition = budgetPosition;
    }

    public static class TransactionDtoBuilder
    {
        private String date;
        private AccountDTO account;
        private double amount;
        private CategoryDTO category;
        private PayeeDTO payee;
        private PayeeDTO gainer;
        private String description;
        private Integer hash;

        private BudgetDTO budget;
        private BudgetPositionDTO budgetPosition;

        private int day;
        private int month;
        private int year;
        
        public TransactionDtoBuilder buildDay(int day)
        {
            this.day = day;
            
            return this;
        }

        public TransactionDtoBuilder buildMonth(int month)
        {
            this.month = month;

            return this;
        }

        public TransactionDtoBuilder buildYear(int year)
        {
            this.year = year;

            return this;
        }

        public TransactionDtoBuilder buildDate()
        {
            this.date = String.format("%s/%s/%s", this.day, this.month, this.year);

            return this;
        }

        public TransactionDtoBuilder buildDate(int day, int month, int year)
        {
            this.day = day;
            this.month = month;
            this.year = year;

            this.date = String.format("%s/%s/%s", day, month, year);

            return this;
        }

        public TransactionDtoBuilder buildAccount(AccountDTO account)
        {
            this.account = account;

            return this;
        }

        public TransactionDtoBuilder buildAmount(double amount)
        {
            this.amount = amount;
            
            return this;
        }
        
        public TransactionDtoBuilder buildCategory(CategoryDTO category)
        {
            this.category = category;

            return this;
        }

        public TransactionDtoBuilder buildPayee(PayeeDTO payee)
        {
            this.payee = payee;

            return this;
        }

        public TransactionDtoBuilder buildGainer(PayeeDTO gainer)
        {
            this.gainer = gainer;

            return this;
        }

        public TransactionDtoBuilder buildDescription(String description)
        {
            this.description = description;

            return this;
        }

        public TransactionDtoBuilder buildHash(int hash)
        {
            this.hash = hash;

            return this;
        }

        public TransactionDtoBuilder buildBudget(BudgetDTO budget)
        {
            this.budget = budget;

            return this;
        }

        public TransactionDtoBuilder buildBudgetPosition(BudgetPositionDTO budgetPosition)
        {
            this.budgetPosition = budgetPosition;

            return this;
        }

        public TransactionDTO build()
        {
            return new TransactionDTO(this);
        }
    }
}
