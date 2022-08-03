package dev.omedia;

import dev.omedia.enums.DestinationType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PersonStats {
    private final String documentNumber;
    private DestinationType destinationType;
    private String crossingDate;
    private int age;

    public PersonStats(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setDestinationType(DestinationType destinationType) {
        this.destinationType = destinationType;
    }

    public DestinationType getDestinationType() {
        return destinationType;
    }

    public int getAge() {
        return age;
    }

    public String getCrossingDate() {
        return crossingDate;
    }

    public void setCrossingDate(String crossingDate) {
        this.crossingDate = crossingDate;
        this.age = setAge();
    }

    private int setAge() {
        File file = new File("person.csv");
        try {
            String birthDate = Files.readAllLines(Paths.get(file.toURI()))
                    .stream()
                    .map((l) -> {
                        String[] line = l.split(",");
                        String number = line[0];
                        if (number.equals(documentNumber)) {
                            return line[2];
                        }
                        return "-1";
                    })
                    .filter(l -> !l.equals("-1"))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);
            LocalDate birth = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate cross = LocalDate.parse(crossingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            return Period.between(birth, cross).getYears();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
