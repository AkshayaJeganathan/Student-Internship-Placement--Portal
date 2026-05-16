package com.placement.portal.controller;

import com.placement.portal.dto.*;
import com.placement.portal.entity.*;
import com.placement.portal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Value("${app.upload.dir}")
    private String uploadDir;

    // ── Registration ──────────────────────────────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Student>> register(@Valid @RequestBody Student student) {
        Student saved = studentService.register(student);
        saved.setPassword("[PROTECTED]");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Registration successful", saved));
    }

    // ── Profile ───────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> getProfile(@PathVariable Long id) {
        Student s = studentService.getById(id);
        s.setPassword("[PROTECTED]");
        return ResponseEntity.ok(ApiResponse.ok("Profile fetched", s));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Student>> updateProfile(
            @PathVariable Long id,
            @RequestBody Student updated) { // <-- Removed @Valid here to bypass empty password check
        Student s = studentService.updateProfile(id, updated);
        s.setPassword("[PROTECTED]");
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", s));
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<ApiResponse<String>> uploadResume(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        String filename = studentService.uploadResume(id, file, uploadDir);
        return ResponseEntity.ok(ApiResponse.ok("Resume uploaded", filename));
    }

    // ── Drives with Eligibility ───────────────────────────────────────────────

    @GetMapping("/{id}/drives")
    public ResponseEntity<ApiResponse<Page<DriveEligibilityDTO>>> viewDrives(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DriveEligibilityDTO> result = studentService.getAllDrivesWithEligibility(id, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Drives fetched", result));
    }

    // ── Apply ─────────────────────────────────────────────────────────────────

    @PostMapping("/{id}/apply/{driveId}")
    public ResponseEntity<ApiResponse<Application>> apply(
            @PathVariable Long id,
            @PathVariable Long driveId) {
        Application app = studentService.applyToDrive(id, driveId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Applied successfully", app));
    }

    // ── Track Applications ────────────────────────────────────────────────────

    @GetMapping("/{id}/applications")
    public ResponseEntity<ApiResponse<List<Application>>> myApplications(@PathVariable Long id) {
        List<Application> apps = studentService.getMyApplications(id);
        return ResponseEntity.ok(ApiResponse.ok("Applications fetched", apps));
    }
}