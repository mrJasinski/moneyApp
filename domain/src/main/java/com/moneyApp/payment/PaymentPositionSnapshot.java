package com.moneyApp.payment;

import java.time.LocalDate;

class PaymentPositionSnapshot
{
    private Long id;
    private LocalDate paymentDate;
    private boolean isPaid;
    private int hash;

    //    persistence constructor
    protected PaymentPositionSnapshot()
    {
    }

    PaymentPositionSnapshot(final Long id, final LocalDate paymentDate, final boolean isPaid, final int hash)
    {
        this.id = id;
        this.paymentDate = paymentDate;
        this.isPaid = isPaid;
        this.hash = hash;
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
}
