package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.Order;
import com.mobilestore.Abdulbasit.entity.User;
import com.mobilestore.Abdulbasit.service.OrderService;
import com.mobilestore.Abdulbasit.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            List<Object[]> cartItems = cartService.getCartWithProducts(user.getId());
            double total = cartItems.stream()
                    .mapToDouble(item -> Double.parseDouble(item[2].toString()))
                    .sum();

            model.addAttribute("cartItems", cartItems);
            // PROBLEM 1 FIX: Variable name exactly 'totalPrice' rakha hai jo image mein missing tha
            model.addAttribute("totalPrice", total);
            model.addAttribute("user", user);
            return "checkout";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/cart?error=true";
        }
    }

    @PostMapping("/place-order")
    public String placeOrder(HttpSession session,
                             @RequestParam String fullName,
                             @RequestParam String address,
                             @RequestParam String city,
                             @RequestParam String phone) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            List<Object[]> cartItems = cartService.getCartWithProducts(user.getId());
            if (cartItems.isEmpty()) return "redirect:/";

            double total = cartItems.stream()
                    .mapToDouble(item -> Double.parseDouble(item[2].toString()))
                    .sum();

            // 1. Save Order Details
            Order order = new Order();
            order.setId(UUID.randomUUID().toString());
            order.setCustomerName(fullName);
            order.setEmail(user.getEmail());
            order.setAddress(address + ", " + city);
            order.setPhoneNumber(phone);
            order.setTotalAmount(total);
            order.setStatus("Pending");
            order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            orderService.saveOrder(order);

            // 2. PROBLEM 2 & 3 FIX: Cart Clear with Null Check (Preventing NullPointerException)
            for (Object[] item : cartItems) {
                if (item != null && item.length > 4 && item[4] != null) {
                    cartService.removeFromCart(user.getId(), item[4].toString());
                }
            }

            // SUCCESS REDIRECT
            return "redirect:/order-success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/checkout?error=true";
        }
    }

    @GetMapping("/my-orders")
    public String showMyOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            List<Order> allOrders = orderService.getAllOrders();
            List<Order> userOrders = allOrders.stream()
                    .filter(o -> o.getEmail() != null && o.getEmail().equals(user.getEmail()))
                    .toList();

            model.addAttribute("orders", userOrders);
            model.addAttribute("user", user);
            return "my_orders";
        } catch (Exception e) {
            return "redirect:/?error=true";
        }
    }

    @GetMapping("/order-success")
    public String orderSuccess() {
        return "order_success"; // matches your order_success.html
    }
}