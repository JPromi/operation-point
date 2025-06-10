package com.jpromi.operation_point.model;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnitLightResponse {
    private UUID uuid;
    private String name;
}
