package com.jpromi.operation_point.enitiy;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class FiredepartmentLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firedepartment_id", nullable = false)
    private Firedepartment firedepartment;

    private String name; // e.g., "feuerwehr_perchtoldsdorf"
    private String url;  // e.g., "https://www.facebook.com/yourpage"
    private String type; // e.g., "website", "facebook", "twitter"
}
