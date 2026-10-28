package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.entity.AppUser;
import com.jpromi.operation_point.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
}
