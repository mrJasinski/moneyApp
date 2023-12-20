package com.moneyApp.payment.dto;

import java.time.LocalDate;

public class PaymentPositionDTO
{
    private LocalDate paymentDate;
    private boolean isPaid;
    private int hash;

    public PaymentPositionDTO(final LocalDate paymentDate, final boolean isPaid, final int hash)
    {
        this.paymentDate = paymentDate;
        this.isPaid = isPaid;
        this.hash = hash;
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
