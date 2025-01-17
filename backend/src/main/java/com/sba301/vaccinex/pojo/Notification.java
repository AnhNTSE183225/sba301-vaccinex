package com.sba301.vaccinex.pojo;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String message;

    LocalDate date;

    @ManyToOne
    VaccineSchedule schedule;
}
