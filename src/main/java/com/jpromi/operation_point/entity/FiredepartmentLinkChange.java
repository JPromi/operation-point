package com.jpromi.operation_point.entity;

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
        if (url != null) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return "https://" + url;
            } else {
                return url;
            }
        } else {
            switch (type) {
                case "facebook":
                    return "https://www.facebook.com/" + name;
                case "instagram":
                    return "https://www.instagram.com/" + name;
                case "youtube":
                    return "https://www.youtube.com/@" + name;
                case "x":
                    return "https://www.x.com/" + name;
                case "tiktok":
                    return "https://www.tiktok.com/@" + name;
                case "flickr":
                    return "https://www.flickr.com/people/" + name;
                default:
                    return url;
            }
        }
    }
}
