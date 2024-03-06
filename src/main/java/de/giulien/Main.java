package de.giulien;

import de.giulien.models.WebUntisUser;

public class Main {
    public static void main(String[] args) {
        WebUntis untis = new WebUntis("Giuli.Chow","Sa_2018isbest");
        WebUntisUser s = untis.Login();

        System.out.println(untis.GetRooms());
    }
}