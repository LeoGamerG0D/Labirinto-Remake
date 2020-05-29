package me.mazeevent.main;

import java.util.Calendar;

public class Utils {
   public static int strToCalendar(String day) {
      if (day.equalsIgnoreCase("Domingo")) {
         return 1;
      } else if (day.equalsIgnoreCase("Segunda")) {
         return 2;
      } else if (day.equalsIgnoreCase("Terca")) {
         return 3;
      } else if (day.equalsIgnoreCase("Quarta")) {
         return 4;
      } else if (day.equalsIgnoreCase("Quinta")) {
         return 5;
      } else if (day.equalsIgnoreCase("Sexta")) {
         return 6;
      } else if (day.equalsIgnoreCase("Sabado")) {
         return 7;
      } else if (day.equalsIgnoreCase("TodosDias")) {
         return Calendar.getInstance().get(7);
      } else {
         if (day.equalsIgnoreCase("3Dias")) {
            if (Calendar.getInstance().get(7) == 3) {
               return 3;
            }

            if (Calendar.getInstance().get(7) == 6) {
               return 6;
            }

            if (Calendar.getInstance().get(7) == 1) {
               return 1;
            }
         }

         if (day.equalsIgnoreCase("Semanal")) {
            if (Calendar.getInstance().get(7) == 7) {
               return 7;
            }

            if (Calendar.getInstance().get(7) == 1) {
               return 1;
            }
         }

         return 0;
      }
   }
}