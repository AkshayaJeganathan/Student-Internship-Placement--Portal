package com.placement.portal.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private long totalStudents;
    private long placedStudents;
    private long totalCompanies;
    private long totalDrives;
    private long activeDrives;
    private long totalApplications;
    private long selectedApplications;
    private long rejectedApplications;

    public double getPlacementPercentage() {
        if (totalStudents == 0) return 0;
        return Math.round((placedStudents * 100.0 / totalStudents) * 10.0) / 10.0;
    }
}