package com.placement.portal.repository;

import com.placement.portal.entity.Application;
import com.placement.portal.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStudentId(Long studentId);

    Page<Application> findByJobDriveId(Long driveId, Pageable pageable);

    Optional<Application> findByStudentIdAndJobDriveId(Long studentId, Long driveId);

    boolean existsByStudentIdAndJobDriveId(Long studentId, Long driveId);

    long countByStatus(ApplicationStatus status);

    List<Application> findByStudentIdAndStatus(Long studentId, ApplicationStatus status);
}