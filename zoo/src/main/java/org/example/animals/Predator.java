package org.example.animals;

import org.example.food.Food;
import org.example.food.Grass;

public abstract class Predator extends Animal {

    public Predator(String name, int age, boolean isMale) {
        super(name, age, isMale);
    }

    @Override
    public void eat(Food food, int amount) {
        super.eat(food, amount);
    };
}
