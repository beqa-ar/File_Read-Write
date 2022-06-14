package dev.omedia;

import dev.omedia.enums.CrossingType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MostVisitedStatisticMaker {
   HashMap<String,Country> countries;
    public MostVisitedStatisticMaker(){
        countries=new HashMap<>();
    }
    public void update(String country,CrossingType type, int age) {;
        if(countries.containsKey(country)){
            if(CrossingType.AIR.equals(type)){
            countries.get(country).visitWithAir();
        }else{
                countries.get(country).visitWithLand();
            }
            }
    }
}
