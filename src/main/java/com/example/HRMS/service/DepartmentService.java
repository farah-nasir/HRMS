package com.example.HRMS.service;
import com.example.HRMS.entity.Department;
import com.example.HRMS.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
         Department dept = departmentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        dept.setStatus("Inactive");
        departmentRepository.save(dept);
    }

    public List<Department> getActiveDepartments() {
        return departmentRepository.findByStatus("Active");
    }

}
