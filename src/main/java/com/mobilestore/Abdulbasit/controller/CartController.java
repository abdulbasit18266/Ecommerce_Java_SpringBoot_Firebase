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
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            // Firestore String ID use ho rahi hai, casting (int) ki zaroorat nahi
            cartService.addToCart(user.getId(), productId);
            return "redirect:/cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/product/" + productId + "?error=failed";
        }
    }

    // 2. View Cart Page (ISKI WAJAH SE 404 AA RAHA THA)
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            // Cart items fetch karo details ke saath
            List<Object[]> cartItems = cartService.getCartWithProducts(user.getId());

            // Total price calculate karne ka logic
            double total = cartItems.stream()
                    .mapToDouble(item -> (double) item[2]) // Price index 2 par hai
                    .sum();

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", total);

            return "cart"; // Ye return karega cart.html template
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    // 3. Remove from Cart Logic
    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable("id") String productId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            cartService.removeFromCart(user.getId(), productId);
            return "redirect:/cart?removed=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/cart?error=deletefailed";
        }
    }
}