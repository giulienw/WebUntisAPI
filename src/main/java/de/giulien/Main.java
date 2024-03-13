package de.giulien;

import java.time.LocalDate;
import java.util.ArrayList;

import de.giulien.models.*;

public class Main {
    public static void main(String[] args) {
        WebUntis untis = new WebUntis("Giuli.Chow","MXQz4#-t");
        WebUntisUser s = untis.Login();
        List<WebUntisLesson> timetable = untis.GetStundenplan(s.PersonId, LocalDate.of(2024,3, 14), LocalDate.of(2024,3, 14), ElementTyp.PERSON);
        List<WebUntisRoom> rooms = untis.GetRooms();
        timetable.toFirst();
        while(timetable.hasAccess()) {
            System.out.println(timetable.getContent().toString());
            timetable.next();
        }
    }
}