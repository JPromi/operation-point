package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enums.ServiceOriginEnum;
import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.model.LocationStatisticResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<Operation> findByBlId(String blId);
    Optional<Operation> findByUaId(String uaId);
    Optional<Operation> findByStId(String stId);
    Optional<Operation> findByTyEventId(String tyEventId);
    Optional<Operation> findByLaSysId(String laSysId);

    List<Operation> findByServiceOriginAndLastSeenNull(ServiceOriginEnum serviceOrigin);

    List<Operation> findByEndTimeNullOrderByStartTime();
    List<Operation> findByEndTimeNullAndFederalStateOrderByStartTime(String federalState);
    List<Operation> findByEndTimeNullAndFederalStateAndDistrictIgnoreCaseOrderByStartTime(String federalState, String district);

    Long countByEndTimeNullOrderByStartTime();
    Long countByEndTimeNullAndFederalStateOrderByStartTime(String federalState);

    Optional<Operation> findByUuid(UUID uuid);
}
