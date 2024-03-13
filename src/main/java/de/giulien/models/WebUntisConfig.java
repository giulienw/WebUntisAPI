package de.giulien.models;

public class WebUntisConfig {
    public String School;
    public String Server;
    public String Username;
    public String Password;

    public WebUntisConfig(String server, String school, String username, String password) {
        School = school;
        Server = server;
        Username = username;
        Password = password;
    }

}
