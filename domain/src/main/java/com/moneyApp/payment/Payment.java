package com.moneyApp.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payments")
public class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    @Enumerated(EnumType.STRING)
    private PaymentFrequency frequencyType;
    private int frequency;
    private String description;
    private double amount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
//    TODO eager testowo
    @JsonIgnore
    @OneToMany(mappedBy = "payment", fetch = FetchType.EAGER)
    private Set<PaymentDate> dates;

//    persistence constructor
    public Payment()
    {
    }

    public Payment(LocalDate startDate, PaymentFrequency frequencyType, int frequency, String description, double amount, User user)
    {
        this.startDate = startDate;
        this.frequencyType = frequencyType;
        this.frequency = frequency;
        this.description = description;
        this.amount = amount;
        this.user = user;
        this.dates = new HashSet<>();
    }

    public Payment(LocalDate startDate, PaymentFrequency frequencyType, int frequency, String description, double amount, User user, Set<PaymentDate> dates)
    {
        this(startDate, frequencyType, frequency, description, amount, user);
        this.dates = dates;
    }

    public PaymentDTO toDto()
    {
        return new PaymentDTO(this.startDate, this.frequencyType, this.frequency, this.description, this.amount, this.dates.stream().toList());
    }

    public Long getId()
    {
        return this.id;
    }

    public LocalDate getStartDate()
    {
        return this.startDate;
    }

    public PaymentFrequency getFrequencyType()
    {
        return this.frequencyType;
    }

    public int getFrequency()
    {
        return this.frequency;
    }

    public String getDescription()
    {
        return this.description;
    }

    public double getAmount()
    {
        return this.amount;
    }

    public User getUser()
    {
        return this.user;
    }

    public Set<PaymentDate> getDates()
    {
        return this.dates;
    }

    public void setDates(Set<PaymentDate> dates)
    {
        this.dates = dates;
    }

//    TODO test

    @Override
    public String toString()
    {
        return "Payment{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", frequencyType=" + frequencyType +
                ", frequency=" + frequency +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", user=" + user +
                ", dates=" + dates +
                '}';
    }
}
