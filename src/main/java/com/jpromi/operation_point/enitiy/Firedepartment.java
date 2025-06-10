package com.jpromi.operation_point.enitiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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
    private String website;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Unit> units;
}
