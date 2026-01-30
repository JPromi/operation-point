package com.jpromi.operation_point.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class OperationFiredepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Firedepartment firedepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Operation operation;

    private OffsetDateTime dispoTime;
    private OffsetDateTime outTime;
    private OffsetDateTime inTime;
    private OffsetDateTime alarmTime;
}
