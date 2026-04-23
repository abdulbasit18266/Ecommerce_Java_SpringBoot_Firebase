package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.Order;
import com.mobilestore.Abdulbasit.entity.Product;
import com.mobilestore.Abdulbasit.service.OrderService;
import com.mobilestore.Abdulbasit.service.ProductFirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private ProductFirestoreService productService;
    @Autowired private OrderService orderService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        try {
            List<Product> products = productService.getAllProducts();
            model.addAttribute("totalProducts", products != null ? products.size() : 0);
            model.addAttribute("products", products);

            List<Order> orders = orderService.getAllOrders();
            model.addAttribute("totalOrders", orders != null ? orders.size() : 0);
            model.addAttribute("orders", orders);

            // ✅ FIX: Revenue Calculation (Saare orders ka sum)
            double revenue = 0;
            if (orders != null) {
                revenue = orders.stream()
                        .filter(o -> o.getTotalAmount() != null)
                        .mapToDouble(Order::getTotalAmount)
                        .sum();
            }
            model.addAttribute("totalRevenue", revenue);

            // ✅ FIX: Graph Data (Order amounts ki list)
            List<Double> orderAmounts = (orders != null) ?
                    orders.stream().map(Order::getTotalAmount).collect(Collectors.toList()) : List.of();
            model.addAttribute("orderAmounts", orderAmounts);

            // ✅ FIX: Brand Split (Doughnut Chart ke liye)
            long apple = (products != null) ? products.stream().filter(p -> p.getBrand().equalsIgnoreCase("Apple")).count() : 0;
            long samsung = (products != null) ? products.stream().filter(p -> p.getBrand().equalsIgnoreCase("Samsung")).count() : 0;
            long others = (products != null) ? (products.size() - (apple + samsung)) : 0;

            model.addAttribute("appleCount", apple);
            model.addAttribute("samsungCount", samsung);
            model.addAttribute("otherCount", others);

            return "admin_dashboard";
        } catch (Exception e) { return "error"; }
    }

    @GetMapping("/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin_orders";
    }

    @PostMapping("/update-order-status")
    @ResponseBody
    public String updateOrderStatus(@RequestParam String orderId, @RequestParam String status) {
        try {
            orderService.updateStatus(orderId, status);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}