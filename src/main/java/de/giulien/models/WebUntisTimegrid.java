package de.giulien.models;

import de.giulien.interfaces.IWebUntisResponse;

import java.util.HashMap;

public final class WebUntisTimegrid implements IWebUntisResponse {
    public String Day;
    public WebUntisTimeUnit[] TimeUnits;

    public WebUntisTimegrid(HashMap<String, String> map) {
        Day = map.get("day");
        String[] timeUnitsArr = map.get("timeUnits").split(", name:");
        TimeUnits = new WebUntisTimeUnit[timeUnitsArr.length];
        for(int i = 0; i < timeUnitsArr.length; i++) {
            String[] current = timeUnitsArr[i].replace("name:","").replace("startTime:", "").replace("endTime:", "").split(",");
            TimeUnits[i] = new WebUntisTimeUnit(current[0],current[1], current[2]);
        }
    }
}
