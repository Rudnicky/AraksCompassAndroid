package com.example.arakjel.arakscompass.conversions;

/**
 * Created by Pawel on 2017-03-12.
 */

public class RoundNumber {

    public RoundNumber() {
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
