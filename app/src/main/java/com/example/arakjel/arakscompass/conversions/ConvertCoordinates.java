package com.example.arakjel.arakscompass.conversions;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class ConvertCoordinates {

    public String convert(double value) {

        String convertToString = String.valueOf(setPrecision(value));
        String insertDegreeSign = convertToString.replaceAll("\\.", "°");

        int lengthToAddApostroph = splitToDegreeSymbol(insertDegreeSign)
                .length() + 3;

        StringBuilder convertedString = new StringBuilder(insertDegreeSign);
        convertedString.insert(lengthToAddApostroph, "'");

        return convertedString.toString();
    }

    private double setPrecision(double value) {
        Double toBeTruncated = new Double(value);
        Double truncatedDouble = BigDecimal.valueOf(toBeTruncated)
                .setScale(6, RoundingMode.HALF_UP)
                .doubleValue();
        return truncatedDouble;
    }

    private String splitToDegreeSymbol(String str) {
        String[] arr = str.split("°");
        return arr[0];
    }
}
