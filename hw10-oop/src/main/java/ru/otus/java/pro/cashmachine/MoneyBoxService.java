package ru.otus.java.pro.cashmachine;


public interface MoneyBoxService {
    Integer getBalance();
    Integer getCurrentNumberOfNotes();
    Integer getMoney(int numberOfNotes) throws IllegalStateException;
    void putMoney(int numberOfNotes) throws IllegalStateException;
}
