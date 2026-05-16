package com.placement.portal.controller;

import com.placement.portal.dto.ApiResponse;
import com.placement.portal.entity.*;
import com.placement.portal.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // ── Registration ──────────────────────────────────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Company>> register(@Valid @RequestBody Company company) {
        Company saved = companyService.register(company);
        saved.setPassword("[PROTECTED]");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Company registered", saved));
    }

    // ── Profile ───────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Company>> getProfile(@PathVariable Long id) {
        Company c = companyService.getById(id);
        c.setPassword("[PROTECTED]");
        return ResponseEntity.ok(ApiResponse.ok("Profile fetched", c));
    }

    // ── Drive Management ──────────────────────────────────────────────────────

    @PostMapping("/{id}/drives")
    public ResponseEntity<ApiResponse<JobDrive>> postDrive(
            @PathVariable Long id,
            @Valid @RequestBody JobDrive drive) {
        JobDrive saved = companyService.postDrive(id, drive);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Drive posted", saved));
    }

    @GetMapping("/{id}/drives")
    public ResponseEntity<ApiResponse<List<JobDrive>>> myDrives(@PathVariable Long id) {
        List<JobDrive> drives = companyService.getMyDrives(id);
        return ResponseEntity.ok(ApiResponse.ok("Drives fetched", drives));
    }

    @PatchMapping("/{id}/drives/{driveId}/close")
    public ResponseEntity<ApiResponse<Void>> closeDrive(
            @PathVariable Long id,
            @PathVariable Long driveId) {
        companyService.closeDrive(driveId, id);
        return ResponseEntity.ok(ApiResponse.ok("Drive closed", null));
    }

    // ── Applicants ────────────────────────────────────────────────────────────

    @GetMapping("/drives/{driveId}/applicants")
    public ResponseEntity<ApiResponse<Page<Application>>> viewApplicants(
            @PathVariable Long driveId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Application> apps = companyService.getDriveApplicants(driveId, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Applicants fetched", apps));
    }
}