package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.User;
import com.mobilestore.Abdulbasit.entity.Order;
import com.mobilestore.Abdulbasit.service.UserServices;
import com.mobilestore.Abdulbasit.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserServices userServices; // Ensure matches class name

    @Autowired
    private OrderService orderService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        try {
            User user = userServices.login(email, password);
            if (user != null) {
                session.setAttribute("user", user);
                // Admin role check (Optional but good)
                if ("ADMIN".equals(user.getRole())) {
                    return "redirect:/admin/dashboard";
                }
                return "redirect:/";
            } else {
                model.addAttribute("error", "Invalid Email or Password!");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong. Please try again.");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            user.setRole("USER");
            userServices.saveUser(user);
            return "redirect:/login?success=true";
        } catch (Exception e) {
            model.addAttribute("error", "Registration Failed!");
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}