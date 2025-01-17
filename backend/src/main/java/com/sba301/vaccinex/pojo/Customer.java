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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne
    Account account;

    @OneToMany(mappedBy = "customer")
    List<Child> children;

    @OneToMany(mappedBy = "customer")
    List<Payment> payments;

    @OneToMany(mappedBy = "customer")
    List<Appointment> appointments;

    @OneToMany(mappedBy = "customer")
    List<VaccineSchedule> schedules;
}
