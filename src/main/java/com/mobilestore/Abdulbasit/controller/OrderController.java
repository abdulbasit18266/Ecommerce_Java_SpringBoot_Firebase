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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String showCheckoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        try {
            List<Object[]> cartItems = cartService.getCartWithProducts(user.getId());
            double total = cartItems.stream()
                    .mapToDouble(item -> Double.parseDouble(item[2].toString()))
                    .sum();

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("total", total);
            model.addAttribute("user", user); // User name display ke liye
            return "checkout";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/place-order")
    public String placeOrder(HttpSession session,
                             @RequestParam String fullName,
                             @RequestParam String address,
                             @RequestParam String city,
                             @RequestParam String phone) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/login";

        try {
            // 1. Calculate Total from Cart
            List<Object[]> cartItems = cartService.getCartWithProducts(user.getId());
            double total = cartItems.stream()
                    .mapToDouble(item -> Double.parseDouble(item[2].toString()))
                    .sum();

            // 2. Create Order Object (Professional Mapping)
            Order order = new Order();
            order.setId(UUID.randomUUID().toString()); // Generate Unique ID
            order.setCustomerName(fullName);
            order.setEmail(user.getEmail());
            order.setAddress(address + ", " + city);
            order.setPhoneNumber(phone);
            order.setTotalAmount(total);
            order.setStatus("Pending");
            order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            // 3. Save to Firestore (Problem 1, 4, 5 FIX)
            orderService.saveOrder(order);

            // 4. Clear Cart after Order (Problem 2 FIX)
            for (Object[] item : cartItems) {
                cartService.removeFromCart(user.getId(), item[4].toString()); // index 4 is product ID
            }

            return "redirect:/order-success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/my-orders")
    public String showMyOrders(HttpSession session, Model model) {
        try {
            User user = (User) session.getAttribute("loggedInUser");
            if (user == null) return "redirect:/login";

            // Problem 1 FIX: Get all orders and filter by user email
            List<Order> allOrders = orderService.getAllOrders();
            List<Order> userOrders = allOrders.stream()
                    .filter(o -> o.getEmail() != null && o.getEmail().equals(user.getEmail()))
                    .toList();

            model.addAttribute("orders", userOrders);
            model.addAttribute("user", user);
            return "my_orders";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/order-success")
    public String orderSuccess() {
        return "order_success";
    }
}