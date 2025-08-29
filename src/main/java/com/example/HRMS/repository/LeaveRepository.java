package com.example.HRMS.repository;

import com.example.HRMS.entity.Leave;
import com.example.HRMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    // Find leaves by employee ID
    List<Leave> findByEmployeeId(Long employeeId);
    // Find leaves by manager ID
    List<Leave> findByManagerId(Long managerId);
    List<Leave> findByEmployee(User employee);
    List<Leave> findByManager(User manager);
}
