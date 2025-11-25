package com.jpromi.operation_point.entity;

import com.jpromi.operation_point.enums.ServiceOriginEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String city;
    private String zipCode;
    private String district;
    private String alarmType;
    private Long alarmLevel;
    private String alarmLevelAddition;
    private String alarmText;
    private String uaAlarmTypeId; // only Upper Austria
    private String uaAlarmTypeType; // only Upper Austria
    private String tyAlarmOrganization; // only Tyrol
    private String tyAlarmOutOrder; // only Tyrol
    private String tyAlarmCategory; // only Tyrol
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String location;
    private Double lat;
    private Double lng;
    @Builder.Default
    private String country = "AT";
    private String federalState;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationFiredepartment> firedepartments;

    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationUnit> units;

    @Builder.Default
    private ServiceOriginEnum serviceOrigin = ServiceOriginEnum.UNKNOWN;

    @Builder.Default
    private OffsetDateTime firstSeen = OffsetDateTime.now();
    private OffsetDateTime lastSeen;

    private String uaId; // only Upper Austria
    private String tyEventId; // only Tyrol
    private String blId; // only Burgenland
    private String stHash; // only Styria
    private String stInstanceId; // only Styria
    private String laWastlPubId; // only Lower Austria (M2......)
    private String laSysId; // only Lower Austria (ELKOS 00000)
    private String laId; // only Lower Austria (-201...)

    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    // list of: feurwehr, fahrzeuge

}
