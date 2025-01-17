package com.sba301.vaccinex.pojo;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @ManyToOne
    Batch batch;

    @ManyToOne
    Transaction transaction;
}
