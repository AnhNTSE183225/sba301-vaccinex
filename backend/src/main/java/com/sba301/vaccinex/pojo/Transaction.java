package com.sba301.vaccinex.pojo;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Integer quantityTaken;

    LocalDate exportDate;

    @OneToMany(mappedBy = "transaction")
    List<BatchTransaction> batchTransactions;
}
