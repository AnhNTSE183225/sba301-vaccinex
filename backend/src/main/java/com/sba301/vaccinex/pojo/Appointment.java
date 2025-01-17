package com.sba301.vaccinex.pojo;

import com.sba301.vaccinex.pojo.enums.PaymentStatus;
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
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    LocalDate bookDate;

    LocalDate startDate;

    String serviceType;

    Double totalPrice;

    @Enumerated(EnumType.STRING)
    Enum<PaymentStatus> status;

    @OneToMany(mappedBy = "appointment")
    List<Payment> payments;

    @OneToMany(mappedBy = "appointment")
    List<VaccineSchedule> schedules;

    @ManyToOne
    Customer customer;
}
