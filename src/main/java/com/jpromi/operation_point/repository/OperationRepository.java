package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.ServiceOriginEnum;
import com.jpromi.operation_point.enitiy.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    Optional<Operation> findByBlId(String blId);

    List<Operation> findByServiceOriginAndLastSeenNull(ServiceOriginEnum serviceOrigin);
}
