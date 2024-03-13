package de.giulien.models;

public class WebUntisConfig {
    public String School;
    public String Server;
    public String Username;
    public String Password;
    public boolean UseNRWClasses;

    public WebUntisConfig(String server, String school, String username, String password) {
        School = school;
        Server = server;
        Username = username;
        Password = password;
        UseNRWClasses = false;
    }

    public WebUntisConfig(String server, String school, String username, String password, boolean useNRWClasses) {
        School = school;
        Server = server;
        Username = username;
        Password = password;
        UseNRWClasses = false;
    }
}
