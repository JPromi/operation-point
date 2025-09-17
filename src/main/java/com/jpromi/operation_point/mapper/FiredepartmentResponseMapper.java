package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.model.FiredepartmentResponse;
import com.jpromi.operation_point.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FiredepartmentResponseMapper {

    @Autowired
    private FileStorageService fileStorageService;

    public FiredepartmentResponse fromFiredepartment(Firedepartment firedepartment) {
        return FiredepartmentResponse.builder()
                .uuid(firedepartment.getUuid())
                .name(firedepartment.getName())
                .atFireDepartmentId(firedepartment.getAtFireDepartmentId())
                .isVolunteer(firedepartment.getIsVolunteer())
                .logo(fileStorageService.getExternalUrl(firedepartment.getLogo()))
                .banner(fileStorageService.getExternalUrl(firedepartment.getBanner()))
                .address(FiredepartmentResponse.FiredepartmentResponseAddress.builder()
                        .street(firedepartment.getAddressStreet())
                        .zipCode(firedepartment.getAddressZipcode())
                        .city(firedepartment.getAddressCity())
                        .federalState(firedepartment.getAddressFederalState())
                        .country(firedepartment.getAddressCountry())
                        .build())
                .contact(FiredepartmentResponse.FiredepartmentResponseContact.builder()
                        .website(firedepartment.getWebsite())
                        .build())
                .build();
    }

}
