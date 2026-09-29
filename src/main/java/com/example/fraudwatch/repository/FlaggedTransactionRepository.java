package com.example.fraudwatch.repository;

import com.example.fraudwatch.entity.FlaggedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlaggedTransactionRepository
        extends JpaRepository<FlaggedTransaction, Long> {

    List<FlaggedTransaction> findAllByOrderByIdDesc();
}
