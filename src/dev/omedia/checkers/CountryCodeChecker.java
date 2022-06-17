package dev.omedia.checkers;

import dev.omedia.exceptions.CountryCodeFormatException;
import dev.omedia.exceptions.IllegalDestinationException;

public class CountryCodeChecker {
    private static final String COUNTRY_CODE_FORMAT = "[A-Z]{3}";

    public static void checkCountryFormat(String countryCode) throws CountryCodeFormatException {
        if (!countryCode.matches(COUNTRY_CODE_FORMAT)) {
            throw new CountryCodeFormatException("incorrect country code format\n");
        }
    }

    public static void checkIfGeorgiaNeighbour(String countryCode) throws IllegalDestinationException {
        switch (countryCode) {
            case "ARM":
            case "AZE":
            case "TUR":
            case "RUS":
                break;
            default :
                throw new IllegalDestinationException("can not go to "+countryCode+"from geo with land\n");
        }
    }
    public static void checkIfArriveInGeorgia(String countryCode) throws IllegalDestinationException {
        if(!countryCode.equals("GEO")){
            throw new IllegalDestinationException("you arrive in geo! not: "+countryCode+"\n");
        }
    }
}
