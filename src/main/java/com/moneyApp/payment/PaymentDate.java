package com.moneyApp.payment;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "payment_dates")
public class PaymentDate
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate paymentDate;
    private boolean isPaid;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
    private int hash;

    public PaymentDate()
    {
    }

    public PaymentDate(LocalDate paymentDate, Payment payment)
    {
        this.paymentDate = paymentDate;
//        domyślnie ustawiane na false ponieważ z założenia jest to do opłacenia w przyłości
        this.isPaid = false;
        this.payment = payment;
        this.hash = hashCode();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.paymentDate, this.payment);
    }

    public Long getId()
    {
        return this.id;
    }

    public LocalDate getPaymentDate()
    {
        return this.paymentDate;
    }

    public boolean isPaid()
    {
        return this.isPaid;
    }

    public Payment getPayment()
    {
        return this.payment;
    }

    public int getHash()
    {
        return this.hash;
    }

    //    TODO test

    @Override
    public String toString()
    {
        return "PaymentDate{" +
                "id=" + id +
                ", date=" + paymentDate +
                ", isPaid=" + isPaid +
                '}';
    }
}
