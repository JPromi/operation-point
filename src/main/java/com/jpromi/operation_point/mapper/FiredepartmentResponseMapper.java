package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.model.FiredepartmentResponse;
import org.springframework.stereotype.Component;

@Component
public class FiredepartmentResponseMapper {

    public FiredepartmentResponse fromFiredepartment(Firedepartment firedepartment) {
        return FiredepartmentResponse.builder()
                .uuid(firedepartment.getUuid())
                .name(firedepartment.getName())
                .atFireDepartmentId(firedepartment.getAtFireDepartmentId())
                .isVolunteer(firedepartment.getIsVolunteer())
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
