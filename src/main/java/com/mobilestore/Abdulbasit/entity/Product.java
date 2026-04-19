package com.mobilestore.Abdulbasit.entity;

/**
 * Clean POJO class for Firebase Firestore.
 */
public class Product {

    private String id;
    private String name;
    private String brand;
    private double price; // Firestore Number (90000)
    private String description;
    private String image;
    private String storage;
    private String ram;
    private String camera; // Handling null values safely
    private String processor;
    private double rating; // Firestore Number (4.8)
    private String fullSpecs;

    // Mandatory Default Constructor
    public Product() {}

    // --- GETTERS & SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getStorage() { return storage; }
    public void setStorage(String storage) { this.storage = storage; }

    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }

    public String getCamera() { return camera; }
    public void setCamera(String camera) { this.camera = camera; }

    public String getProcessor() { return processor; }
    public void setProcessor(String processor) { this.processor = processor; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getFullSpecs() { return fullSpecs; }
    public void setFullSpecs(String fullSpecs) { this.fullSpecs = fullSpecs; }
}