package com.placement.portal.controller;

import com.placement.portal.dto.*;
import com.placement.portal.entity.*;
import com.placement.portal.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ── Dashboard Stats ───────────────────────────────────────────────────────

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getStats() {
        return ResponseEntity.ok(ApiResponse.ok("Stats fetched", adminService.getDashboardStats()));
    }

    // ── Application Management ────────────────────────────────────────────────

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<Page<Application>>> getAllApplications(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
            ApiResponse.ok("Applications fetched", adminService.getAllApplications(page, size))
        );
    }

    @PatchMapping("/applications/{appId}/status")
    public ResponseEntity<ApiResponse<Application>> updateStatus(
            @PathVariable Long appId,
            @RequestParam ApplicationStatus status,
            @RequestParam(required = false) String remarks) {
        Application app = adminService.updateApplicationStatus(appId, status, remarks);
        return ResponseEntity.ok(ApiResponse.ok("Status updated to " + status, app));
    }

    @PostMapping("/applications/{appId}/offer-letter")
    public ResponseEntity<ApiResponse<Application>> uploadOfferLetter(
            @PathVariable Long appId,
            @RequestParam("file") MultipartFile file) throws IOException {
        Application app = adminService.uploadOfferLetter(appId, file);
        return ResponseEntity.ok(ApiResponse.ok("Offer letter uploaded", app));
    }

    // ── Student & Drive Listings ──────────────────────────────────────────────

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<Page<Student>>> getAllStudents(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Student> students = adminService.getAllStudents(page, size);
        students.forEach(s -> s.setPassword("[PROTECTED]"));
        return ResponseEntity.ok(ApiResponse.ok("Students fetched", students));
    }

    @GetMapping("/drives")
    public ResponseEntity<ApiResponse<Page<JobDrive>>> getAllDrives(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
            ApiResponse.ok("Drives fetched", adminService.getAllDrives(page, size))
        );
    }
}