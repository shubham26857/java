package com.example.splitpay.repository;

import com.example.splitpay.entity.SplitPayGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  SplitPayGroupRepository extends JpaRepository<SplitPayGroup, Integer> {
}
