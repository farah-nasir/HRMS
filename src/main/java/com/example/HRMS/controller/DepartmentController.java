package com.example.HRMS.controller;

import com.example.HRMS.entity.Department;
import com.example.HRMS.service.DepartmentService;
import com.example.HRMS.repository.DepartmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Department")
public class DepartmentController {

    private final DepartmentRepository departmentRepository;
    private final DepartmentService departmentService;

   // Constructor injection (Spring will auto-inject the repository bean)
    public DepartmentController(DepartmentRepository departmentRepository,
                                DepartmentService departmentService) {
        this.departmentRepository = departmentRepository;
        this.departmentService = departmentService;
    }


    // List all departments
    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "Department/list"; // lowercase folder
    }

    // Show form to add department
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("department", new Department()); // singular entity
        return "Department/form";
    }

      // Handle save
    @PostMapping
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentRepository.save(department);
        return "redirect:/Department"; // after save, redirect to list
    }

    // Show form to edit department
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Department department = departmentService.getDepartmentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid department Id:" + id));
        model.addAttribute("department", department);
        return "Department/form";
    }

    // Delete department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return "redirect:/Department"; // after delete, redirect to list
    }
}

