package ru.otus.java.pro;

public class MainApp {
    static public void main(String [] args) {
        Box<Fruit> box1 = new Box<>();
        Box<Orange> box2 = new Box<>();
        box1.addFruit(new Apple());
        box2.addFruit(new Orange());
        box2.transferIntoAnother(box1);
    }
}
