package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.Product;
import com.mobilestore.Abdulbasit.service.ProductFirestoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private ProductFirestoreService productService;

    // 1. Home Page (Brand Filter ke saath)
    @GetMapping("/")
    public String viewHomePage(@RequestParam(value = "brand", required = false) String brand, Model model) {
        try {
            List<Product> listProducts;
            if (brand != null && !brand.isEmpty()) {
                listProducts = productService.getProductsByBrand(brand);
            } else {
                listProducts = productService.getAllProducts();
            }
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("currentBrand", brand);
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    // 2. Product Details Page (FIXED)
    @GetMapping("/product/{id}")
    public String viewProductDetail(@PathVariable("id") String id, Model model) {
        try {
            System.out.println("DEBUG: Fetching details for ID: " + id);

            Product product = productService.getProductById(id);

            if (product != null) {
                model.addAttribute("product", product);
                // Make sure aapki file ka naam 'product_details.html' hi hai templates folder mein
                return "product_details";
            } else {
                System.err.println("DEBUG: Product not found!");
                return "redirect:/?error=notfound";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    // 3. Search Logic
    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        try {
            List<Product> allProducts = productService.getAllProducts();
            List<Product> filteredProducts = allProducts.stream()
                    .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                            p.getBrand().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());

            model.addAttribute("listProducts", filteredProducts);
            model.addAttribute("keyword", keyword);
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}