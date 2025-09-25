package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.enitiy.FileData;
import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.FiredepartmentChange;
import com.jpromi.operation_point.enitiy.FiredepartmentLinkChange;
import com.jpromi.operation_point.model.CommunityImporvementFiredepartmentRequest;
import org.springframework.stereotype.Component;

@Component
public class FiredepartmentChangeMapper {
    public FiredepartmentChange fromCommunityImporvementFiredepartmentRequest(
            CommunityImporvementFiredepartmentRequest ciFd,
            FileData logo,
            FileData banner,
            String ip,
            String userAgent
    ) {
        FiredepartmentChange firedepartment = FiredepartmentChange.builder()
                .firedepartmentUuid(ciFd.getFiredepartmentUuid())
                .nameId(ciFd.getNameId())
                .friendlyName(ciFd.getName())
                .atFireDepartmentId(ciFd.getAtFireDepartmentId())
                .isVolunteer(ciFd.getIsVolunteer())
                .addressStreet(ciFd.getAddress().getStreet())
                .addressCity(ciFd.getAddress().getCity())
                .addressZipcode(ciFd.getAddress().getZipCode())
                .addressFederalState(ciFd.getAddress().getFederalState())
                .logo(logo)
                .banner(banner)
                .ip(ip)
                .userAgent(userAgent)
                .build();

        if (ciFd.getLinks() != null) {
            firedepartment.setLinks(ciFd.getLinks().stream().map(link -> FiredepartmentLinkChange.builder()
                    .name(link.getName())
                    .url(link.getUrl())
                    .type(link.getType())
                    .firedepartmentChange(firedepartment)
                    .build()).toList());
        }

        return firedepartment;
    }
}
