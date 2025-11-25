package com.jpromi.operation_point.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FiredepartmentChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String nameId;
    private String name;
    private String friendlyName;
    private String atFireDepartmentId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isVolunteer = true;

    private String addressCity;
    private String addressStreet;
    private String addressZipcode;
    private String addressCountry;
    private String addressFederalState;
    @OneToMany(mappedBy = "firedepartmentChange", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FiredepartmentLinkChange> links;

    @OneToOne(fetch = FetchType.LAZY)
    private FileData logo;

    @OneToOne(fetch = FetchType.LAZY)
    private FileData banner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Unit> units;

    private Long firedepartmentId;
    private UUID firedepartmentUuid;

    @Builder.Default
    private LocalDateTime changeDate = LocalDateTime.now();
    @Column(nullable = false)
    private String changeType; // e.g., "Added", "Updated"
    @Column(nullable = true)
    private String changedEmail;
    @Column(nullable = true)
    private String ip;
    @Column(nullable = true)
    private String userAgent;
    private LocalDateTime processedDate;
    @Builder.Default
    private Boolean isProcessed = false;
    @Builder.Default
    private Boolean isVerified = false;

}