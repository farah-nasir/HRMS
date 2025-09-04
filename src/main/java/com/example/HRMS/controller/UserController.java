package com.example.HRMS.controller;

import com.example.HRMS.entity.Department;
import com.example.HRMS.entity.User;
import com.example.HRMS.service.UserService;
import com.example.HRMS.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.HRMS.repository.UserRepository;
import com.example.HRMS.service.DepartmentService;
import com.example.HRMS.service.UserService;
import com.example.HRMS.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;

@Controller
@RequestMapping("/User")
public class UserController {

    private final UserService userService;
    private final RoleService roleService; 
    private final PasswordEncoder passwordEncoder; // inject encoder
    private final DepartmentService departmentService;

    public UserController(UserService userService,
                        RoleService roleService,
                        DepartmentService departmentService,
                        PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
    }

    // List all users
    @GetMapping
    public String listUsers(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir) {

        Page<User> userPage = userService.getPaginatedUsers(page, size, sortBy, sortDir);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("totalItems", userPage.getTotalElements()); 

        // Sorting info
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "User/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles()); 
        model.addAttribute("departments", departmentService.getActiveDepartments());
        model.addAttribute("managers", userService.getManagers());
        model.addAttribute("username", userDetails.getUsername());
        return "User/form";
    }

    // Save user
   @PostMapping
    public String saveUser(@ModelAttribute User user, Model model) {
        System.out.println("---- Saving User ----");
        System.out.println("ID: " + user.getId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("FullName: " + user.getFullName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role ID: " + (user.getRole() != null ? user.getRole().getId() : null));
        System.out.println("Department ID: " + (user.getDepartment() != null ? user.getDepartment().getId() : null));
        System.out.println("-------------------");

        // Ensure role and department exist
        Role role = roleService.getRoleById(user.getRole().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Role ID"));
        Department dept = departmentService.getDepartmentById(user.getDepartment().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Department ID"));

        // 🔍 Check for duplicate username (only when creating a new user)
        if (user.getId() == null && userService.isUsernameTaken(user.getUsername())) {
            model.addAttribute("error", "Username already taken!");
            model.addAttribute("user", user);

            // repopulate dropdowns so Thymeleaf form still works
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("departments", departmentService.getActiveDepartments());
            model.addAttribute("managers", userService.getManagers());

            return "User/form"; // stay on form page
        }

        if (user.getId() != null) {
            // Editing existing user
            User existingUser = userService.getUserById(user.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            existingUser.setUsername(user.getUsername());
            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(role);
            existingUser.setDepartment(dept);

            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userService.saveUser(existingUser);
        } else {
            // New user
            user.setRole(role);
            user.setDepartment(dept);
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userService.saveUser(user);
        }

        System.out.println("User saved successfully!");
        return "redirect:/User";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Ensure department and manager are not null to avoid Thymeleaf errors
        if (user.getDepartment() == null) user.setDepartment(new Department());
 
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("departments", departmentService.getActiveDepartments());
        model.addAttribute("managers", userService.getManagers());

        return "User/form";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/User";
    }
    
}