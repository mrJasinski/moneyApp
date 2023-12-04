package com.moneyApp.payee;

import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.BillSource;
import com.moneyApp.vo.UserSource;

import java.util.HashSet;
import java.util.Set;

class PayeeSnapshot
{
    private Long id;
    private String name;
    private PayeeRole role;
//    private Set<BillSource> bills = new HashSet<>();
//    private Set<BillPositionSource> billPositions = new HashSet<>();
    private UserSource user;

    //    persistence constructor
    protected PayeeSnapshot()
    {
    }

    PayeeSnapshot(
            final Long id
            , final String name
            , final PayeeRole role
//            , final Set<BillSource> bills
//            , final Set<BillPositionSource> billPositions
            , final UserSource user)
    {
        this.id = id;
        this.name = name;
        this.role = role;
//        this.bills.addAll(bills);
//        this.billPositions.addAll(billPositions);
        this.user = user;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public PayeeRole getRole()
    {
        return this.role;
    }

//    public Set<BillSource> getBills()
//    {
//        return this.bills;
//    }

//    public Set<BillPositionSource> getBillPositions()
//    {
//        return this.billPositions;
//    }

    public UserSource getUser()
    {
        return this.user;
    }
}
