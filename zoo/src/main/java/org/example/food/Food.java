package org.example.food;

public abstract class Food {
    protected int amount;

    public Food(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int increaseAmount(int add) {
        return this.amount += add;
    }

    public int decreaseAmount(int take) {
        return this.amount -= take;
    }
}
