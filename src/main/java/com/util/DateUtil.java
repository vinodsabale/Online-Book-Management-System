package com.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    private static final double FINE_PER_DAY = 5.0; // ₹5 per day

    public static double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) return 0.0;
        long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        return overdueDays * FINE_PER_DAY;
    }

    public static long daysOverdue(LocalDate dueDate) {
        if (!isOverdue(dueDate)) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    public static boolean isOverdue(LocalDate dueDate) {
        return LocalDate.now().isAfter(dueDate);
    }
}