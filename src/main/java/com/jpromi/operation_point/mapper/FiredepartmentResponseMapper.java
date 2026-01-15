package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.entity.Firedepartment;
import com.jpromi.operation_point.entity.FiredepartmentChange;
import com.jpromi.operation_point.entity.FiredepartmentLink;
import com.jpromi.operation_point.entity.FiredepartmentLinkChange;
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
                .name(firedepartment.getFriendlyName())
                .nameId(firedepartment.getNameId())
                .atFireDepartmentId(firedepartment.getAtFireDepartmentId())
                .isVolunteer(firedepartment.getIsVolunteer())
                .logo(fileStorageService.getExternalUrl(firedepartment.getLogo(), "/static/images/korpsabzeichen.svg"))
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

    public FiredepartmentResponse fromFiredepartmentChange(FiredepartmentChange firedepartmentChange) {
        return FiredepartmentResponse.builder()
                .uuid(firedepartmentChange.getFiredepartmentUuid())
                .name(firedepartmentChange.getFriendlyName())
                .atFireDepartmentId(firedepartmentChange.getAtFireDepartmentId())
                .isVolunteer(firedepartmentChange.getIsVolunteer())
                .logo(fileStorageService.getExternalUrl(firedepartmentChange.getLogo(), "/static/images/korpsabzeichen.svg"))
                .banner(fileStorageService.getExternalUrl(firedepartmentChange.getBanner()))
                .address(FiredepartmentResponse.FiredepartmentResponseAddress.builder()
                        .street(firedepartmentChange.getAddressStreet())
                        .zipCode(firedepartmentChange.getAddressZipcode())
                        .city(firedepartmentChange.getAddressCity())
                        .federalState(firedepartmentChange.getAddressFederalState())
                        .country(null)
                        .build())
                .links(mapLinksChange(firedepartmentChange.getLinks()))
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
                    .url(link.getLink())
                    .build());
        }

        return linksMapped;
    }

    private List<FiredepartmentResponse.FiredepartmentResponseLinks> mapLinksChange(List<FiredepartmentLinkChange> links) {
        List<FiredepartmentResponse.FiredepartmentResponseLinks> linksMapped = new ArrayList<>();

        if (links == null) {
            return linksMapped;
        }

        for (FiredepartmentLinkChange link : links) {
            linksMapped.add(FiredepartmentResponse.FiredepartmentResponseLinks.builder()
                    .name(link.getName())
                    .type(link.getType())
                    .url(link.getLink())
                    .build());
        }

        return linksMapped;
    }

}
