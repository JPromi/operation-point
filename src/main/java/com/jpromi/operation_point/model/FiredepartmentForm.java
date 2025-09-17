package com.jpromi.operation_point.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class FiredepartmentForm {
    private String nameId;
    private String friendlyName;
    private String atFireDepartmentId;
    private String addressCity;
    private String addressStreet;
    private String addressZipcode;
    private String addressFederalState;
    private String addressCountry;
    private String website;
    private Boolean isVolunteer = false;
    private Boolean isWrongAssignment = false;
    private MultipartFile logo;
    private MultipartFile banner;
    private Boolean logoDelete = false;
    private Boolean bannerDelete = false;
    private List<FiredepartmentFormLinks> links =  new ArrayList<>();

    @Data
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FiredepartmentFormLinks {
        private Long id;
        private String name;
        private String url;
        private String type;
    }
}
