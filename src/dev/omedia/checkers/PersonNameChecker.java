package dev.omedia.checkers;

import dev.omedia.exceptions.PersonNameFormatException;

public class PersonNameChecker {
private final static String NAME_FORMAT="[a-zA-Z]+[\\s][a-zA-Z]+|[ა-ჰ]+[\\s][ა-ჰ]+";

    public static void checkPersonNameFormat(String name) throws PersonNameFormatException {
        if(!name.matches(NAME_FORMAT)){
            throw new PersonNameFormatException("incorrect person name format\n");
        }
    }
}
