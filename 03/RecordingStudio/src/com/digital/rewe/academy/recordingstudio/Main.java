package com.digital.rewe.academy.recordingstudio;

import com.digital.rewe.academy.recordingstudio.exception.MaxHoursExceededException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        try {
            RecordingStudio recordingStudio = new RecordingStudio(1, new BigDecimal("20"), 14);
            recordingStudio.setRentedHours(12);

            System.out.println("Income in BGN for today: " + recordingStudio.getIncomeInBGN(LocalDate.now()));
            System.out.println("Income in EUR for today: " +recordingStudio.getIncomeInEur(LocalDate.now()));


            RecordingStudio recordingStudio2 = new RecordingStudio(2, new BigDecimal("30"), 10);
            System.out.println("Income in BGN for today /Studio2/: " + recordingStudio2.getIncomeInBGN());
            System.out.println("Income in EUR for today /Studio2/: " + recordingStudio2.getIncomeInEur());

            recordingStudio2.setRentedHours(6);
            System.out.println("Income in BGN for today /Studio2/: " + recordingStudio2.getIncomeInBGN());
            System.out.println("Income in EUR for today /Studio2/: " + recordingStudio2.getIncomeInEur());

            System.out.println("Studio1 compare to Studio2: " + recordingStudio.compareTo(recordingStudio2));

            recordingStudio.setRentedHours(LocalDate.of(2025, 3, 13), 14);
            System.out.println("Income in BGN for tomorrow: " +recordingStudio.getIncomeInBGN(LocalDate.of(2025, 3, 13)));
            System.out.println("Income in EUR for tomorrow: " +recordingStudio.getIncomeInEur(LocalDate.of(2025, 3, 13)));

        } catch (MaxHoursExceededException e) {
            System.out.println(e.getMessage());
        }
    }
}