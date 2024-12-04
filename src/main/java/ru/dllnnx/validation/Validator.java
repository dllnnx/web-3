package ru.dllnnx.validation;

public class Validator {
    public static boolean checkArea(float x, float y, float r){
        if (x < 0 && y > 0 && x >= -r && y <= r/2) return true;
        if (x < 0 && y < 0 && y >= - x - r) return true;
        if (x > 0 && y < 0 && x*x + y*y <= r*r) return true;
        if (y == 0 && (x >= -r || x <= r)) return true;
        return x == 0 && (y <= r / 2 && y >= -r);
    }
}
