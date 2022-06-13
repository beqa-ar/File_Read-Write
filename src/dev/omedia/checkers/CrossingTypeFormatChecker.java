package dev.omedia.checkers;

import dev.omedia.exceptions.CrossingTypeFormatException;

public class CrossingTypeFormatChecker {
    private final static String TYPE_FORMAT = "AIR|LAND";

    public static void checkCrossingType(String type) throws CrossingTypeFormatException {
        if (!type.matches(TYPE_FORMAT)) {
            throw new CrossingTypeFormatException();
        }
    }
}
