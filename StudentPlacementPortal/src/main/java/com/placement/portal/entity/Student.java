package com.placement.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String branch;          // CSE, ECE, MECH, CIVIL, IT, EEE

    @DecimalMin("0.0") @DecimalMax("10.0")
    @Column(nullable = false)
    private Double cgpa;

    @Min(0)
    @Column(nullable = false)
    private Integer backlogs;

    private String phone;
    private String resumePath;      // uploaded file path
    private String linkedIn;
    private String skills;          // comma-separated

    @Column(nullable = false)
    private boolean placed = false;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;
}