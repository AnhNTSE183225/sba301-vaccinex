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
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    Integer dose;

    @OneToMany(mappedBy = "vaccine")
    List<VaccineScheduleDetail> scheduleDetails;

    @OneToMany(mappedBy = "vaccine")
    List<VaccineUse> vaccineUses;

    @OneToMany(mappedBy = "vaccine")
    List<VaccineCombo> vaccineCombos;

    @OneToMany(mappedBy = "vaccine")
    List<Batch> batches;
}
