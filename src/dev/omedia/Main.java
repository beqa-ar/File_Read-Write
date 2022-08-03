package dev.omedia;


import dev.omedia.enums.CrossingType;
import dev.omedia.enums.DestinationType;
import dev.omedia.exceptions.FormatException;
import dev.omedia.exceptions.IllegalDestinationException;
import dev.omedia.exceptions.NoSuchPersonException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main {


    public static void main(String[] args)  {
        Map<String, PersonStats> border = new HashMap<>();
        try {
            CrossTypeStatisticMaker ctsm = new CrossTypeStatisticMaker();
            MostVisitedStatisticMaker mvsm = new MostVisitedStatisticMaker();
            File file = new File("bordercross.csv");
            Files.readAllLines(Paths.get(file.toURI()))
                    .forEach(
                            l -> {
                                try {
                                    String[] line = l.split(",");
                                    checkFormats(line);
                                    checkLogic(line);
                                    if (isIllegalEntry(border, line[2], getDestinationType(line[5]), line[4])) {
                                        try (OutputStream outputStream = Files.newOutputStream(Paths.get("Culprit.csv"), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
                                            String info = "From: " + border.get(line[2]).getCrossingDate() + " to: " + line[4] + "," + line[1] +
                                                    "," + line[5] + "," + line[2];
                                            outputStream.write(info.getBytes());
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    int age = border.get(line[2]).getAge();
                                    CrossingType crossingType = getCrossingType(line[1]);
                                    ctsm.update(crossingType, age);
                                    mvsm.update(age, line[6], crossingType);

                                } catch (FormatException | NoSuchPersonException | IllegalDestinationException e) {
                                    try (OutputStream outputStream = Files.newOutputStream(Paths.get("error.log"), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
                                        String info = file + " " + UUID.randomUUID() + e.getMessage();
                                        outputStream.write(info.getBytes());
                                    } catch (IOException ee) {
                                        throw new RuntimeException(ee);
                                    }
                                }

                            });
            ctsm.writeStatisticInFile("StatiscicsCrossType.csv");
            mvsm.writeStatisticInFile("StatiscicsMostVisited.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*checks border cross csv file line values format*/
    private static void checkFormats(String[] line) throws FormatException {
        Checker.checkBorderCrossFileLineFormat(line);
        Checker.checkCrossingType(line[1]);
        Checker.CheckPersonDocumentNumberFormat(line[2]);
        Checker.checkBorderCrossingDateFormat(line[4]);
        Checker.checkDestinationType(line[5]);
        Checker.checkCountryFormat(line[6]);
    }

    private static void checkLogic(String[] line) throws NoSuchPersonException, IllegalDestinationException {
        Checker.checkIfPersonExists(line[2], line[4]);
        if (line[3].equals("OUT") && line[1].equals("LAND")) {
            Checker.checkIfGeorgiaNeighbour(line[6]);
        }
        if (line[3].equals("IN")) {
            Checker.checkIfArriveInGeorgia(line[6]);
        }
    }

    private static CrossingType getCrossingType(String type) {
        if (type.equals("AIR")) {
            return CrossingType.AIR;
        } else {
            return CrossingType.LAND;
        }
    }

    private static DestinationType getDestinationType(String type) {
        if (type.equals("IN")) {
            return DestinationType.IN;
        } else {
            return DestinationType.OUT;
        }
    }

    private static boolean isIllegalEntry(Map<String, PersonStats> border, String documentNumber, DestinationType destinationType, String crossingDate) {
        if (border.containsKey(documentNumber)) {
            return border.get(documentNumber).getDestinationType().equals(destinationType);
        } else {
            PersonStats ps = new PersonStats(documentNumber);
            ps.setDestinationType(destinationType);
            ps.setCrossingDate(crossingDate);
            border.put(documentNumber, ps);
        }
        return false;
    }
}
