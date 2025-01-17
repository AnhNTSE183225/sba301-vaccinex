package com.sba301.vaccinex.pojo;

import com.sba301.vaccinex.pojo.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Child extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String firstName;

    String lastName;

    LocalDate dob;

    Double weight;

    Double height;

    String bloodType;

    String healthNote;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @ManyToOne
    Customer customer;

    @OneToMany(mappedBy = "child")
    List<VaccineSchedule> schedules;
}
