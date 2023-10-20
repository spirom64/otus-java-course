package ru.otus.java.pro.cashmachine.impl;

import ru.otus.java.pro.cashmachine.CashMachineEmulator;
import ru.otus.java.pro.cashmachine.CashMachineService;

import java.util.*;
import java.util.stream.Collectors;

public class CashMachineEmulatorImpl implements CashMachineEmulator {
    private static final Set<Integer> DEFAULT_ACCEPTED_NOTE_VALUES = Set.of(100, 200, 500, 1000, 2000, 5000);
    private static final int DEFAULT_INITIAL_NOTES_NUMBER = 10;
    private final Set<Integer> acceptedNoteValues;
    private final CashMachineService cashMachineService;

    private final Scanner in = new Scanner(System.in);

    public CashMachineEmulatorImpl() {
        Map<Integer, Integer> acceptedNoteValuesWithInitialNumbers = new HashMap<>();
        for (int acceptedNoteValue : DEFAULT_ACCEPTED_NOTE_VALUES) {
            acceptedNoteValuesWithInitialNumbers.put(acceptedNoteValue, DEFAULT_INITIAL_NOTES_NUMBER);
        }
        acceptedNoteValues = DEFAULT_ACCEPTED_NOTE_VALUES;
        cashMachineService = new CashMachineServiceImpl(acceptedNoteValuesWithInitialNumbers);
    }

    public CashMachineEmulatorImpl(Set<Integer> acceptedNoteValues, int initialNumberOfNotes) {
        Map<Integer, Integer> acceptedNoteValuesWithInitialNumbers = new HashMap<>();
        for (int acceptedNoteValue : acceptedNoteValues) {
            acceptedNoteValuesWithInitialNumbers.put(acceptedNoteValue, initialNumberOfNotes);
        }
        this.acceptedNoteValues = acceptedNoteValues;
        cashMachineService = new CashMachineServiceImpl(acceptedNoteValuesWithInitialNumbers);
    }

    private void prettyPrintCurrentState() {
        System.out.println("Currently available notes:");
        for (Map.Entry<Integer, Integer> noteWithAmount : cashMachineService.getCurrentAvailableNotes().entrySet()) {
            System.out.println(noteWithAmount.getKey() + " - " + noteWithAmount.getValue());
        }
        System.out.println();
    }

    private void handlePutMoney() {
        int noteValue;
        int notesNumber;

        System.out.print("\nEnter the note value to put: ");
        try {
            noteValue = Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Note value should be correct integer!\n");
            return;
        }

        System.out.print("\nEnter notes number to put: ");
        try {
            notesNumber = Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Notes number should be correct integer!\n");
            return;
        }

        try {
            cashMachineService.putMoney(Map.of(noteValue, notesNumber));
            System.out.println("Successfully put money\n");
            prettyPrintCurrentState();
        } catch (IllegalStateException e) {
            System.out.println("Failed to put money: " + e + "\n");
        }
    }

    private void handleGetMoney() {
        int amount;

        System.out.print("\nEnter the money amount to get: ");
        try {
            amount = Integer.parseInt(in.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Money amount should be correct integer!\n");
            return;
        }

        try {
            Map<Integer, Integer> result = cashMachineService.getMoney(amount);

            if (result == null) {
                System.out.println("Nothing returned\n");
            } else {
                System.out.println("Successfully got money\n");
            }
            prettyPrintCurrentState();
        } catch (IllegalStateException e) {
            System.out.println("Failed to get money: " + e + "\n");
        }
    }

    public void operate() {
        List<Integer> acceptedNoteValuesSorted = new ArrayList<>(acceptedNoteValues);
        Collections.sort(acceptedNoteValuesSorted);

        System.out.println("Cash machine emulator, v. 1.0");
        System.out.println("Accepted note values: " + acceptedNoteValuesSorted.stream()
                .map(String::valueOf).collect(Collectors.joining(", ")) + "\n");
        prettyPrintCurrentState();
        System.out.println();

        String command;
        boolean exitFlag = false;

        while(true) {
            System.out.println("Enter next command:");
            System.out.println("\"B\" to check current balance");
            System.out.println("\"G\" to get money");
            System.out.println("\"P\" to put money");
            System.out.println("\"Q\" to quit\n");
            System.out.print("-> ");
            command = in.nextLine();
            switch (command.toLowerCase()) {
                case "g":
                    handleGetMoney();
                    break;
                case "p":
                    handlePutMoney();
                    break;
                case "b":
                    System.out.println("Current balance: " + cashMachineService.getBalance() + "\n");
                    break;
                case "q":
                    exitFlag = true;
                    break;
                default:
                    System.out.println("Unknown command: \"" + command + "\"\n");
            }

            if (exitFlag) {
                break;
            }
        }
    }
}
