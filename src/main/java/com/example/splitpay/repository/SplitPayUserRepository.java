package com.example.splitpay.repository;

import com.example.splitpay.entity.SplitPayUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitPayUserRepository extends JpaRepository<SplitPayUser, Integer> {

}
