package ru.otus.java.pro.cashmachine;

import java.util.Map;

public interface CashMachineService {
    Integer getBalance();
    Map<Integer, Integer> getMoney(int amount) throws IllegalStateException;
    Map<Integer, Integer> getCurrentAvailableNotes();
    void putMoney(Map<Integer, Integer> notesValueToAmountMap) throws IllegalStateException;
}
