package org.example.animals;

import org.example.food.Food;
import org.example.interfaces.Land;

public class Camel extends  Herbivore implements Land {
    public Camel(String name, int age, boolean isMale) {
        super(name, age, isMale);
    }

    @Override
    public void eat(Food food, int amount) {
        super.eat(food, amount);
    }

    @Override
    public void walk() {
        System.out.println("This camel is walking!");
    }
}
