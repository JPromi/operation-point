package com.jpromi.operation_point.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appVersion;
    private String platform;
    private String userAgent;
    private String path;
    private String requestOrigin;
    private String requestReferer;
    private String objectType; // vehicle, firedepartment, operation,...
    private UUID objectUuid;

    @Builder.Default
    private OffsetDateTime timestamp = OffsetDateTime.now();
}
