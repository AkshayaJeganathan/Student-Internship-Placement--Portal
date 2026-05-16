package com.placement.portal.service;

import com.placement.portal.dto.*;
import com.placement.portal.entity.*;
import com.placement.portal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepo;
    private final JobDriveRepository driveRepo;
    private final ApplicationRepository appRepo;
    private final EligibilityService eligibilityService;

    public Student register(Student student) {
        if (studentRepo.existsByEmail(student.getEmail()))
            throw new IllegalArgumentException("Email already registered");
        student.setRole(Role.STUDENT);
        return studentRepo.save(student);
    }

    public Student getById(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + id));
    }

    public Student getByEmail(String email) {
        return studentRepo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Student not found: " + email));
    }

    public Student updateProfile(Long id, Student updated) {
        Student s = getById(id);
        s.setFullName(updated.getFullName());
        s.setBranch(updated.getBranch());
        s.setCgpa(updated.getCgpa());
        s.setBacklogs(updated.getBacklogs());
        s.setPhone(updated.getPhone());
        s.setLinkedIn(updated.getLinkedIn());
        s.setSkills(updated.getSkills());
        return studentRepo.save(s);
    }

    public String uploadResume(Long studentId, MultipartFile file, String uploadDir) throws IOException {
        String filename = "resume_" + studentId + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir).resolve(filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        Student s = getById(studentId);
        s.setResumePath(filename);
        studentRepo.save(s);
        return filename;
    }

    /**
     * Returns all active drives with per-drive eligibility result.
     */
    public Page<DriveEligibilityDTO> getAllDrivesWithEligibility(Long studentId, int page, int size) {
        Student student = getById(studentId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("driveDate").descending());
        Page<JobDrive> drives = driveRepo.findByActiveTrue(pageable);

        List<DriveEligibilityDTO> dtos = drives.getContent().stream().map(drive -> {
            EligibilityService.EligibilityResult result = eligibilityService.check(student, drive);
            boolean applied = appRepo.existsByStudentIdAndJobDriveId(studentId, drive.getId());
            return new DriveEligibilityDTO(drive, result.eligible(), result.reasons(), applied);
        }).toList();

        return new PageImpl<>(dtos, pageable, drives.getTotalElements());
    }

    public Application applyToDrive(Long studentId, Long driveId) {
        Student student = getById(studentId);
        JobDrive drive = driveRepo.findById(driveId)
                .orElseThrow(() -> new NoSuchElementException("Drive not found"));

        if (!eligibilityService.isEligible(student, drive))
            throw new IllegalStateException("Student is not eligible for this drive");

        if (appRepo.existsByStudentIdAndJobDriveId(studentId, driveId))
            throw new IllegalStateException("Already applied to this drive");

        Application app = Application.builder()
                .student(student)
                .jobDrive(drive)
                .status(ApplicationStatus.APPLIED)
                .build();
        return appRepo.save(app);
    }

    public List<Application> getMyApplications(Long studentId) {
        return appRepo.findByStudentId(studentId);
    }
}