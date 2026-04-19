package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.User;
import com.mobilestore.Abdulbasit.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    // 1. Add to Cart Logic
    @GetMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") String productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            cartService.addToCart(user.getId(), productId);
            return "redirect:/?success=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?error=true";
        }
    }

    // 2. Show Cart Page (FIXED: Subtotal & Total Amount null problem)
    @GetMapping("/cart")
    public String showCartPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<Object[]> cartItems = cartService.getCartWithProducts(user.getId());

            // Calculation Logic for Subtotal
            double total = 0;
            if (cartItems != null && !cartItems.isEmpty()) {
                total = cartItems.stream()
                        .mapToDouble(item -> {
                            try {
                                return Double.parseDouble(item[2].toString());
                            } catch (Exception e) {
                                return 0.0;
                            }
                        })
                        .sum();
            }

            // HTML variables matching
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", total); // Isse 'null' hat jayega
            return "cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?error=true";
        }
    }

    // 3. Remove Item Logic (FIXED: 404 Error problem)
    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable("id") String productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            // Null check taaki console error na aaye
            if (productId != null) {
                cartService.removeFromCart(user.getId(), productId);
            }
            return "redirect:/cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/cart?error=true";
        }
    }
}