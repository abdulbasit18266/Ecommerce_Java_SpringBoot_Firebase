package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.User;
import com.mobilestore.Abdulbasit.service.UserServices;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class ForgotPasswordController {

    @Autowired private UserServices userServices;
    @Autowired private JavaMailSender mailSender; // Email bhejne ke liye

    // 1. Forgot Password Page dikhana
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot_password";
    }

    // 2. Email par OTP bhejna
    @PostMapping("/forgot-password")
    public String sendOTP(@RequestParam("email") String email, HttpSession session, Model model) {
        User user = userServices.findByEmail(email);
        if (user == null) {
            model.addAttribute("error", "This email is not registered!");
            return "forgot_password";
        }

        // 6-digit OTP generate karna
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Session mein save karna taaki verify kar sakein
        session.setAttribute("otp", otp);
        session.setAttribute("resetEmail", email);

        // Email bhejna
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Password Reset OTP - Mobile Store");

            // ✅ Time 1 minute set kiya gaya hai
            message.setText("Your OTP for password reset is: " + otp + "\nValid for 1 minute. Please do not share this code with anyone.");

            mailSender.send(message);

            return "verify_otp";
        } catch (Exception e) {
            model.addAttribute("error", "Error sending email. Try again.");
            return "forgot_password";
        }
    } // ✅ Yeh wala bracket miss tha, maine add kar diya hai.

    // 3. OTP Verify karna
    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam("otp") String userOtp, HttpSession session, Model model) {
        String sessionOtp = (String) session.getAttribute("otp");
        if (sessionOtp != null && sessionOtp.equals(userOtp)) {
            return "reset_password";
        }
        model.addAttribute("error", "Invalid OTP!");
        return "verify_otp";
    }

    // 4. Naya Password Update karna
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("password") String newPassword, HttpSession session) {
        String email = (String) session.getAttribute("resetEmail");
        if (email != null) {
            userServices.updatePassword(email, newPassword);
            session.invalidate(); // Clear session after reset
            return "redirect:/login?resetSuccess=true";
        }
        return "redirect:/forgot-password";
    }
}