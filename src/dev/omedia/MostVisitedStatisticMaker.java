package dev.omedia;

import dev.omedia.enums.CrossingType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class MostVisitedStatisticMaker {
    private Map<String, CountryStats> to25;
    private Map<String, CountryStats> to40;
    private Map<String, CountryStats> to60;
    private Map<String, CountryStats> to80;
    private Map<String, CountryStats> above80;

    public MostVisitedStatisticMaker() {
        to25 = new HashMap<>();
        to40 = new HashMap<>();
        to60 = new HashMap<>();
        to80 = new HashMap<>();
        above80 = new HashMap<>();
    }

    public void update(int age, String countryCode, CrossingType crossingType) {
        if (age < 25) {
            updateMap(to25, countryCode, crossingType);
        } else if (age < 40) {
            updateMap(to40, countryCode, crossingType);
        } else if (age < 60) {
            updateMap(to60, countryCode, crossingType);
        } else if (age < 80) {
            updateMap(to80, countryCode, crossingType);
        } else {
            updateMap(above80, countryCode, crossingType);
        }

    }

    public void writeStatisticInFile(String path) {
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(path), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            outputStream.write("range,AIR,LAND".getBytes());
            String st = "\n0-25," + getMostVisitedCountryByAir(to25) + "," + getMostVisitedCountryByLand(to25) +
                    "\n25-40," + getMostVisitedCountryByAir(to40) + "," + getMostVisitedCountryByLand(to40) +
                    "\n40-60," + getMostVisitedCountryByAir(to60) + "," + getMostVisitedCountryByLand(to60) +
                    "\n60-80," + getMostVisitedCountryByAir(to80) + "," + getMostVisitedCountryByLand(to80) +
                    "\n80-xx," + getMostVisitedCountryByAir(above80) + "," + getMostVisitedCountryByLand(above80);
            outputStream.write(st.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMostVisitedCountryByAir(Map<String, CountryStats> map) {
        List<CountryStats> countries = new ArrayList<>(map.values().stream().toList());
        if (countries.size() > 0) {
            countries.sort(Comparator.comparingInt(CountryStats::getAir));
            return countries.get(0).getCountryCode();
        } else {
       return "null";
        }
    }
    private String getMostVisitedCountryByLand(Map<String, CountryStats> map) {
        List<CountryStats> countries = new ArrayList<>(map.values().stream().toList());
        if (countries.size() > 0) {
        countries.sort(Comparator.comparingInt(CountryStats::getLand));
        return countries.get(0).getCountryCode();
    } else {
        return "null";
    }
    }

    private void updateMap(Map<String, CountryStats> map, String countryCode, CrossingType crossingType) {
        if (map.containsKey(countryCode)) {
            if (crossingType.equals(CrossingType.AIR)) {
                map.get(countryCode).visitWithAir();
            } else {
                map.get(countryCode).visitWithLand();
            }
        } else {
            CountryStats cs = new CountryStats(countryCode);
            if (crossingType.equals(CrossingType.AIR)) {
                cs.visitWithAir();
                map.put(countryCode, cs);
            } else {
                cs.visitWithLand();
                map.put(countryCode, cs);
            }
        }
    }
}
