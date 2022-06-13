package dev.omedia.checkers;

import dev.omedia.exceptions.PersonDocumentNumberFormatException;

public class PersonDocumentNumberFormatChecker {
    private static final int ID_LENGTH = 11;
    private static final int PASSPORT_NUMBER_LENGTH = 9;
    private static final String ID_PATTERN="[0-9]+";

    public static void CheckPersonDocumentNumberFormat(String documentNumber) throws PersonDocumentNumberFormatException {
        int length = documentNumber.length();
        switch (length) {
            case ID_LENGTH:
                if (!documentNumber.matches(ID_PATTERN)) {
                    throw new PersonDocumentNumberFormatException();
                }
                break;
            case PASSPORT_NUMBER_LENGTH:
                if (!matchesPassportFormat(documentNumber)) {
                    throw new PersonDocumentNumberFormatException();
                }
            default:
                throw new PersonDocumentNumberFormatException();
        }

    }

    private static boolean matchesPassportFormat(String passportNumber)  {
        String chars=passportNumber.substring(0,3);
        String numbers=passportNumber.substring(3);
        if(!chars.matches("[A-Z]+")){
            return false;
        }
        return numbers.matches("[0-9]+");
    }
}


