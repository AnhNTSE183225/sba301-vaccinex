package com.sba301.vaccinex.pojo;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue
    Integer id;

    String employeeCode;

    @OneToOne
    Account account;

    @OneToMany(mappedBy = "employee")
    List<VaccineSchedule> schedules;
}
