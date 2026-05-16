package com.placement.portal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "job_drives")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDrive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotBlank
    private String jobTitle;

    @NotBlank
    private String jobType;         // Internship / Full-Time / Both

    @DecimalMin("0.0")
    private Double ctc;             // LPA

    @DecimalMin("0.0")
    private Double minCgpa;

    @Min(0)
    private Integer maxBacklogs;

    @Column(length = 500)
    private String allowedBranches; // comma-separated: CSE,ECE,IT

    private String jobDescription;
    private String location;

    @Column(nullable = false)
    private LocalDate driveDate;

    @Column(nullable = false)
    private LocalDate lastDateToApply;

    private boolean active = true;
}