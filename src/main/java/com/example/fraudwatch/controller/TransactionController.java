package com.example.fraudwatch.controller;

import com.example.fraudwatch.dto.TransactionRequest;
import com.example.fraudwatch.entity.Transaction;
import com.example.fraudwatch.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(
            @Valid @RequestBody TransactionRequest request) {

        return service.createTransaction(request);
    }

    @GetMapping
    public List<Transaction> getAll() {
        return service.getAllTransactions();
    }
}
