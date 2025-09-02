package com.example.HRMS.controller;

import com.example.HRMS.entity.Department;
import com.example.HRMS.service.DepartmentService;
import com.example.HRMS.repository.DepartmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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
    public String listDepartments(Model model, 
            @AuthenticationPrincipal User userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size ) {
        model.addAttribute("username", userDetails.getUsername());
        Page<Department> departmentPage = departmentService.getPaginatedActiveDepartments(page, size);
        model.addAttribute("departments", departmentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", departmentPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalItems", departmentPage.getTotalElements()); 

        return "Department/list";
    }

    // Show form to add department
    @GetMapping("/new")
    public String showCreateForm(
            Model model, 
            @AuthenticationPrincipal User userDetails) { // Inject currently logged-in user
        model.addAttribute("department", new Department());
        model.addAttribute("username", userDetails.getUsername()); // Now this works
        return "Department/form";
    }

    // Handle save
    @PostMapping
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentRepository.save(department);
        return "redirect:/Department";
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
        return "redirect:/Department"; 
    }

}

