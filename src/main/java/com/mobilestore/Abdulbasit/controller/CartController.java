package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.User;
import com.mobilestore.Abdulbasit.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    // ✅ FIXED: Missing Add to Cart Mapping (Jo index.html se call hota hai)
    @GetMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") String productId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getId() == null) return "redirect:/login";

        try {
            cartService.addToCart(user.getId(), productId);
            return "redirect:/?success=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?error=true";
        }
    }

    @GetMapping("/cart")
    public String showCartPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            List<Object[]> cartItems = cartService.getCartWithProducts(user.getId());
            // item[3] = Price, item[5] = Quantity
            double total = cartItems.stream().mapToDouble(item -> {
                try {
                    return Double.parseDouble(item[3].toString()) * Integer.parseInt(item[5].toString());
                } catch (Exception e) { return 0.0; }
            }).sum();

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", total);
            return "cart";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?error";
        }
    }

    @GetMapping("/cart/update/{id}/{change}")
    public String updateQty(@PathVariable String id, @PathVariable int change, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try { cartService.updateQuantity(user.getId(), id, change); } catch (Exception e) {}
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove-from-cart/{id}")
    public String removeFromCart(@PathVariable String id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try { cartService.removeFromCart(user.getId(), id); } catch (Exception e) {}
        }
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        try {
            List<Object[]> items = cartService.getCartWithProducts(user.getId());
            double total = items.stream().mapToDouble(item -> {
                try {
                    return Double.parseDouble(item[3].toString()) * Integer.parseInt(item[5].toString());
                } catch (Exception e) { return 0.0; }
            }).sum();
            model.addAttribute("cartItems", items);
            model.addAttribute("totalPrice", total);
            return "checkout";
        } catch (Exception e) { return "redirect:/cart"; }
    }
}