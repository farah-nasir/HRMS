package com.example.HRMS.controller;

import com.example.HRMS.entity.User;
import com.example.HRMS.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

   @GetMapping("/login")
    public String loginPage(Model model) {
        return "login"; // looks for login.html in templates/
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpServletRequest request) {

        User user = userService.validateUser(username, password);
        if (user != null) {
            // 1. Load authorities (ROLE_ADMIN, ROLE_MANAGER, etc.)
            List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().getName()) // e.g. "ROLE_ADMIN"
            );

            // 2. Create UserDetails-like object
            org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(
                            user.getUsername(),
                            user.getPassword(),
                            authorities
                    );

            // 3. Build Authentication object
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities);

            // 4. Put it into SecurityContext
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            // 5. Persist it in session so it survives redirects
            HttpSession session = request.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context
            );

            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password!");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("role", authentication.getAuthorities().iterator().next().getAuthority());

        System.out.println("DEBUG User: " + userDetails.getUsername() +
                        " Role: " + authentication.getAuthorities());

        return "dashboard";
    }

    @GetMapping("/users")
    public String usersPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/login";
        }

        // Restrict access to admins only
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return "error/403"; //TODO Make templates/error/403.html
        }

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);

        return "users";
    }

    // @GetMapping("/departments")
    // public String departmentsPage(Model model) {
    //     // You can add attributes to the model if needed
    //     // e.g., model.addAttribute("departments", departmentService.getAllDepartments());
    //     return "departments"; // maps to templates/departments.html
    // }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
