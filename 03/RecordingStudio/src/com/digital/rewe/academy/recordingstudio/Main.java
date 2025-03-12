package com.digital.rewe.academy.recordingstudio;

import com.digital.rewe.academy.recordingstudio.exception.MaxHoursExceededException;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        RecordingStudio recordingStudio = new RecordingStudio(1, new BigDecimal("20"), 14);

        try {
            recordingStudio.setRentedHours(12);
        } catch (MaxHoursExceededException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(recordingStudio.getIncomeInBGN(LocalDate.now()));
        System.out.println(recordingStudio.getIncomeInEur(LocalDate.now()));


        RecordingStudio recordingStudio2 = new RecordingStudio(2, new BigDecimal("30"), 10);


        System.out.println(recordingStudio.compareTo(recordingStudio2));
    }
}