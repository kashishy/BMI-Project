package com.example.bmi;

import java.util.HashMap;
import java.util.Map;

public class UserData {

    private String name, email, mobile, age, gender, weight, height, activity;

    public UserData() {

    }
    public UserData(String name, String email, String mobile, String age, String gender, String weight, String height, String activity) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.activity = activity;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("mobile", mobile);
        result.put("age", age);
        result.put("gender", gender);
        result.put("weight", weight);
        result.put("height", height);
        result.put("activity", activity);

        return result;
    }
}
