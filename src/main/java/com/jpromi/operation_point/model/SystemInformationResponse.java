package com.jpromi.operation_point.model;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemInformationResponse {
    private String name;
    private String version;
    private String packageId;
    private Instant buildDate;
}
