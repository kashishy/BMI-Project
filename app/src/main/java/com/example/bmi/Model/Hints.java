package com.example.bmi.Model;

import java.util.List;

public class Hints {

    private Food food;

    private List<Measures> measures;

    public Hints(Food food, List<Measures> measures) {
        this.food = food;
        this.measures = measures;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public List<Measures> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measures> measures) {
        this.measures = measures;
    }
}
