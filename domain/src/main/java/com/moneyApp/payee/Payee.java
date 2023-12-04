package com.moneyApp.payee;

import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.BillSource;
import com.moneyApp.vo.UserSource;

import java.util.Set;

class Payee
{
//    TODO bills i billPositions zakomentowane do testu

    static Payee restore(PayeeSnapshot snapshot)
    {
        return new Payee(
                snapshot.getId()
                , snapshot.getName()
                , snapshot.getRole()
//                , snapshot.getBills()
//                , snapshot.getBillPositions()
                , snapshot.getUser()
        );
    }

    private final Long id;
    private final String name;
    private final PayeeRole role;
//    private final Set<BillSource> bills;
//    private final Set<BillPositionSource> billPositions;
    private final UserSource user;

    private Payee(
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
//        this.bills = bills;
//        this.billPositions = billPositions;
        this.user = user;
    }

    PayeeSnapshot getSnapshot()
    {
        return new PayeeSnapshot(
                this.id
                , this.name
                , this.role
//                , this.bills
//                ,this.billPositions
                , this.user
        );
    }
}