package dev.omedia.checkers;

import dev.omedia.exceptions.DirectionTypeFormatException;

public class DirectionTypeFormatChecker {
    private static final String TYPE_FORMAT="IN|OUT";

    public static void checkDirectionType(String type) throws DirectionTypeFormatException {
        if(!type.matches(TYPE_FORMAT)){
            throw new DirectionTypeFormatException();
        }
    }
}
