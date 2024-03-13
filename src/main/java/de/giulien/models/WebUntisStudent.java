package de.giulien.models;

import de.giulien.interfaces.IWebUntisResponse;

import java.util.Map;

public final class WebUntisStudent implements IWebUntisResponse {
    public String Id;
    public String Key;
    public String Name;
    public String ForeName;
    public String LastName;
    public String Gender;

    public WebUntisStudent(Map<String, String> map) {
        Id = map.get("id");
        Key = map.get("key");
        Name = map.get("name");
        ForeName = map.get("foreName");
        LastName = map.get("lastName");
        Gender = map.get("Gender");
    }
}
