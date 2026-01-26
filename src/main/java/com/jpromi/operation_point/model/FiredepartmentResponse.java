package com.jpromi.operation_point.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiredepartmentResponse {
    private UUID uuid;
    private String nameId;
    private String name;
    private String atFireDepartmentId;
    private Boolean isVolunteer;
    private FiredepartmentResponseAddress address;
    private List<FiredepartmentResponseLinks> links;
    private String logo;
    private String banner;

    @Data
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FiredepartmentResponseAddress {
        private String street;
        private String zipCode;
        private String city;
        private String federalState;
        private String country;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FiredepartmentResponseLinks {
        private String name;
        private String url;
        private String type;
    }

}
