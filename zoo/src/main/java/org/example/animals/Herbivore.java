package org.example.animals;

import org.example.food.Food;
import org.example.food.Grass;

public abstract class Herbivore extends Animal{
    public Herbivore(String name, int age, boolean isMale) {
        super(name, age, isMale);
    }

    @Override
    public void eat(Food food, int amount) {
        if (food instanceof Grass) {
            super.eat(food, amount);
        } else {
            super.badFood();
        }
    }
}
