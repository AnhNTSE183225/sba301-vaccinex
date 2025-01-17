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
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    String description;

    Double price;

    Integer minAge;

    Integer maxAge;

    @OneToMany(mappedBy = "combo")
    List<VaccineScheduleDetail> scheduleDetails;

    @OneToMany(mappedBy = "combo")
    List<VaccineCombo> vaccineCombos;
}
