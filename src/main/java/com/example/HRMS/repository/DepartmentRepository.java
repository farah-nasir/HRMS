package com.example.HRMS.repository;
import com.example.HRMS.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByStatus(String status);
    Page<Department> findByStatus(String status, Pageable pageable);
}

