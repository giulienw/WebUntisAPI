package de.giulien;

import java.util.ArrayList;

import de.giulien.models.WebUntisRoom;
import de.giulien.models.WebUntisUser;

public class Main {
    public static void main(String[] args) {
        WebUntis untis = new WebUntis("Giuli.Chow","Sa_2018isbest");
        WebUntisUser s = untis.Login();
        ArrayList<WebUntisRoom> rooms = untis.GetRooms();

        System.out.println(rooms.get(0));
    }
}