package com.dellnaresh.com.dellnaresh.util;

/**
 * Created by NARESHM on 23/03/2015.
 */
public class Utility {
    public static double round(double value) {

        long factor = (long) Math.pow(10, 3);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
