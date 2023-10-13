package ru.otus.java.pro;

import java.util.ArrayList;
import java.util.List;

public class Box<E extends Fruit> {
    private final List<E> fruits = new ArrayList<>();

    public void addFruit(E fruit) {
        fruits.add(fruit);
    }

    public Integer weight() {
        return fruits.stream()
                .map(Fruit::getWeight)
                .reduce(0, Integer::sum);
    }

    public boolean compare(Box<?> boxToCompare) {
        return weight().equals(boxToCompare.weight());
    }

    public void transferIntoAnother(Box<? super E> anotherBox) throws IllegalStateException {
        if (anotherBox == null) {
            throw new IllegalStateException("Destination box should not be null");
        }

        if (anotherBox.equals(this)) {
            return;
        }
        anotherBox.fruits.addAll(fruits);
        fruits.clear();
    }
}
