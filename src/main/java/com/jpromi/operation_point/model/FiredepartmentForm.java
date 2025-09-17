package com.jpromi.operation_point.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
public class FiredepartmentForm {
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
}
