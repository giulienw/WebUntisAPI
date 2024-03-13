package de.giulien.models;

import de.giulien.interfaces.IWebUntisResponse;

public final class WebUntisTimeUnit implements IWebUntisResponse {
    public String Name;
    public String StartDate;
    public String EndDate;

    public WebUntisTimeUnit(String name, String startDate, String endDate) {
        Name = name;
        StartDate = startDate;
        EndDate = endDate;
    }
}
