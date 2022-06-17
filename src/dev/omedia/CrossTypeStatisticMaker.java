package dev.omedia;

import dev.omedia.enums.CrossingType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CrossTypeStatisticMaker {
    private static int[][] statistic;

    public CrossTypeStatisticMaker() {
        statistic = new int[2][5];
    }

    public void update(CrossingType type, int age) {
        statistic[getX(type)][getY(age)]++;
    }


    public  void writeStatisticInFile(String path) {
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(path), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            outputStream.write("range,AIR,LAND".getBytes());
            String st = "\n0-25," + getAt(0, 0) + "," + getAt(1, 0) +
                    "\n25-40," + getAt(0, 1) + "," + getAt(1, 1) +
                    "\n40-60," + getAt(0, 2) + "," + getAt(1, 2) +
                    "\n60-80," + getAt(0, 3) + "," + getAt(1, 3) +
                    "\n80-xx," + getAt(0, 4) + "," + getAt(1, 4);
            outputStream.write(st.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
 private int getX(CrossingType type){
        if(type.equals(CrossingType.AIR)){
            return 0;
        }else{
            return 1;
        }
 }
    private int getY(int age){
        if(age<25){
            return 0;
        } else if (age<40) {
            return 1;
        } else if (age<60) {
            return 2;
        } else if (age < 80) {
            return 3;
        }else {
            return 4;
        }

    }
    private static int getAt(int x, int y) {
        return statistic[x][y];
    }
}
