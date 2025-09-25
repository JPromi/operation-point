package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enitiy.AppUser;
import com.jpromi.operation_point.enitiy.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByFileNameAndUuid(String fileName, UUID uuid);
}
