package de.giulien.models;

import java.util.HashMap;

public final class WebUntisKlasse extends WebUntisBaseModel{
    public String Teacher1;
    public String Teacher2;

    public WebUntisKlasse(HashMap<String,String> map) {
        super(map);
        Teacher1 = map.get("teacher1");
        Teacher2 = map.get("teacher2");
    }
}
