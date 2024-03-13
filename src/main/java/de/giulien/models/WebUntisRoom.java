package de.giulien.models;

import de.giulien.WebUntis;

import java.util.HashMap;

public final class WebUntisRoom extends WebUntisBaseModel {

    public String Building;

    public WebUntisRoom(HashMap<String, String> map) {
        super(map);
        Building = map.get("building");
    }
    
}
