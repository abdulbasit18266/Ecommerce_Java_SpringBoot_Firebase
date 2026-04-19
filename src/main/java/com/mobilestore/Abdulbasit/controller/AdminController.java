package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.Order;
import com.mobilestore.Abdulbasit.entity.Product;
import com.mobilestore.Abdulbasit.service.OrderService;
import com.mobilestore.Abdulbasit.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    // ✅ FIXED: Added 'throws Exception' to handle Firestore async calls
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) throws Exception {
        List<Product> products = productService.getAllProducts();
        List<Order> orders = orderService.getAllOrders();

        if (products == null) products = List.of();
        if (orders == null) orders = List.of();

        long appleCount = products.stream()
                .filter(p -> p.getBrand() != null && (p.getBrand().equalsIgnoreCase("Apple") || p.getBrand().contains("iPhone")))
                .count();
        long samsungCount = products.stream()
                .filter(p -> p.getBrand() != null && (p.getBrand().equalsIgnoreCase("Samsung") || p.getBrand().contains("Galaxy")))
                .count();
        long otherCount = products.size() - (appleCount + samsungCount);

        double totalRevenue = orders.stream().mapToDouble(Order::getTotalAmount).sum();

        List<Double> orderAmounts = orders.stream()
                .map(Order::getTotalAmount)
                .collect(Collectors.toList());

        model.addAttribute("products", products);
        model.addAttribute("totalProducts", products.size());
        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("orderAmounts", orderAmounts);

        model.addAttribute("appleCount", appleCount);
        model.addAttribute("samsungCount", samsungCount);
        model.addAttribute("otherCount", otherCount);

        return "admin_dashboard";
    }

    @GetMapping("/add-product")
    public String addProductPage() {
        return "add_product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/edit-product/{id}")
    public String editProductPage(@PathVariable String id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "edit_product";
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/update-product")
    public String updateProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return "redirect:/admin/dashboard";
    }

    // ✅ FIXED: Added 'throws Exception' here as well
    @GetMapping("/orders")
    public String viewOrders(Model model) throws Exception {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin_orders";
    }
}