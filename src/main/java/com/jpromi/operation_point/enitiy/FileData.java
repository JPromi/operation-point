package com.jpromi.operation_point.enitiy;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String fileName;
    private String contentType;
    private Long fileSize;
}
