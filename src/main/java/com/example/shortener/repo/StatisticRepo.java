package com.example.shortener.repo;

import com.example.shortener.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepo extends JpaRepository<Statistic, Integer> {
}
