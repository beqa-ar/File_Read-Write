package dev.omedia.checkers;

import dev.omedia.exceptions.DateFormatException;

import java.time.LocalDate;

public class DateChecker {
    private final static String BIRTH_DATE_FORMAT = "[0-9]{4}[\\-][0-9]{2}[\\-][0-9]{2}";
    private final static String BORDER_CROSSING_DATE_FORMAT = "[0-9]{4}[\\-][0-9]{2}[\\-][0-9]{2}[\\s][0-9]{2}[:][0-9]{2}";

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

    public static void checkPersonAgeWhileBorderCrossing(String birthDate, String crossingDate) throws DateFormatException {
        String[] crossingDateParts = crossingDate.split("[-:\\s]");
        String[] birthDateParts = birthDate.split("-");
        if (Integer.parseInt(crossingDateParts[0]) < Integer.parseInt(birthDateParts[0])) {
            throw new DateFormatException("not born yet ?????\n");
        }
        if (Integer.parseInt(crossingDateParts[0]) == Integer.parseInt(birthDateParts[0])) {
            if (Integer.parseInt(crossingDateParts[1]) < Integer.parseInt(birthDateParts[1])) {
                throw new DateFormatException("not born yet ?????\n");
            } else if (Integer.parseInt(crossingDateParts[1]) == Integer.parseInt(birthDateParts[1])) {
                if (Integer.parseInt(crossingDateParts[2]) < Integer.parseInt(birthDateParts[2])) {
                    throw new DateFormatException("not born yet ?????\n");
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
        if (Integer.parseInt(date[0]) == LocalDate.now().getYear() && Integer.parseInt(date[1]) == LocalDate.now().getMonthValue() && Integer.parseInt(date[2]) > LocalDate.now().getDayOfMonth()) {
            return true;
        }
        return false;
    }
}
