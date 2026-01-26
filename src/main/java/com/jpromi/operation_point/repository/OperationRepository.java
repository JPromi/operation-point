package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enums.ServiceOriginEnum;
import com.jpromi.operation_point.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<Operation> findByBlId(String blId);
    Optional<Operation> findByUaId(String uaId);
    Optional<Operation> findByStHash(String stHash);
    Optional<Operation> findByTyEventId(String tyEventId);
    Optional<Operation> findByLaSysId(String laSysId);

    List<Operation> findByServiceOriginAndLastSeenNull(ServiceOriginEnum serviceOrigin);

    List<Operation> findByEndTimeNullOrderByStartTime();
    List<Operation> findByEndTimeNullAndFederalStateOrderByStartTime(String federalState);
    List<Operation> findByEndTimeNullAndFederalStateAndDistrictIgnoreCaseOrderByStartTime(String federalState, String district);

    Long countByEndTimeNullOrderByStartTime();
    Long countByEndTimeNullAndFederalStateOrderByStartTime(String federalState);

    Optional<Operation> findByUuid(UUID uuid);

    @Query("""
        SELECT o FROM Operation o
        JOIN o.firedepartments fdState
        JOIN fdState.firedepartment fd
        WHERE o.endTime IS NULL
        AND fd.uuid = :firedepartmentUuid
    """)
    List<Operation> findActiveOperationsByFiredepartment(UUID firedepartmentUuid);

    @Query("""
      SELECT DISTINCT o
      FROM Operation o
      JOIN o.firedepartments fdState
      JOIN fdState.firedepartment fd
      WHERE fd.uuid = :firedepartmentUuid
        AND o.startTime >= COALESCE(:from, o.startTime)
        AND o.startTime <= COALESCE(:to,   o.startTime)
      ORDER BY o.startTime DESC
    """)
    Page<Operation> findByFiredepartmentFiltered(
            @Param("firedepartmentUuid") UUID firedepartmentUuid,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable
    );
}
