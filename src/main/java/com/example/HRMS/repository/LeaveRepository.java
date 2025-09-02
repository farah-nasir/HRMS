package com.example.HRMS.repository;

import com.example.HRMS.entity.Leave;
import com.example.HRMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    // Page<Leave> findByEmployee(User employee, Pageable pageable);
    Page<Leave> findByEmployeeId(Long employeeId, Pageable pageable);
    Page<Leave> findByDepartmentId(Long departmentId, Pageable pageable);
}
