package org.example.animals;

import org.example.food.Fish;
import org.example.food.Food;
import org.example.interfaces.Water;

public class Dolphin extends Animal implements Water {
    public Dolphin(String name, int age, boolean isMale) {
        super(name, age, isMale);
    }

    @Override
    public void eat(Food food, int amount) {
        if (food instanceof Fish) {
            super.eat(food, amount);
        } else {
            super.badFood();
        }
    }

    @Override
    public void swim() {
        System.out.println("This dolphin is swimming!");
    }
}
