package dev.omedia.checkers;

import dev.omedia.exceptions.DestinationTypeFormatException;

public class DestinationTypeChecker {
    private static final String TYPE_FORMAT="IN|OUT";

    public static void checkDirectionType(String type) throws DestinationTypeFormatException {
        if(!type.matches(TYPE_FORMAT)){
            throw new DestinationTypeFormatException("incorrect destination type format");
        }
    }
}
