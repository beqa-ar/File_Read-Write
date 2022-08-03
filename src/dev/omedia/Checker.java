package dev.omedia;


import dev.omedia.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.UUID;

public class Checker {
    private final static String BORDER_CROSSING_DATE_FORMAT = "\\d{4}-\\d{2}-\\d{2}\s\\d{2}:\\d{2}";
    private final static String NAME_FORMAT = "[a-zA-Z]+\s[a-zA-Z]+|[ა-ჰ]+\s[ა-ჰ]+";
    private final static String BIRTH_DATE_FORMAT = "\\d{4}-\\d{2}-\\d{2}";
    private final static String DESTINATION_TYPE_FORMAT = "IN|OUT";
    private final static String CROSSING_TYPE_FORMAT = "AIR|LAND";
    private final static String COUNTRY_CODE_FORMAT = "[A-Z]{3}";
    private final static String ID_PATTERN = "\\d+";
    private final static int PASSPORT_NUMBER_LENGTH = 9;
    private final static int ID_LENGTH = 11;

    public static void checkPersonNameFormat(String name) throws PersonNameFormatException {
        if (!name.matches(NAME_FORMAT)) {
            throw new PersonNameFormatException("incorrect person name format: " + name);
        }
    }

    public static void CheckPersonDocumentNumberFormat(String documentNumber) throws PersonDocumentNumberFormatException {
        int length = documentNumber.length();
        switch (length) {
            case ID_LENGTH:
                if (!documentNumber.matches(ID_PATTERN)) {
                    throw new PersonDocumentNumberFormatException("incorrect document number format");
                }
                break;
            case PASSPORT_NUMBER_LENGTH:
                if (!matchesPassportFormat(documentNumber)) {
                    throw new PersonDocumentNumberFormatException("incorrect document number format");
                }
            default:
                throw new PersonDocumentNumberFormatException("incorrect document number format");
        }
    }

    public static void checkIfPersonExists(String documentNumber, String crossingDate) throws NoSuchPersonException {

        File file = new File("person.csv");
        boolean exists;
        try {
            exists = Files.readAllLines(Paths.get(file.toURI()))
                    .stream()
                    .map(l -> {
                        String[] line = l.split(",");
                        try {
                            checkFormats(line, crossingDate);
                        } catch (FormatException e) {
                            try (OutputStream outputStream = Files.newOutputStream(Paths.get("error.log"), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
                                String info = file + " " + UUID.randomUUID() + " " + e.getMessage();
                                outputStream.write(info.getBytes());
                            } catch (IOException ee) {
                                throw new RuntimeException(ee);
                            }
                        }
                        return line[0];
                    }).anyMatch(p -> p.equals(documentNumber));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!exists) {
            throw new NoSuchPersonException("person does not exists");
        }
    }


    public static void checkPersonFileLineFormat(String[] line) throws LineFormatException {
        if (line.length != 3) {
            throw new LineFormatException("line must contain 3 words not->" + line.length);
        }
    }


    public static void checkBorderCrossFileLineFormat(String[] line) throws LineFormatException {
        if (line.length != 7) {
            throw new LineFormatException("line must contain 7 words not->" + line.length);
        }
    }

    public static void checkDestinationType(String type) throws DestinationTypeFormatException {
        if (!type.matches(DESTINATION_TYPE_FORMAT)) {
            throw new DestinationTypeFormatException("incorrect destination type format");
        }
    }

    public static void checkBirthDateFormat(String date) throws DateFormatException {
        if (!date.matches(BIRTH_DATE_FORMAT)) {
            throw new DateFormatException("incorrect birth date format");
        }
        String[] parts = date.split("-");
        if (Integer.parseInt(parts[0]) > LocalDate.now().getYear() || Integer.parseInt(parts[1]) > 12 || Integer.parseInt(parts[2]) > 31) {
            throw new DateFormatException("illogical date");
        }
        if (Integer.parseInt(parts[1]) == 0 || Integer.parseInt(parts[2]) == 0) {
            throw new DateFormatException("illogical date");
        }
    }

    public static void checkBorderCrossingDateFormat(String date) throws DateFormatException {
        if (!date.matches(BORDER_CROSSING_DATE_FORMAT)) {
            throw new DateFormatException("incorrect border crossing date format");
        }
        String[] parts = date.split("[-:\\s]");
        if (checkFutureDate(parts)) {
            throw new DateFormatException("future date");
        }
        if (Integer.parseInt(parts[1]) > 12 || Integer.parseInt(parts[2]) > 31 || Integer.parseInt(parts[3]) > 23 || Integer.parseInt(parts[4]) > 59) {
            throw new DateFormatException("illogical date");
        }
        if (Integer.parseInt(parts[1]) == 0 || Integer.parseInt(parts[2]) == 0) {
            throw new DateFormatException("illogical date");
        }
    }

    public static void checkPersonAgeWhileBorderCrossing(String birthDate, String crossingDate) throws
            DateFormatException {
        String[] crossingDateParts = crossingDate.split("[-:\\s]");
        String[] birthDateParts = birthDate.split("-");
        if (Integer.parseInt(crossingDateParts[0]) < Integer.parseInt(birthDateParts[0])) {
            throw new DateFormatException("not born yet");
        }
        if (Integer.parseInt(crossingDateParts[0]) == Integer.parseInt(birthDateParts[0])) {
            if (Integer.parseInt(crossingDateParts[1]) < Integer.parseInt(birthDateParts[1])) {
                throw new DateFormatException("not born yet");
            } else if (Integer.parseInt(crossingDateParts[1]) == Integer.parseInt(birthDateParts[1])) {
                if (Integer.parseInt(crossingDateParts[2]) < Integer.parseInt(birthDateParts[2])) {
                    throw new DateFormatException("not born yet");
                }
            }
        }

    }

    private static boolean checkFutureDate(String[] date) {
        if (Integer.parseInt(date[0]) > LocalDate.now().getYear()) {
            return true;
        }
        if (Integer.parseInt(date[0]) == LocalDate.now().getYear() && Integer.parseInt(date[1]) > LocalDate.now().getMonthValue()) {
            return true;
        }
        return Integer.parseInt(date[0]) == LocalDate.now().getYear() && Integer.parseInt(date[1]) == LocalDate.now().getMonthValue() && Integer.parseInt(date[2]) > LocalDate.now().getDayOfMonth();
    }

    public static void checkCrossingType(String type) throws CrossingTypeFormatException {
        if (!type.matches(CROSSING_TYPE_FORMAT)) {
            throw new CrossingTypeFormatException("incorrect crossing type format");
        }
    }

    public static void checkCountryFormat(String countryCode) throws CountryCodeFormatException {
        if (!countryCode.matches(COUNTRY_CODE_FORMAT)) {
            throw new CountryCodeFormatException("incorrect country code format");
        }
    }

    public static void checkIfGeorgiaNeighbour(String countryCode) throws IllegalDestinationException {
        switch (countryCode) {
            case "ARM":
            case "AZE":
            case "TUR":
            case "RUS":
                break;
            default:
                throw new IllegalDestinationException("can not go to " + countryCode + "from geo with land");
        }
    }

    public static void checkIfArriveInGeorgia(String countryCode) throws IllegalDestinationException {
        if (!countryCode.equals("GEO")) {
            throw new IllegalDestinationException("you arrive in geo! not: " + countryCode);
        }
    }

    private static boolean matchesPassportFormat(String passportNumber) {
        String chars = passportNumber.substring(0, 3);
        String numbers = passportNumber.substring(3);
        if (!chars.matches("[A-Z]+")) {
            return false;
        }
        return numbers.matches("\\d+");
    }

    private static void checkFormats(String[] line, String crossingDate) throws FormatException {
        checkPersonFileLineFormat(line);
        CheckPersonDocumentNumberFormat(line[0]);
        checkPersonNameFormat(line[1]);
        checkBirthDateFormat(line[2]);
        checkPersonAgeWhileBorderCrossing(line[2], crossingDate);
    }
}
