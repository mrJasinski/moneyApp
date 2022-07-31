package com.moneyAppV5.budget.repository;

import com.moneyAppV5.budget.Budget;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository
{
    List<Budget> findAll();

    Optional<Budget> findById(Integer id);
    Optional<Budget> findByMonthAndYear(Integer month, Integer year);
    boolean existsById(int id);

    Budget save(Budget entity);

    boolean existsByMonthAndYear(int month, int year);

//    Budget findByMonthAndYear(int month, int year);

    Optional<Budget> findByHash(Integer hash);

    Optional<Integer> findNewestBudgetHash();

    Optional<Integer> readBudgetIdByBudgetHash(Integer hash);

    List<Budget> findBudgetsByYear(Integer year);
}
