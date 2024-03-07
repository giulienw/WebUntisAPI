package de.giulien.models;

import java.util.HashMap;

public class WebUntisRoom implements IWebUntisResponse{
    public String Id;
    public String Name;
    public String LongName;
    public boolean Active;
    public String Building;

    public WebUntisRoom(HashMap<String, String> map) {
        Id = map.get("id");
        Name = map.get("name");
        LongName = map.get("longName");
        Active = Boolean.parseBoolean(map.get("active"));
        Building = map.get("building");
    }
    
}
