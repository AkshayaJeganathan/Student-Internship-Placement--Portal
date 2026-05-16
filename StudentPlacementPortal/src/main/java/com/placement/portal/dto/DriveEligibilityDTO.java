package com.placement.portal.dto;

import com.placement.portal.entity.JobDrive;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriveEligibilityDTO {
    private JobDrive drive;
    private boolean eligible;
    private List<String> ineligibilityReasons;
    private boolean alreadyApplied;
}