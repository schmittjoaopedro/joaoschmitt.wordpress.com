package com.github.schmittjoaopedro;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Utils {

    public static int getNumMonthsAfterNumDays(int numberOfDays) {
        LocalDate now = LocalDate.now();
        LocalDate future = LocalDate.now().plusDays(numberOfDays);
        return (int) ChronoUnit.MONTHS.between(now, future);
    }

    public static int getNumDaysAfterNumMonths(int numberOfMonths) {
        LocalDate now = LocalDate.now();
        LocalDate future = LocalDate.now().plusMonths(numberOfMonths);
        return (int) ChronoUnit.DAYS.between(now, future);
    }

}
