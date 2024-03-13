package de.giulien.models;

import de.giulien.interfaces.IWebUntisResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public final class WebUntisLesson implements IWebUntisResponse {
    public String Id;
    public String Date;
    public String StartTime;
    public String EndTime;
    public WebUntisBaseModel Teacher;
    public WebUntisBaseModel Klasse;
    public WebUntisBaseModel Room;
    public WebUntisBaseModel Su;
    public String ActivityType;
    public String SG;
    public String LsNumber;

    public WebUntisLesson(HashMap<String, String> map) {
        Id = map.get("id");
        Date = map.get("date");
        StartTime = map.get("startTime");
        EndTime = map.get("endTime");
        ActivityType = map.get("activityType");
        LsNumber = map.get("lsnumber");
        SG = map.get("sg");
        Map<String, String> klMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String[] klArr = map.get("kl").replace(" ","").replace("id", "").split(",");
        for(String v : klArr) {
            String[] s = v.split(":");
            if(s[0].isEmpty()) {
                klMap.put("id", s[1]);
            } else if (s.length <= 1) {
                klMap.put("id", s[0]);
            }else {
                klMap.put(s[0], s[1]);
            }
        }
        Map<String, String> teMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String[] teArr = map.get("te").replace(" ","").replace("id", "").split(",");
        for(String v : teArr) {
            String[] s = v.split(":");
            if(s[0].isEmpty()) {
                teMap.put("id", s[1]);
            } else if (s.length <= 1) {
                teMap.put("id", s[0]);
            }else {
                    teMap.put(s[0], s[1]);
                }
        }
        Map<String, String> roMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String[] roArr = map.get("ro").replace(" ","").replace("id", "").split(",");
        for(String v : roArr) {
            String[] s = v.split(":");
            if(s[0].isEmpty()) {
                roMap.put("id", s[1]);
            } else if (s.length <= 1) {
                roMap.put("id", s[0]);
            }else {
                roMap.put(s[0], s[1]);
            }
        }
        Map<String, String> suMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        String[] suArr = map.get("su").replace(" ","").replace("id", "").split(",");
        for(String v : suArr) {
            String[] s = v.split(":");
            if(s[0].isEmpty()) {
                suMap.put("id", s[1]);
            } else if (s.length <= 1) {
                roMap.put("id", s[0]);
            }else {
                suMap.put(s[0], s[1]);
            }
        }
        Klasse = new WebUntisBaseModel(klMap);
        Teacher = new WebUntisBaseModel(teMap);
        Room = new WebUntisBaseModel(roMap);
        Su = new WebUntisBaseModel(suMap);

        System.out.println(map);
    }

    @Override
    public String toString() {
        return String.format("{\nId:%s\nDate:%s\nStartTime:%s\nEndTime:%s\nTeacher:%s\nRoom:%s\nActivityType:%s\n}",Id,Date,StartTime,EndTime,Teacher.LongName,Room.LongName,ActivityType);
    }
}
