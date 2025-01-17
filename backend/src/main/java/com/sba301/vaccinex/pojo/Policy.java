package com.sba301.vaccinex.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String key;

    String value;

    String description;

    @Builder.Default
    boolean enabled = true;
}
