package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.OperationPointApplication;
import com.jpromi.operation_point.model.SystemInformationResponse;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@RestController("SystemController")
@RequestMapping("/system")
public class SystemController {

    private final BuildProperties build;

    public SystemController(BuildProperties build) {
        this.build = build;
    }

    @GetMapping("/version")
    public ResponseEntity<SystemInformationResponse> version() {
        SystemInformationResponse informations = SystemInformationResponse.builder()
                .name(build.getName())
                .version(build.getVersion())
                .packageId(build.getGroup() + "." + build.getArtifact())
                .buildDate(build.getTime())
                .build();

        return ResponseEntity.ok(informations);
    }
}
