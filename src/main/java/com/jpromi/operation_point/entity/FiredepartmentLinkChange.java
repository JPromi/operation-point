package com.jpromi.operation_point.entity;

import com.jpromi.operation_point.helper.SocialHelper;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class FiredepartmentLinkChange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firedepartment_change_id", nullable = false)
    private FiredepartmentChange firedepartmentChange;

    private String name; // e.g., "feuerwehr_perchtoldsdorf"
    private String url;  // e.g., "https://www.facebook.com/yourpage"
    private String type; // e.g., "website", "facebook", "twitter"

    public String getLink() {
        return SocialHelper.GetUrlFromSocialLinks(url, type, name);
    }
}
