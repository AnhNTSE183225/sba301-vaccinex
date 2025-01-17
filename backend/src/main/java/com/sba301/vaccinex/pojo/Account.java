package com.sba301.vaccinex.pojo;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String firstName;

    String lastName;

    String email;

    String password;

    String phoneNumber;

    String address;

    LocalDate dob;

    Integer age;

    @Builder.Default
    boolean nonLocked = true;

    @Builder.Default
    boolean enabled = true;

    @OneToOne(mappedBy = "account")
    Customer customer;

    @OneToOne(mappedBy = "account")
    Employee employee;

    @ManyToOne
    Role role;
}
