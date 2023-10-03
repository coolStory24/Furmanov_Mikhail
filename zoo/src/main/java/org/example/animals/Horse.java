package org.example.animals;

import org.example.food.Food;
import org.example.interfaces.Land;

public class Horse extends Herbivore implements Land {
    public Horse(String name, int age, boolean isMale) {
        super(name, age, isMale);
    }

    @Override
    public void eat(Food food, int amount) {
        super.eat(food, amount);
    }

    @Override
    public void walk() {
        System.out.println("This horse is walking!");
    }
}
