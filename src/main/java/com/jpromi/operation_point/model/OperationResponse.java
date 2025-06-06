package com.jpromi.operation_point.model;

import com.jpromi.operation_point.enums.ServiceOriginEnum;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class OperationResponse {
    private UUID uuid;
    @Builder.Default
    private OperationResponseExternalIds externalIds = new OperationResponseExternalIds();
    @Builder.Default
    private OperationResponseAlarm alarm = new OperationResponseAlarm();
    @Builder.Default
    private OperationResponseAddress address = new OperationResponseAddress();
    @Builder.Default
    private List<OperationResponseFiredepartment> firedepartments = new ArrayList<>();
    @Builder.Default
    private List<OperationResponseUnit> units = new ArrayList<>();
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    @Builder.Default
    private OperationResponseSystem system = new OperationResponseSystem();

    @Data
    public static class OperationResponseExternalIds {
        private String upperAustriaId;
        private String tyrolEventId;
        private String burgenlandId;
        private String styriaId;
        private String lowerAustriaWastlPubId;
        private String lowerAustriaSysId;
        private String lowerAustriaId;
    }

    @Data
    public static class OperationResponseAlarm {
        private String type;
        private Long level;
        private String message;
        private String upperAustriaId;
        private String upperAustriaType;
        private String tyrolOrganization;
        private String tyrolOutOrder;
        private String tyrolCategory;
    }

    @Data
    public static class OperationResponseAddress {
        private String country;
        private String federalState;
        private String city;
        private String zipCode;
        private String district;
        private String location;
    }

    @Data
    public static class OperationResponseSystem {
        private ServiceOriginEnum serviceOrigin;
        private OffsetDateTime firstSeen;
        private OffsetDateTime lastSeen;
        private OffsetDateTime lastUpdate;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationResponseUnit {
        private UnitLightResponse unit;
        private OffsetDateTime dispoTime;
        private OffsetDateTime outTime;
        private OffsetDateTime inTime;
        private OffsetDateTime alarmTime;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperationResponseFiredepartment {
        private FiredepartmentLightResponse firedepartment;
        private OffsetDateTime dispoTime;
        private OffsetDateTime outTime;
        private OffsetDateTime inTime;
        private OffsetDateTime alarmTime;
    }
}
