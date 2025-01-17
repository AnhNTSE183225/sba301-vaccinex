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
public class VaccineSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalDate date;

    String feedback;

    @ManyToOne
    Employee employee;

    @ManyToOne
    Child child;

    @ManyToOne
    Customer customer;

    @ManyToOne
    Appointment appointment;

    @OneToMany(mappedBy = "schedule")
    List<Notification> notifications;

    @OneToMany(mappedBy = "schedule")
    List<SideEffect> sideEffects;

    @OneToMany(mappedBy = "schedule")
    List<VaccineScheduleDetail> scheduleDetails;
}
