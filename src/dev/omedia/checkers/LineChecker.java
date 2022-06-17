package dev.omedia.checkers;

import dev.omedia.exceptions.LineFormatException;

public class LineChecker {

    public static void checkPersonFileLineFormat(String[] line) throws LineFormatException {
        if(line.length!=3){
            throw new LineFormatException("line must contain 3 words not->"+line.length+"\n");
        }
    }


    public static void checkBorderCrossFileLineFormat(String[] line) throws LineFormatException {
        if(line.length!=7){
            throw new LineFormatException("line must contain 7 words not->"+line.length+"\n");
        }
    }
}
