package com.example.fraudwatch.repository;

import com.example.fraudwatch.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RuleRepository extends JpaRepository<Rule, Long> {

    List<Rule> findByActiveTrue();
}
