package com.dinecraft.app;

public class Food {
    private String id;
    private String name;
    private String category;
    private String description;
    private double price;
    private String imageName; // name of drawable (no extension)

    // Required empty constructor for some serializers (Firestore)
    public Food() {}

    public Food(String id, String name, String category, String description, double price, String imageName) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.imageName = imageName;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageName() { return imageName; } // <- used by adapter

    // optional setters if you need them
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}
