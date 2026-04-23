package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.Product;
import com.mobilestore.Abdulbasit.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(value = "brand", required = false) String brand) throws Exception {
        List<Product> allProducts = productService.getAllProducts();
        List<Product> listProducts;

        if (brand != null && !brand.isEmpty()) {
            listProducts = productService.getProductsByBrand(brand);
        } else {
            listProducts = allProducts;
        }

        List<String> listBrands = allProducts.stream()
                .map(Product::getBrand)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("selectedBrand", brand);

        return "index";
    }

    // FIXED: Added 'throws Exception' to handle Firestore call
    @GetMapping("/product/{id}")
    public String viewProductDetails(@PathVariable String id, Model model) throws Exception {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product_details";
        }
        return "redirect:/";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) throws Exception {
        List<Product> allProducts = productService.getAllProducts();
        List<Product> filteredProducts = allProducts.stream()
                .filter(p -> (p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase())) ||
                        (p.getBrand() != null && p.getBrand().toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());

        List<String> listBrands = allProducts.stream()
                .map(Product::getBrand)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        model.addAttribute("listProducts", filteredProducts);
        model.addAttribute("listBrands", listBrands);
        return "index";
    }

    @GetMapping("/seed-huawei")
    @ResponseBody
    public String seedHuaweiProducts() {
        try {
            java.util.ArrayList<Product> huaweiList = new java.util.ArrayList<>();

            // ... (Your Huawei product seeding logic remains the same)
            // Just ensuring saveProduct is called inside the try-catch block

            String[] names = {"Pura 70 Pro", "Mate XT", "Mate X6", "Mate 50", "Nova 10 Pro",
                    "Mate 20 Pro", "Mate 40 Pro", "P30 Pro", "Pura 80", "Pura X"};

            for (String name : names) {
                Product p = new Product();
                p.setName(name);
                p.setBrand("Huawei");
                p.setPrice(50000.0 + (Math.random() * 100000));
                p.setImage("/images/default.jpg");
                p.setDescription("High performance Huawei device.");
                productService.saveProduct(p);
            }

            return "✅ Success! Huawei devices added.";
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }
}