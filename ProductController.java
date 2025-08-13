package com.example.online_shopping_springboot;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Controller

public class ProductController {
	private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ProductRepository productRepository;

    
    @GetMapping("/addproduct")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    
    @PostMapping("/addproduct1")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException {

        Product existingProduct = null;

        // If the product has an ID, it's an update operation
        if (product.getId() != null) {
            existingProduct = productRepository.findById(product.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product Id: " + product.getId()));

            // If no new image is uploaded, preserve the existing image
            if (multipartFile.isEmpty()) {
                product.setImageUrl(existingProduct.getImageUrl());
            }
        }

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = UPLOAD_DIR;

            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) uploadPath.mkdirs();

            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            product.setImageUrl("/" + uploadDir + fileName);
        }

        productRepository.save(product);  // Works for both new and existing products
        return "redirect:/viewshopping";
    }

    @GetMapping("/viewshopping")
    public String viewProducts(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "product_list";
    }

    // ✅ Show form to edit product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id: " + id));
        model.addAttribute("product", product);
        return "product_form";
    }

    // ✅ Delete product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        productRepository.delete(product);
        return "redirect:/viewshopping";
    }
    
}
