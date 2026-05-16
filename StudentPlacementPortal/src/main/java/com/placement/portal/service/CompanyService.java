package com.placement.portal.service;

import com.placement.portal.entity.*;
import com.placement.portal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepo;
    private final JobDriveRepository driveRepo;
    private final ApplicationRepository appRepo;

    public Company register(Company company) {
        if (companyRepo.existsByEmail(company.getEmail()))
            throw new IllegalArgumentException("Email already registered");
        company.setRole(Role.COMPANY);
        return companyRepo.save(company);
    }

    public Company getById(Long id) {
        return companyRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Company not found: " + id));
    }

    public Company getByEmail(String email) {
        return companyRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Company not found: " + email));
    }

    public JobDrive postDrive(Long companyId, JobDrive drive) {
        Company company = getById(companyId);
        drive.setCompany(company);
        drive.setActive(true);
        return driveRepo.save(drive);
    }

    public List<JobDrive> getMyDrives(Long companyId) {
        return driveRepo.findByCompanyIdAndActiveTrue(companyId);
    }

    public Page<Application> getDriveApplicants(Long driveId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return appRepo.findByJobDriveId(driveId, pageable);
    }

    public void closeDrive(Long driveId, Long companyId) {
        JobDrive drive = driveRepo.findById(driveId)
                .orElseThrow(() -> new NoSuchElementException("Drive not found"));
        if (!drive.getCompany().getId().equals(companyId))
            throw new IllegalStateException("Unauthorized");
        drive.setActive(false);
        driveRepo.save(drive);
    }
}