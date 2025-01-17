package com.sba301.vaccinex.pojo;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Integer batchSize;

    Integer quantity;

    @ManyToOne
    Vaccine vaccine;

    @ManyToOne
    Warehouse warehouse;

    @OneToMany(mappedBy = "batch")
    List<BatchTransaction>  batchTransactions;
}
