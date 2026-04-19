package com.mobilestore.Abdulbasit.controller;

import com.mobilestore.Abdulbasit.entity.Product;
import com.mobilestore.Abdulbasit.service.ProductService;
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

    @GetMapping("/product/{id}")
    public String viewProductDetails(@PathVariable String id, Model model) {
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
                .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()) ||
                        p.getBrand().toLowerCase().contains(query.toLowerCase()))
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
}