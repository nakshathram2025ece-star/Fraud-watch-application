package com.example.fraudwatch.repository;

import com.example.fraudwatch.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    long countBySenderAndTimestampBetween(
            String sender,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Transaction> findByStatus(
            Transaction.TransactionStatus status
    );
}
