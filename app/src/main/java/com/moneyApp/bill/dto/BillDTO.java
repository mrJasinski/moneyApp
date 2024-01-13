package com.moneyApp.bill.dto;

import com.moneyApp.vo.BillSource;

import java.time.LocalDate;
import java.util.List;

public class BillDTO
{
    static public Builder builder()
    {
        return new Builder();
    }

    private Long id;
    private String number;                  // format yearMonthValue_number(counted bills in given month + 1)
    private LocalDate date;
    private String payeeName;
    private String accountName;
    private String description;
    private Double billSum;                 //  positions sum in bill
    List<BillPositionDTO> positions;

    BillDTO(
            final Long id
            , final String number
            , final LocalDate date
            , final String payeeName
            , final String accountName
            , final String description
            , final Double billSum
            , final List<BillPositionDTO> positions)
    {
        this.id = id;
        this.number = number;
        this.date = date;
        this.payeeName = payeeName;
        this.accountName = accountName;
        this.description = description;
        this.billSum = billSum;
        this.positions = positions;
    }

    public BillSource toSource()
    {
        return new BillSource(this.id);
    }

    public Long getId()
    {
        return this.id;
    }

    public String getNumber()
    {
        return this.number;
    }

    public LocalDate getDate()
    {
        return this.date;
    }

    public String getPayeeName()
    {
        return this.payeeName;
    }

    public String getAccountName()
    {
        return this.accountName;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Double getBillSum()
    {
        return this.billSum;
    }

    public List<BillPositionDTO> getPositions()
    {
        return this.positions;
    }

    public static class Builder
    {
        private Long id;
        private String number;                  // format yearMonthValue_number(counted bills in given month + 1)
        private LocalDate date;
        private String payeeName;
        private String accountName;
        private String description;
        private Double billSum;
        List<BillPositionDTO> positions;

        public Builder withId(Long id)
        {
            this.id = id;
            return this;
        }

        public Builder withNumber(String number)
        {
            this.number = number;
            return this;
        }

        public Builder withDate(LocalDate date)
        {
            this.date = date;
            return this;
        }

        public Builder withPayeeName(String payeeName)
        {
            this.payeeName = payeeName;
            return this;
        }

        public Builder withAccountName(String accountName)
        {
            this.accountName = accountName;
            return this;
        }

        public Builder withDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Builder withBillSum(Double billSum)
        {
            this.billSum = billSum;
            return this;
        }

        public Builder withPositions(List<BillPositionDTO> positions)
        {
            this.positions = positions;
            return this;
        }

        public BillDTO build()
        {
            return new BillDTO(this.id, this.number, this.date, this.payeeName, this.accountName, this.description, this.billSum, this.positions);
        }

    }
}
