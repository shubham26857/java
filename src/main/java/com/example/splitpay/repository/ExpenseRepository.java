package com.example.splitpay.repository;

import com.example.splitpay.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer > {
}

