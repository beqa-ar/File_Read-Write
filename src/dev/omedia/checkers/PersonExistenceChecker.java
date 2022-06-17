package dev.omedia.checkers;

import dev.omedia.exceptions.FormatException;
import dev.omedia.exceptions.NoSuchPersonException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class PersonExistenceChecker {
    public static void checkIfPersonExists(String documentNumber,String crossingDate) throws NoSuchPersonException{

        File file = new File("person.csv");
        boolean exists = false;
        try {
            exists=Files.readAllLines(Paths.get(file.toURI()))
                    .stream()
                    .map(l -> {
                        String[] line = l.split(",");
                        try {
                            checkFormats(line, crossingDate);
                        } catch (FormatException e) {
                            try (OutputStream outputStream = Files.newOutputStream(Paths.get("error.log"), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
                                String info= file.toString()+" "+UUID.randomUUID() +" "+ e.getMessage();
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
            throw new NoSuchPersonException("person does not exists\n");
        }
    }

    private static void checkFormats(String[] line,String crossingDate) throws FormatException {
        LineChecker.checkPersonFileLineFormat(line);
        PersonDocumentNumberChecker.CheckPersonDocumentNumberFormat(line[0]);
        PersonNameChecker.checkPersonNameFormat(line[1]);
        DateChecker.checkBirthDateFormat(line[2]);
        DateChecker.checkPersonAgeWhileBorderCrossing(line[2],crossingDate);
    }
}
