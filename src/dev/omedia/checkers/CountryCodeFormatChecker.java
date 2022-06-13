package dev.omedia.checkers;

import dev.omedia.exceptions.CountryCodeFormatException;

public class CountryCodeFormatChecker {
    private static final String COUNTRY_CODE_FORMAT="[A-Z]{3}";

    public void checkCountryFormat(String countryCode) throws CountryCodeFormatException {
        if(!countryCode.matches(COUNTRY_CODE_FORMAT)){
            throw new CountryCodeFormatException();
        }
    }
}
