package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.entity.Unit;
import com.jpromi.operation_point.model.UnitResponse;
import org.springframework.stereotype.Component;

@Component
public class UnitResponseMapper {

    public UnitResponse fromUnit(Unit unit) {
        return UnitResponse.builder()
                .uuid(unit.getUuid())
                .name(unit.getName())
                .friendlyName(unit.getFriendlyName())
                .build();
    }

}
