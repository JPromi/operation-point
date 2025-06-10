package com.jpromi.operation_point.enitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class OperationUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Operation operation;


    private OffsetDateTime dispoTime;
    private OffsetDateTime outTime;
    private OffsetDateTime inTime;
    private OffsetDateTime alarmTime;
}
