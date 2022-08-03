package dev.omedia;


import dev.omedia.enums.CrossingType;
import dev.omedia.enums.DestinationType;
import dev.omedia.exceptions.FormatException;
import dev.omedia.exceptions.IllegalDestinationException;
import dev.omedia.exceptions.NoSuchPersonException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {


    public static void main(String[] args) {
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
                                    Checker.checkBorderInfoFormats(line);
                                    Checker.checkBorderInfoLogic(line);
                                    if (isIllegalEntry(border, line[2], getDestinationType(line[5]), line[4])) {
                                        ErrorFileWriter.IllegalEntryWrite(border, line);
                                    }
                                    int age = border.get(line[2]).getAge();
                                    CrossingType crossingType = getCrossingType(line[1]);
                                    ctsm.update(crossingType, age);
                                    mvsm.update(age, line[6], crossingType);

                                } catch (FormatException | NoSuchPersonException | IllegalDestinationException e) {
                                    ErrorFileWriter.FormatErrorsWrite(file.toString(), e);
                                }

                            });
            ctsm.writeStatisticInFile("StatiscicsCrossType.csv");
            mvsm.writeStatisticInFile("StatiscicsMostVisited.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static CrossingType getCrossingType(String type) {
        if (type.equals(CrossingType.AIR.toString())) {
            return CrossingType.AIR;
        } else {
            return CrossingType.LAND;
        }
    }

    private static DestinationType getDestinationType(String type) {
        if (type.equals(DestinationType.IN.toString())) {
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
