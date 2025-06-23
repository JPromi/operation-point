package com.jpromi.operation_point.model;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class LocationStatisticResponse {
    private String nameId;
    private Long countActive;
    private Long countFire;
    private Long countTechnical;
    private Long countAcid;
    private Long countOther;
}
