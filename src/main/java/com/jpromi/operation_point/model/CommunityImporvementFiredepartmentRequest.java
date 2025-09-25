package com.jpromi.operation_point.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
public class CommunityImporvementFiredepartmentRequest {
    private UUID firedepartmentUuid;
    private String nameId;
    private String name;
    private Boolean isVolunteer;
    private String atFireDepartmentId;
    private CummunityImporvementFiredepartmentRequestAddress address;
    private List<CummunityImporvementFiredepartmentRequestLink> links;
    private MultipartFile logo;
    private MultipartFile banner;
    private String changedEmail;

    @Data
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CummunityImporvementFiredepartmentRequestAddress {
        private String street;
        private String city;
        private String zipCode;
        private String federalState;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CummunityImporvementFiredepartmentRequestLink {
        private String name;
        private String url;
        private String type;
    }
}
