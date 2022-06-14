package dev.omedia;

import dev.omedia.checkers.*;
import dev.omedia.exceptions.FormatException;
import dev.omedia.exceptions.IllegalDestinationException;
import dev.omedia.exceptions.NoSuchPersonException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {


    public static void main(String[] args) throws Exception {
        try {
            File file = new File("bordercross.csv");
            Files.readAllLines(Paths.get(file.toURI()))
                    .stream()
                    .map(l -> {
                        String[] line = l.split(",");
                        try {
                            checkFormats(line);
                            checkLogic(line);
                        } catch (FormatException | NoSuchPersonException | IllegalDestinationException e) {
                            /**/
                        }
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*checks border cross csv file line values format*/
    private static void checkFormats(String[] line) throws FormatException {
        LineChecker.checkBorderCrossFileLineFormat(line);
        CrossingTypeChecker.checkCrossingType(line[1]);
        PersonDocumentNumberChecker.CheckPersonDocumentNumberFormat(line[2]);
        DateChecker.checkBorderCrossingDateFormat(line[4]);
        DestinationTypeChecker.checkDirectionType(line[5]);
        CountryCodeChecker.checkCountryFormat(line[6]);
    }

    private static void checkLogic(String[] line) throws NoSuchPersonException, IllegalDestinationException {
        PersonExistenceChecker.checkIfPersonExists(line[2], line[4]);
        if (line[3].equals("OUT") && line[1].equals("LAND")) {
            CountryCodeChecker.checkIfGeorgiaNeighbour(line[6]);
        }
        if(line[3].equals("IN")){
            CountryCodeChecker.checkIfArriveInGeorgia(line[6]);
        }
    }
}
