package com.example.fraudwatch.repository;

import com.example.fraudwatch.entity.ReviewOutcome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewOutcomeRepository
        extends JpaRepository<ReviewOutcome, Long> {
}
