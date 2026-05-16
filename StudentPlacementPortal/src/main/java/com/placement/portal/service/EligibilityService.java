package com.placement.portal.service;

import com.placement.portal.entity.JobDrive;
import com.placement.portal.entity.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * EligibilityService — Smart Eligibility Engine
 * Checks whether a student is eligible for a given JobDrive
 * and returns detailed reasons when not eligible.
 */
@Service
@Slf4j
public class EligibilityService {

    /**
     * Full eligibility result with pass/fail + reasons.
     */
    public record EligibilityResult(boolean eligible, List<String> reasons) {
        public static EligibilityResult pass() {
            return new EligibilityResult(true, List.of());
        }
        public static EligibilityResult fail(List<String> reasons) {
            return new EligibilityResult(false, reasons);
        }
    }

    /**
     * Core check: validates CGPA, branch, and backlogs against drive criteria.
     *
     * @param student  the applicant
     * @param drive    the job drive
     * @return EligibilityResult with eligible flag and reason list
     */
    public EligibilityResult check(Student student, JobDrive drive) {
        List<String> failReasons = new ArrayList<>();

        // 1. CGPA check
        if (student.getCgpa() == null || student.getCgpa() < drive.getMinCgpa()) {
            failReasons.add(String.format(
                "CGPA %.2f is below required minimum %.2f",
                student.getCgpa() == null ? 0.0 : student.getCgpa(),
                drive.getMinCgpa()
            ));
        }

        // 2. Backlog check
        if (student.getBacklogs() == null || student.getBacklogs() > drive.getMaxBacklogs()) {
            failReasons.add(String.format(
                "Active backlogs %d exceed allowed maximum %d",
                student.getBacklogs() == null ? -1 : student.getBacklogs(),
                drive.getMaxBacklogs()
            ));
        }

        // 3. Branch check  ("ALL" means open to every branch)
        if (drive.getAllowedBranches() != null
                && !drive.getAllowedBranches().equalsIgnoreCase("ALL")) {
            List<String> allowed = Arrays.stream(drive.getAllowedBranches().split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .toList();
            if (student.getBranch() == null
                    || !allowed.contains(student.getBranch().toUpperCase())) {
                failReasons.add(String.format(
                    "Branch '%s' is not in the allowed list: %s",
                    student.getBranch(), drive.getAllowedBranches()
                ));
            }
        }

        if (failReasons.isEmpty()) {
            log.debug("Student {} is ELIGIBLE for drive {}", student.getId(), drive.getId());
            return EligibilityResult.pass();
        }

        log.debug("Student {} NOT eligible for drive {}. Reasons: {}",
                  student.getId(), drive.getId(), failReasons);
        return EligibilityResult.fail(failReasons);
    }

    /**
     * Convenience boolean check (used in filtering).
     */
    public boolean isEligible(Student student, JobDrive drive) {
        return check(student, drive).eligible();
    }
}