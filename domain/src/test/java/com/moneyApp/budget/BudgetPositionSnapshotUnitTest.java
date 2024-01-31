package com.moneyApp.budget;

import com.moneyApp.vo.BillPositionSource;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BudgetPositionSnapshotUnitTest
{
    @Test
    void BudgetPositionSnapshot_shouldInitializeBillPositionsAsEmptySetIfNullIsPassed()
    {
//        given + when
        var position = new BudgetPositionSnapshot(null, null, null, null, null, null);

//        then
        assertNotNull(position.getBillPositions());
        assertTrue(position.getBillPositions().isEmpty());
    }

//    BudgetPositionSnapshot(
//            final Long id
//            , final CategorySource category
//            , final Double plannedAmount
//            , final String description
//            , final BudgetSnapshot budget
//            , final Set<BillPositionSource> billPositions)
//    {
//        this.id = id;
//        this.category = category;
//        this.plannedAmount = plannedAmount;
//        this.description = description;
//        this.budget = budget;
//        addBillPositionSources(billPositions);
//    }

    @Test
    void addBillPositionSources_shouldAddGivenBillPositionsSetToBudgetPositionBillPositionsSet()
    {
//        given
        var sources = Set.of(new BillPositionSource(2L), new BillPositionSource(3L));
        var position = new BudgetPositionSnapshot();
        var billPositionsInitSize = position.getBillPositions().size();

//        when
        position.addBillPositionSources(sources);

//        then
        assertEquals(billPositionsInitSize + sources.size(), position.getBillPositions().size());
    }

    @Test
    void addBillPositionSources_shouldNotAddToBudgetPositionBillPositionsSet()
    {
//        given
        Set<BillPositionSource> sources = null;
        var position = new BudgetPositionSnapshot();
        var billPositionsInitSize = position.getBillPositions().size();

//        when
        position.addBillPositionSources(sources);

//        then
        assertEquals(billPositionsInitSize, position.getBillPositions().size());
    }

//    void addBillPositionSources(Set<BillPositionSource> sources)
//    {
//        if (this.billPositions == null)
//            this.billPositions = new HashSet<>();
//
//        this.billPositions.addAll(sources);
//    }

    @Test
    void addBillPositionSources_shouldAddGivenBillPositionSourceToBudgetPositionBillPositionsSet()
    {
//        given
        var source = new BillPositionSource(1L);
        var position = new BudgetPositionSnapshot();
        var billPositionsInitSize = position.getBillPositions().size();

//        when
        position.addBillPositionSource(source);

//        then
        assertEquals(billPositionsInitSize + 1, position.getBillPositions().size());
    }
}