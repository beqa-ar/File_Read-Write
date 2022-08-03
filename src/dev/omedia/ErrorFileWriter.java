package dev.omedia;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.UUID;

public class ErrorFileWriter {
    private static final String ERROR_FILE_PATH ="error.log";
    private static final String ILLEGAL_ENTRY_PATH="Culprit.csv";

    //file name where error occur
    public static void FormatErrorsWrite(String fileName, Exception e){
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(ERROR_FILE_PATH), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            String info = fileName + " " + UUID.randomUUID() + " " + e.getMessage()+"\n";
            outputStream.write(info.getBytes());
        } catch (IOException ee) {
            throw new RuntimeException(ee);
        }
    }
    public static void IllegalEntryWrite(Map<String, PersonStats> borderHistory,String[] entryInfo){
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(ILLEGAL_ENTRY_PATH),
                StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            String info = "From: " + borderHistory.get(entryInfo[2]).getCrossingDate() +
                    " to: " + entryInfo[4] + "," + entryInfo[1] +
                    "," + entryInfo[5] + "," + entryInfo[2]+"\n";
            outputStream.write(info.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
