package com.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;

public class CommonUtil {

    public static String capitalize(String s) {
        if (s == null || s.isBlank()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    // Generate page numbers list for pagination UI
    public static List<Integer> getPageNumbers(Page<?> page) {
        int totalPages = page.getTotalPages();
        if (totalPages <= 0) return List.of();
        return IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
    }
}