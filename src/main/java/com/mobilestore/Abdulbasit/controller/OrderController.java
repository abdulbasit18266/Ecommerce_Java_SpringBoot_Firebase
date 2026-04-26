package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.Order;
import com.mobilestore.Abdulbasit.entity.User;
import com.mobilestore.Abdulbasit.service.OrderService;
import com.mobilestore.Abdulbasit.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private CartService cartService;

    @PostMapping("/place-order")
    public String placeOrder(@ModelAttribute Order order, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            // ✅ Important: User table ka email aur name session se le rahe hain
            order.setEmail(user.getEmail());
            // order.getPhoneNumber() mein form wala number aayega (jo user ne checkout mein dala hai)

            List<Object[]> cartItems = cartService.getCartWithProducts(user.getId());
            String productNames = cartItems.stream()
                    .map(item -> item[1].toString() + " (x" + item[5].toString() + ")")
                    .collect(Collectors.joining(", "));
            order.setName(productNames);

            double total = cartItems.stream()
                    .mapToDouble(item -> Double.parseDouble(item[3].toString()) * Integer.parseInt(item[5].toString()))
                    .sum();
            order.setTotalAmount(total);

            // Order save hoga naye shipping phone number ke saath
            orderService.saveOrder(order);

            cartService.clearCart(user.getId());
            session.removeAttribute("pendingProductId");

            return "redirect:/order-success";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/checkout?error=true";
        }
    }
    @GetMapping("/user/my-orders")
    public String viewMyOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        List<Order> userOrders = orderService.getOrdersByUserEmail(user.getEmail());
        model.addAttribute("orders", userOrders);
        return "my_orders";
    }

    @GetMapping("/order-success")
    public String orderSuccess() { return "order_success"; }
}