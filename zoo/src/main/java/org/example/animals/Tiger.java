package org.example.animals;

import org.example.food.Beef;
import org.example.food.Food;
import org.example.interfaces.Land;

public class Tiger extends Predator implements Land {

    public Tiger(String name, int age, boolean isMale) {
        super(name, age, isMale);
    }

    @Override
    public void eat(Food food, int amount) {
        if (food instanceof Beef) {
            super.eat(food, amount);
        } else {
            super.badFood();
        }
    }

    @Override
    public void walk() {
        System.out.println("This tiger is walking!");
    }
}
