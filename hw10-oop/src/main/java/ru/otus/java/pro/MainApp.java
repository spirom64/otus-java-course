package ru.otus.java.pro;

import ru.otus.java.pro.cashmachine.CashMachineEmulator;
import ru.otus.java.pro.cashmachine.impl.CashMachineEmulatorImpl;


public class MainApp {
    public static void main(String[] args) {
        CashMachineEmulator cashMachineEmulator = new CashMachineEmulatorImpl();
        cashMachineEmulator.operate();
    }
}
