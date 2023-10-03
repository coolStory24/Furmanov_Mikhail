package org.example.animals;
import org.example.food.Food;
import org.example.food.Meat;
import org.example.interfaces.Air;

public class Eagle extends Predator implements Air {
    public Eagle(String name, int age, boolean isMale) {
        super(name, age, isMale);
    }

    @Override
    public void eat(Food food, int amount) {
        if (food instanceof Meat) {
            super.eat(food, amount);
        } else {
            super.badFood();
        }
    }

    @Override
    public void fly() {
        System.out.println("This eagle is flying!");
    }
}
