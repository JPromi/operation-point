package com.jpromi.operation_point.model;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceStatusResponse {
    private String serviceName;
    private String name;
    private Boolean isEnabled;
}
