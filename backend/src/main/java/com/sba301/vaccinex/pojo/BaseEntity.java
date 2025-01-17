package com.sba301.vaccinex.pojo;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;

@MappedSuperclass
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    @Builder.Default
    boolean deleted = false;

}
