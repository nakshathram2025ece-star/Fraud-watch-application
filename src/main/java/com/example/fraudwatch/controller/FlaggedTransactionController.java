package com.example.fraudwatch.controller;

import com.example.fraudwatch.entity.FlaggedTransaction;
import com.example.fraudwatch.entity.Transaction;
import com.example.fraudwatch.repository.FlaggedTransactionRepository;
import com.example.fraudwatch.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flagged")
public class FlaggedTransactionController {

    private final FlaggedTransactionRepository repository;
    private final TransactionRepository transactionRepository;

    public FlaggedTransactionController(
            FlaggedTransactionRepository repository,
            TransactionRepository transactionRepository) {

        this.repository = repository;
        this.transactionRepository = transactionRepository;
    }

    // Get all flagged transactions
    @GetMapping
    public List<FlaggedTransaction> getFlaggedTransactions() {
        return repository.findAllByOrderByIdDesc();
    }

    // Dashboard - flagged transactions grouped by rule
    @GetMapping("/dashboard")
    public Map<String, Long> getDashboard() {

        List<FlaggedTransaction> flaggedTransactions =
                repository.findAll();

        return flaggedTransactions.stream()
                .collect(Collectors.groupingBy(
                        flagged -> flagged.getRule().getName(),
                        Collectors.counting()
                ));
    }

    // Approve flagged transaction
    @PutMapping("/{id}/approve")
    public FlaggedTransaction approveTransaction(@PathVariable Long id) {

        FlaggedTransaction flagged =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Flagged transaction not found"));

        Transaction transaction = flagged.getTransaction();

        transaction.setStatus(Transaction.TransactionStatus.APPROVED);

        transactionRepository.save(transaction);

        return flagged;
    }

    // Block flagged transaction
    @PutMapping("/{id}/block")
    public FlaggedTransaction blockTransaction(@PathVariable Long id) {

        FlaggedTransaction flagged =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Flagged transaction not found"));

        Transaction transaction = flagged.getTransaction();

        transaction.setStatus(Transaction.TransactionStatus.BLOCKED);

        transactionRepository.save(transaction);

        return flagged;
    }
}