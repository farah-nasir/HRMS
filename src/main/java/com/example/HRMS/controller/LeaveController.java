package com.example.HRMS.controller;

import com.example.HRMS.entity.Leave;
import com.example.HRMS.entity.User;
import com.example.HRMS.repository.UserRepository;
import com.example.HRMS.service.LeaveService;
import com.example.HRMS.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/Leave")
public class LeaveController {

    private final LeaveService leaveService;
    private final UserService userService;
    private final UserRepository userRepository;

    public LeaveController(LeaveService leaveService, UserService userService, UserRepository userRepository) {
        this.leaveService = leaveService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // List leave applications
    @GetMapping
    public String listLeaves(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        User currentUser = userService.getCurrentUser();
        Page<Leave> leavePage;

        // Check role
        Long roleId = currentUser.getRole().getId();
        if (roleId == 1L) { // Admin
            leavePage = leaveService.getAllLeaves(page, size);
        } else { // Manager or Employee
            leavePage = leaveService.getLeavesForCurrentUser(currentUser, page, size);
        }

        model.addAttribute("leaveApplications", leavePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", leavePage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("roleId", roleId);
        model.addAttribute("totalItems", leavePage.getTotalElements());

        return "Leave/list";
    }

    // Show leave application form
    @GetMapping("/new")
    public String showLeaveForm(Model model, Principal principal) {
        Leave leave = new Leave();
        User currentUser = userService.getUserByUsername(principal.getName()).orElseThrow();
        leave.setEmployee(currentUser);
        leave.setManager(null); // manager will be set when submitting

        model.addAttribute("leave", leave);
        return "Leave/form";
    }

    // Submit leave application
    @PostMapping
    public String submitLeave(@ModelAttribute Leave leave, Principal principal, Model model) {
         System.out.println("SubmitLeave method called!");
        try {
            // Get current user
            User currentUser = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            System.out.println("Current User: " + currentUser.getUsername() + ", ID: " + currentUser.getId());

            // Check department
            if (currentUser.getDepartment() == null) {
                System.out.println("Department is null for current user!");
                model.addAttribute("error", "Your account has no department assigned.");
                return "Leave/form";
            }
            System.out.println("Department: " + currentUser.getDepartment().getName() + ", ID: " + currentUser.getDepartment().getId());

            // Find manager by role_id = 2 in current user's department
            User manager = userRepository.findByDepartment_IdAndRole_Id(currentUser.getDepartment().getId(), 2L)
                    .orElseThrow(() -> new IllegalArgumentException("Manager not found for your department"));
            System.out.println("Manager found: " + manager.getUsername() + ", ID: " + manager.getId());

            // Set leave fields
            leave.setStatus("PENDING");
            leave.setEmployee(currentUser);
            leave.setManager(manager);
            leave.setDepartment(currentUser.getDepartment());

            // Debug leave object
            System.out.println("Leave object to save:");
            System.out.println("  Employee ID: " + leave.getEmployee().getId());
            System.out.println("  Manager ID: " + leave.getManager().getId());
            System.out.println("  Department ID: " + leave.getDepartment().getId());
            System.out.println("  Start Date: " + leave.getStartDate());
            System.out.println("  End Date: " + leave.getEndDate());
            System.out.println("  Reason: " + leave.getReason());

            // Save leave
            leaveService.saveLeave(leave);
            System.out.println("Leave saved successfully!");

            return "redirect:/Leave";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while submitting the leave: " + e.getMessage());
            return "Leave/form";
        }
    }

    // Approve leave
    @GetMapping("/approve/{id}")
    public String approveLeave(@PathVariable Long id) {
        leaveService.updateStatus(id, "APPROVED");
        return "redirect:/Leave";
    }

    // Reject leave
    @PostMapping("/reject/{id}")
    public String rejectLeave(@PathVariable Long id,
                              @RequestParam("rejectReason") String rejectReason) {
        Leave leave = leaveService.getLeaveById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid leave ID"));
        leave.setStatus("REJECTED");
        leave.setRejectReason(rejectReason);
        leaveService.saveLeave(leave);
        return "redirect:/Leave";
    }

    // Submit appeal
    @PostMapping("/appeal/{id}")
    public String submitAppeal(@PathVariable Long id,
                               @RequestParam String appealReason,
                               Principal principal) {
        leaveService.submitAppeal(id, appealReason, principal);
        return "redirect:/Leave";
    }

    // Global controller advice for username to display at the topnav
    @ControllerAdvice
    public static class GlobalControllerAdvice {

        private final UserService userService;

        public GlobalControllerAdvice(UserService userService) {
            this.userService = userService;
        }

        @ModelAttribute
        public void addUsername(Model model) {
            User currentUser = userService.getCurrentUser();
            if (currentUser != null) {
                model.addAttribute("username", currentUser.getUsername());
            }
        }
    }
}
