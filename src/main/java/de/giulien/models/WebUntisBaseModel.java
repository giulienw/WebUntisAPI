package de.giulien.models;

import de.giulien.interfaces.IWebUntisResponse;

import java.util.Map;

public class WebUntisBaseModel implements IWebUntisResponse {
    public final String Id;
    public String Name;
    public String LongName;
    public boolean Active;

    public WebUntisBaseModel(Map<String, String> map) {
        Id = map.get("id");
        Name = map.get("name");
        LongName = map.get("longName");
        if(map.containsKey("active")) {
            Active = Boolean.parseBoolean(map.get("active"));
        }
    }

    public WebUntisBaseModel(String id, String name, String longName, String active) {
        Id = id;
        Name = name;
        LongName = longName;
        Active = Boolean.parseBoolean(active);
    }

    @Override
    public String toString() {
        return String.format("{\nName:%s\nLongName:%s\n}",Name, LongName);
    }
}
