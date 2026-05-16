package com.placement.portal.service;

import com.placement.portal.dto.DashboardStatsDTO;
import com.placement.portal.entity.*;
import com.placement.portal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ApplicationRepository appRepo;
    private final StudentRepository studentRepo;
    private final CompanyRepository companyRepo;
    private final JobDriveRepository driveRepo;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public Application updateApplicationStatus(Long appId, ApplicationStatus status, String remarks) {
        Application app = appRepo.findById(appId)
                .orElseThrow(() -> new NoSuchElementException("Application not found: " + appId));
        app.setStatus(status);
        app.setRemarks(remarks);

        // Auto-mark student placed when SELECTED
        if (status == ApplicationStatus.SELECTED) {
            Student s = app.getStudent();
            s.setPlaced(true);
            studentRepo.save(s);
        }
        return appRepo.save(app);
    }

    public Application uploadOfferLetter(Long appId, MultipartFile file) throws IOException {
        Application app = appRepo.findById(appId)
                .orElseThrow(() -> new NoSuchElementException("Application not found"));
        String filename = "offer_" + appId + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir).resolve(filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        app.setOfferLetterPath(filename);
        return appRepo.save(app);
    }

    public DashboardStatsDTO getDashboardStats() {
        long totalStudents    = studentRepo.count();
        long placedStudents   = studentRepo.countByPlacedTrue();
        long totalCompanies   = companyRepo.count();
        long totalDrives      = driveRepo.count();
        long activeDrives     = driveRepo.countByActiveTrue();
        long totalApps        = appRepo.count();
        long selectedApps     = appRepo.countByStatus(ApplicationStatus.SELECTED);
        long rejectedApps     = appRepo.countByStatus(ApplicationStatus.REJECTED);

        return new DashboardStatsDTO(
            totalStudents, placedStudents, totalCompanies,
            totalDrives, activeDrives, totalApps,
            selectedApps, rejectedApps
        );
    }

    public Page<Application> getAllApplications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return appRepo.findAll(pageable);
    }

    public Page<Student> getAllStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName"));
        return studentRepo.findAll(pageable);
    }

    public Page<JobDrive> getAllDrives(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("driveDate").descending());
        return driveRepo.findAll(pageable);
    }
}