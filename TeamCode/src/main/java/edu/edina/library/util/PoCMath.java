package edu.edina.library.util;

public class PoCMath {
    public static boolean between(int value, int lowerLimit, int upperlimit) {
        if ((value >= lowerLimit) && (value <= upperlimit)) {
            return true;
        } else {
            return false;
        }
    }
}
