package com.example.fraudwatch.controller;

import com.example.fraudwatch.entity.ReviewOutcome;
import com.example.fraudwatch.entity.Transaction;
import com.example.fraudwatch.repository.ReviewOutcomeRepository;
import com.example.fraudwatch.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final TransactionRepository transactionRepository;
    private final ReviewOutcomeRepository reviewRepository;

    public ReviewController(
            TransactionRepository transactionRepository,
            ReviewOutcomeRepository reviewRepository) {

        this.transactionRepository = transactionRepository;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/{transactionId}")
    public ReviewOutcome review(
            @PathVariable Long transactionId,
            @RequestParam String outcome,
            @RequestParam(required = false) String comments) {

        Transaction transaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() ->
                                new RuntimeException("Transaction not found"));

        if (!outcome.equalsIgnoreCase("APPROVED")
                && !outcome.equalsIgnoreCase("BLOCKED")) {

            throw new IllegalArgumentException(
                    "Outcome must be APPROVED or BLOCKED");
        }

        transaction.setStatus(
                outcome.equalsIgnoreCase("APPROVED")
                        ? Transaction.TransactionStatus.APPROVED
                        : Transaction.TransactionStatus.BLOCKED
        );

        transactionRepository.save(transaction);

        ReviewOutcome review = new ReviewOutcome();

        review.setTransaction(transaction);
        review.setOutcome(outcome.toUpperCase());
        review.setComments(comments);
        review.setReviewedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }
}
