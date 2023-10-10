package org.example;

import org.example.animals.*;
import org.example.food.Beef;
import org.example.food.Fish;
import org.example.food.Grass;

public class Main {
    public static void main(String[] args) {
        // animals
        Camel camel = new Camel("Isabel", 10, false);
        Tiger tiger = new Tiger("Leo", 20, true);
        Dolphin dolphin = new Dolphin("Rocket", 15, false);
        Eagle eagle = new Eagle("Johny", 37, true);
        Horse horse = new Horse("Marry", 13, false);

        //food

        Beef beef = new Beef(100);
        Fish fish = new Fish(20);
        Grass grass = new Grass(50);

        System.out.println(tiger);
        tiger.walk();
        tiger.eat(fish, 30);
        tiger.eat(beef, 70);
        tiger.eat(beef,70);

        horse.eat(grass, 50);
        eagle.eat(fish, 10);
        dolphin.eat(fish, 10);

        dolphin.swim();
        eagle.fly();
    }
}