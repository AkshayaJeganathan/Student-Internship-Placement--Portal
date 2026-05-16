package com.placement.portal.repository;

import com.placement.portal.entity.JobDrive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobDriveRepository extends JpaRepository<JobDrive, Long> {

    Page<JobDrive> findByActiveTrue(Pageable pageable);

    List<JobDrive> findByCompanyIdAndActiveTrue(Long companyId);

    @Query("SELECT j FROM JobDrive j WHERE j.active = true " +
           "AND j.minCgpa <= :cgpa " +
           "AND j.maxBacklogs >= :backlogs " +
           "AND (j.allowedBranches = 'ALL' OR j.allowedBranches LIKE %:branch%)")
    Page<JobDrive> findEligibleDrives(@Param("cgpa") Double cgpa,
                                      @Param("backlogs") Integer backlogs,
                                      @Param("branch") String branch,
                                      Pageable pageable);

    long countByActiveTrue();
}