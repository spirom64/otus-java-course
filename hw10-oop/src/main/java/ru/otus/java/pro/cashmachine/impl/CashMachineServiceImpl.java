package ru.otus.java.pro.cashmachine.impl;

import ru.otus.java.pro.cashmachine.CashMachineService;
import ru.otus.java.pro.cashmachine.MoneyBoxService;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class CashMachineServiceImpl implements CashMachineService {
    private final Map<Integer, MoneyBoxService> acceptedNoteValueToMoneyBoxMap = new HashMap<>();

    public CashMachineServiceImpl(Map<Integer, Integer> acceptedNoteValuesWithInitialNumbers) {
        for (Map.Entry<Integer, Integer> acceptedNoteValue : acceptedNoteValuesWithInitialNumbers.entrySet()) {
            acceptedNoteValueToMoneyBoxMap.put(acceptedNoteValue.getKey(), new MoneyBoxServiceImpl(acceptedNoteValue.getKey(), acceptedNoteValue.getValue()));
        }
    }

    public Map<Integer, Integer> getCurrentAvailableNotes() {
        Map<Integer, Integer> result = new HashMap<>();
        for (Map.Entry<Integer, MoneyBoxService> noteWithMoneyBox : acceptedNoteValueToMoneyBoxMap.entrySet()) {
            result.put(noteWithMoneyBox.getKey(), noteWithMoneyBox.getValue().getCurrentNumberOfNotes());
        }
        return result;
    }

    public Integer getBalance() {
        int result = 0;
        for (MoneyBoxService moneyBox : acceptedNoteValueToMoneyBoxMap.values()) {
            result += moneyBox.getBalance();
        }
        return result;
    }

    public void putMoney(Map<Integer, Integer> notesValueToAmountMap) throws IllegalStateException {
        for (Map.Entry<Integer, Integer> noteValueWithAmount : notesValueToAmountMap.entrySet()) {
            if (!acceptedNoteValueToMoneyBoxMap.containsKey(noteValueWithAmount.getKey())) {
                throw new IllegalStateException("Note value \"" + noteValueWithAmount.getKey() + "\" is not accepted");
            }
            acceptedNoteValueToMoneyBoxMap.get(noteValueWithAmount.getKey()).putMoney(noteValueWithAmount.getValue());
        }
    }

    public Map<Integer, Integer> getMoney(int amount) throws IllegalStateException {
        Map<Integer, Map.Entry<Integer, Map<Integer, Integer>>> availableAmountsWithPath = new HashMap<>();
        availableAmountsWithPath.put(0, new AbstractMap.SimpleEntry<>(0, new HashMap<>()));
        Map<Integer, Map.Entry<Integer, Map<Integer, Integer>>> tempMap;
        int maxRequiredNotes;

        if (amount < 0) {
            throw new IllegalStateException("Required amount should be non-negative integer");
        }

        if (amount == 0) {
            return null;
        }

        for (Map.Entry<Integer, MoneyBoxService> noteValueWithMoneyBox : acceptedNoteValueToMoneyBoxMap.entrySet()) {
            maxRequiredNotes = Math.min(noteValueWithMoneyBox.getValue().getCurrentNumberOfNotes(),
                    amount / noteValueWithMoneyBox.getKey());
            tempMap =  new HashMap<>(availableAmountsWithPath);
            for (Map.Entry<Integer, Map.Entry<Integer, Map<Integer, Integer>>> availableAmount : availableAmountsWithPath.entrySet()) {
                for (int i = 1; i <= maxRequiredNotes; i++) {
                    int currentAmount = availableAmount.getKey() + i * noteValueWithMoneyBox.getKey();
                    if (currentAmount > amount) {
                        break;
                    }
                    int currentPathLength = tempMap.get(availableAmount.getKey()).getKey() + i;
                    if (!tempMap.containsKey(currentAmount) || tempMap.get(currentAmount).getKey() > currentPathLength) {
                        Map<Integer, Integer> currentPath = new HashMap<>(tempMap.get(availableAmount.getKey()).getValue());
                        currentPath.put(noteValueWithMoneyBox.getKey(), i);
                        tempMap.put(
                                currentAmount,
                                new AbstractMap.SimpleEntry<>(currentPathLength, currentPath));
                    }
                }
            }
            availableAmountsWithPath = tempMap;
        }

        if (!availableAmountsWithPath.containsKey(amount)) {
            throw new IllegalStateException("Amount of \"" + amount + "\" cannot be served");
        }

        Map<Integer, Integer> result = availableAmountsWithPath.get(amount).getValue();
        for (Map.Entry<Integer, Integer> requiredNoteWithAmount: result.entrySet()) {
            acceptedNoteValueToMoneyBoxMap.get(requiredNoteWithAmount.getKey()).getMoney(requiredNoteWithAmount.getValue());
        }

        return result;
    }
}
