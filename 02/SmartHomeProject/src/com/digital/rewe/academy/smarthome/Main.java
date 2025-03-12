package com.digital.rewe.academy.smarthome;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        VacuumCleaner vacuumCleaner = new VacuumCleaner("RS106", LocalDate.of(2025, 1, 1),
            BigDecimal.valueOf(60), Placement.MOVABLE, false, BigDecimal.valueOf(100));

        System.out.println(vacuumCleaner.getId());

        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(90)));
        System.out.println("Battery Level left: " + vacuumCleaner.getBatteryLevel());

        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(2)));
        System.out.println("Battery Level left: " +vacuumCleaner.getBatteryLevel());

        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(2)));
        System.out.println("Battery Level left: " +vacuumCleaner.getBatteryLevel());

        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(1)));
        System.out.println("Battery Level left: " +vacuumCleaner.getBatteryLevel());

        vacuumCleaner.setBatteryLevel(BigDecimal.valueOf(100));
        System.out.println("Battery Level left: " +vacuumCleaner.getBatteryLevel());
        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(99)));
        System.out.println("Battery Level left: " +vacuumCleaner.getBatteryLevel());

        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(null));
        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(-6)));
        vacuumCleaner.setBatteryLevel(BigDecimal.valueOf(100));
        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(100)));
        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(120)));

        vacuumCleaner.setBatteryLevel(BigDecimal.valueOf(100));
        vacuumCleaner.setMop(true);
        System.out.println("Area cleaned: " + vacuumCleaner.cleanTargetArea(BigDecimal.valueOf(46)));


        SmartLight smartLight = new SmartLight("RS106", LocalDate.of(2024, 1, 1),
            BigDecimal.valueOf(10), Placement.BUILT_IN, BigDecimal.valueOf(120));

        System.out.println(smartLight.getId());
        System.out.println("Maximum Lumen Output: " + smartLight.getMaximumLumen());
        System.out.println("Current Lighting Mode: " + smartLight.getLightingMode());
        System.out.println("Lumen Output in Medium Lighting Mode: " + smartLight.getLumenOutput(LightingMode.MEDIUM));
        System.out.println("Current Lighting Mode: " + smartLight.getLightingMode());
        System.out.println("Lumen Output in Dim Lighting Mode: " + smartLight.getLumenOutput(LightingMode.DIM));
        System.out.println("Current Lighting Mode: " + smartLight.getLightingMode());

        System.out.println(smartLight.getLumenOutput(null));

        SmartHome smartHome = new SmartHome(1, "My Home");
        smartHome.addDevice(vacuumCleaner);
        smartHome.addDevice(smartLight);

        System.out.println("Number of smart devices: " + smartHome.getDevices().size());

        smartHome.removeDevice(vacuumCleaner);
        System.out.println("Number of smart devices: " + smartHome.getDevices().size());
    }
}