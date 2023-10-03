package org.example.animals;

import org.example.food.Food;

public abstract class Animal {
    protected String name;
    protected int age;
    protected boolean isMale;

    public Animal(String name, int age, boolean isMale) {
        this.name = name;
        this.age = age;
        this.isMale = isMale;
    }

    protected void badFood() {
        System.out.println("I don't want to eat it.");
    }

    public void eat(Food food, int amount) {
        if (food.getAmount() >= amount) {
            food.decreaseAmount(amount);
            System.out.println("It's so tasty!");
        }  else {
            System.out.println("It's not enough food for me :(");
        }
    };

    @Override
    public String toString() {
        return name + ", "+ (isMale ? "male" : "female") +", " + age + "y. o. ";
    }
}
