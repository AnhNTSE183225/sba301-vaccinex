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
public class VaccineCombo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    Integer quantity;

    @ManyToOne
    Combo combo;

    @ManyToOne
    Vaccine vaccine;
}
