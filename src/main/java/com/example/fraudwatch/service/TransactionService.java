package com.example.fraudwatch.service;

import com.example.fraudwatch.dto.TransactionRequest;
import com.example.fraudwatch.entity.FlaggedTransaction;
import com.example.fraudwatch.entity.Rule;
import com.example.fraudwatch.entity.Transaction;
import com.example.fraudwatch.repository.FlaggedTransactionRepository;
import com.example.fraudwatch.repository.RuleRepository;
import com.example.fraudwatch.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RuleRepository ruleRepository;
    private final FlaggedTransactionRepository flaggedRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            RuleRepository ruleRepository,
            FlaggedTransactionRepository flaggedRepository) {

        this.transactionRepository = transactionRepository;
        this.ruleRepository = ruleRepository;
        this.flaggedRepository = flaggedRepository;
    }

    @Transactional
    public Transaction createTransaction(TransactionRequest request) {

        Transaction transaction = new Transaction();

        transaction.setAmount(request.getAmount());
        transaction.setSender(request.getSender());
        transaction.setReceiver(request.getReceiver());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus(Transaction.TransactionStatus.PENDING);

        transaction = transactionRepository.save(transaction);

        List<Rule> rules = ruleRepository.findByActiveTrue();

        for (Rule rule : rules) {

            boolean triggered = false;
            String reason = "";

            if ("AMOUNT".equalsIgnoreCase(rule.getType())) {

                if (request.getAmount()
                        .compareTo(rule.getAmountThreshold()) > 0) {

                    triggered = true;
                    reason = "Transaction amount exceeded configured threshold";
                }
            }

            if ("VELOCITY".equalsIgnoreCase(rule.getType())) {

                LocalDateTime start =
                        transaction.getTimestamp()
                                .minusMinutes(rule.getTimeWindowMinutes());

                long count =
                        transactionRepository
                                .countBySenderAndTimestampBetween(
                                        request.getSender(),
                                        start,
                                        transaction.getTimestamp()
                                );

                if (count > rule.getTransactionLimit()) {
                    triggered = true;
                    reason = "Too many transactions from same account";
                }
            }

            if (triggered) {

                FlaggedTransaction flagged =
                        new FlaggedTransaction();

                flagged.setTransaction(transaction);
                flagged.setRule(rule);
                flagged.setReason(reason);

                flaggedRepository.save(flagged);
            }
        }

        return transaction;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
