package dev.omedia;

public class CountryStats {
    private final String countryCode;
    private int air=0;
    private int land=0;

    public CountryStats(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getAir() {
        return air;
    }

    public int getLand() {
        return land;
    }

    public void visitWithAir(){
        air++;
    }
    public void visitWithLand(){
        land++;
    }
}
