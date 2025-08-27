package com.example.HRMS.controller;

import com.example.HRMS.entity.User;
import com.example.HRMS.service.UserService;
import com.example.HRMS.service.RoleService; // if you need roles in form
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/User")
public class UserController {

    private final UserService userService;
    private final RoleService roleService; 
    private final PasswordEncoder passwordEncoder; // inject encoder

    
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // List all users
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "User/list"; // maps to templates/user/list.html
    }

    // Show form to add user
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles()); // for dropdown
        return "User/form";
    }

    // Save user
   @PostMapping
    public String saveUser(@ModelAttribute User user) {
        Optional<User> existingUser = userService.getUserById(user.getId());
        if (existingUser.isPresent()) {
            User dbUser = existingUser.get();
            
            // Only update password if a new one is provided
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                dbUser.setPassword(passwordEncoder.encode(user.getPassword())); // encode if using Spring Security
            }
            
            dbUser.setUsername(user.getUsername());
            dbUser.setRole(user.getRole());
            userService.saveUser(dbUser);
        } else {
            // new user, encode password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
        }

        return "redirect:/User";
    }


    // Show form to edit user
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "User/form";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/User";
    }
}