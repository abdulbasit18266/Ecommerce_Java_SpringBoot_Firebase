package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.User;
import com.mobilestore.Abdulbasit.service.UserServices;
import com.mobilestore.Abdulbasit.service.OrderService;
import com.mobilestore.Abdulbasit.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired private UserServices userServices;
    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;

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

                // ✅ Migration logic
                String pendingId = (String) session.getAttribute("pendingProductId");
                if (pendingId != null) {
                    cartService.addToCart(user.getId(), pendingId);
                    session.removeAttribute("pendingProductId");
                }

                if ("ADMIN".equals(user.getRole())) return "redirect:/admin/dashboard";
                return "redirect:/";
            } else {
                // ✅ Update: Added showForgot flag to show the link only on error
                model.addAttribute("error", "Invalid Email or Password!");
                model.addAttribute("showForgot", true);
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error logging in.");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model, HttpSession session) {
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