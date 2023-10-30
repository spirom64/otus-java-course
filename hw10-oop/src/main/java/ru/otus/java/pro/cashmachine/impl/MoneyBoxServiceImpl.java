package ru.otus.java.pro.cashmachine.impl;

import ru.otus.java.pro.cashmachine.MoneyBoxService;

public class MoneyBoxServiceImpl implements MoneyBoxService {
    private final int acceptedNoteValue;

    private int currentNumberOfNotes = 0;

    public MoneyBoxServiceImpl(int acceptedNoteValue) {
        this.acceptedNoteValue = acceptedNoteValue;
    }

    public MoneyBoxServiceImpl(int acceptedNoteValue, int numberOfNotes) {
        this(acceptedNoteValue);
        this.currentNumberOfNotes = numberOfNotes;
    }

    public Integer getBalance() {
        return currentNumberOfNotes * acceptedNoteValue;
    }

    public Integer getCurrentNumberOfNotes() {
        return currentNumberOfNotes;
    }

    public Integer getMoney(int numberOfNotes) throws IllegalStateException {
        if (numberOfNotes > currentNumberOfNotes) {
            throw new IllegalStateException("Required number of notes exceeds current number");
        }

        if (numberOfNotes < 0) {
            throw new IllegalStateException("Required number if notes should be non-negative integer");
        }

        currentNumberOfNotes -= numberOfNotes;
        return numberOfNotes * acceptedNoteValue;
    }

    public void putMoney(int numberOfNotes) throws IllegalStateException {
        if (numberOfNotes < 0) {
            throw new IllegalStateException("Required number if notes should be non-negative integer");
        }

        currentNumberOfNotes += numberOfNotes;
    }
}
