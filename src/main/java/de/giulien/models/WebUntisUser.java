package de.giulien.models;

import de.giulien.interfaces.IWebUntisResponse;

import java.util.HashMap;

public final class WebUntisUser implements IWebUntisResponse {
    public String PersonId;
    public String KlassenId;
    public int PersonType;
    public String SessionId;

    public WebUntisUser(HashMap<String, String> map) {
        PersonId = map.get("personId");
        KlassenId = map.get("klassenId");
        SessionId = map.get("sessionId");
        PersonType = Integer.valueOf(map.get("personType"));
    }

    public WebUntisUser(){}
}
