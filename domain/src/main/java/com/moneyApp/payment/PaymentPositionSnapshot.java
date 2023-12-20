package com.moneyApp.payment;

import java.time.LocalDate;
import java.util.Objects;

class PaymentPositionSnapshot
{
    private Long id;
    private LocalDate paymentDate;
    private boolean isPaid;
    private int hash;
    private PaymentSnapshot payment;

    //    persistence constructor
    protected PaymentPositionSnapshot()
    {
    }

    PaymentPositionSnapshot(final Long id, final LocalDate paymentDate, final PaymentSnapshot payment)
    {
        this.id = id;
        this.paymentDate = paymentDate;
//        newly created payment is assumed to be not paid before
        this.isPaid = false;
        this.hash = hashCode();
        this.payment = payment;
    }

    public PaymentPositionSnapshot(final Long id, final LocalDate paymentDate, final boolean isPaid, final int hash)
    {
        this.id = id;
        this.paymentDate = paymentDate;
        this.isPaid = isPaid;
        this.hash = hash;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id, this.paymentDate, this.isPaid, this.payment);
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

    public int getHash()
    {
        return this.hash;
    }

    public PaymentSnapshot getPayment()
    {
        return this.payment;
    }
}
