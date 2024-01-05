package edu.edina.library.util;

public class PoCMath {
    public static boolean between(int comparevalue, int lowvalue, int highvalue){
        if(comparevalue >= lowvalue && comparevalue <= highvalue){
            return true;
        }
        else{
            return false;
        }
    }
}
