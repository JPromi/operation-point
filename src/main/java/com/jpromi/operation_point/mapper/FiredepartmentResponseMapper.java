package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.FiredepartmentLink;
import com.jpromi.operation_point.model.FiredepartmentResponse;
import com.jpromi.operation_point.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
                .links(mapLinks(firedepartment.getLinks()))
                .build();
    }

    private List<FiredepartmentResponse.FiredepartmentResponseLinks> mapLinks(List<FiredepartmentLink> links) {
        List<FiredepartmentResponse.FiredepartmentResponseLinks> linksMapped = new ArrayList<>();

        if (links == null) {
            return linksMapped;
        }

        for (FiredepartmentLink link : links) {
            linksMapped.add(FiredepartmentResponse.FiredepartmentResponseLinks.builder()
                    .name(link.getName())
                    .type(link.getType())
                    .url(link.getUrl())
                    .build());
        }

        return linksMapped;
    }

}
