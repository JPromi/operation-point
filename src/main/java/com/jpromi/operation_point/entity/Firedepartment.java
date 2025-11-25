package com.jpromi.operation_point.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
public class Firedepartment {

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
    @OneToMany(mappedBy = "firedepartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FiredepartmentLink> links;

    @OneToOne(fetch = FetchType.LAZY)
    private FileData logo;

    @OneToOne(fetch = FetchType.LAZY)
    private FileData banner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Unit> units;

    @Builder.Default
    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isHidden = false;
}
