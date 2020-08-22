package com.example.bmi.Model;

public class Food {

    private String foodId;

    private String label;

    private Nutrients nutrients;

    private String brand;

    private String category;

    private String categoryLabel;

    private String foodContentsLabel;

    public Food(String foodId, String label, Nutrients nutrients, String brand, String category, String categoryLabel, String foodContentsLabel) {
        this.foodId = foodId;
        this.label = label;
        this.nutrients = nutrients;
        this.brand = brand;
        this.category = category;
        this.categoryLabel = categoryLabel;
        this.foodContentsLabel = foodContentsLabel;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public String getFoodContentsLabel() {
        return foodContentsLabel;
    }

    public void setFoodContentsLabel(String foodContentsLabel) {
        this.foodContentsLabel = foodContentsLabel;
    }
}
