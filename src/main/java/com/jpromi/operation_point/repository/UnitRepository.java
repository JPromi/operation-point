package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enitiy.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String Name);
    Optional<Unit> findByUuid(UUID uuid);
    List<Unit> findByFriendlyNameContainingIgnoreCase(String name);
}
