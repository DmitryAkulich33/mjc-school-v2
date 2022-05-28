package com.epam.esm.util;

public class OffsetCalculator {
    public static Integer calculateOffset(Integer pageNumber, Integer pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
