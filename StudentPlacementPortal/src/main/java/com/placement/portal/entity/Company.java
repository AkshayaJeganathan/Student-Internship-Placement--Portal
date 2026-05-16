package com.placement.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String companyName;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String password;

    private String industry;       // IT, Finance, Core, Consulting
    private String website;
    private String description;
    private String hrName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role = Role.COMPANY;
}