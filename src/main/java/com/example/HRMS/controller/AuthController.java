package com.example.HRMS.controller;

import com.example.HRMS.entity.User;
import com.example.HRMS.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                        HttpSession session) {

        User user = userService.validateUser(username, password);
        if (user != null) {
            session.setAttribute("loggedInUser", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password!");
            return "login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", user.getUsername());
       model.addAttribute("role", user.getRole() != null ? user.getRole().getName() : "No role assigned");
       System.out.println("DEBUG User: " + user.getUsername() + " Role: " + 
    (   user.getRole() != null ? user.getRole().getName() : "NULL"));

        return "dashboard"; // dashboard.html
        
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
