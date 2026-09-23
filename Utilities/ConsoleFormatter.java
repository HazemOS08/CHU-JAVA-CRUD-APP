package Utilities;
import Table_Classes.Schedules;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import  java.util.List;
public class ConsoleFormatter {
    
    public static void printHeader(String title) {
        int totalWidth = 168;

        if (title == null) title = "";
        int padding = Math.max(0, (totalWidth - title.length()) / 2);

        System.out.println("\n" +  "=".repeat(totalWidth));

        System.out.println(" ".repeat(padding) + title);

        System.out.println("=".repeat(totalWidth));

    }

    public static void printDivider() {
        System.out.println("-".repeat(168));
    }
    
    public static void printWeeklyDays(LocalDateTime monday){

        if(monday==null){

                List<String> days = List.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

            for(String day : days){
             System.out.printf("%-22s |",day);     
            }
        }else{
            // dynamic headers with date numbers (e.g MON (21))
            for (int i = 0; i < 7; i++) {

                LocalDateTime dayDate = monday.plusDays(i);

                String label = String.format("%s (%d)", 

                dayDate.getDayOfWeek().toString().substring(0, 3), 

                dayDate.getDayOfMonth()

            );

            System.out.printf("%-22s |", label);

           }

        }
        

        System.out.println();
    }

    public static LocalDateTime getPreviousOrCurrentMonday(LocalDateTime targetDate) {

    if (targetDate == null) {

        return null;
    }

    LocalDateTime date = targetDate;

   
    while (date.getDayOfWeek() != DayOfWeek.MONDAY) {
        date = date.minusDays(1);
    }

    return date;
    }


    public static void printWeeklySchedule(List<Schedules> sch, LocalDateTime targetDate){


        
        if (sch == null || sch.isEmpty()) {
        System.out.println("No shifts scheduled for this week.");
        return;
        }

        //accepts any date, determines its week's monday and prints the (Mon-Sun)  schedule 
        LocalDateTime monday = getPreviousOrCurrentMonday(targetDate);

        //grouping shifts in one day lists (0 = Monday, 6 = Sunday) to handle multiple shifts per day
         List<List<Schedules>> week = new ArrayList<>();
         for (int i = 0; i < 7; i++) {

          week.add(new ArrayList<>());

         }
        
        // organizing shifts in a list for each day
        for(Schedules s : sch){
            for(int i=0;i<7;i++){
                if(s.getStartTime().toLocalDate().equals(monday.plusDays(i).toLocalDate())){
                    week.get(i).add(s);
                }
            }
        }


        int maxShifts = 0;

         for (List<Schedules> dayShifts : week) {

           maxShifts = Math.max(maxShifts, dayShifts.size());
           
         }

            if (maxShifts == 0) maxShifts = 1;
        
        
        // no need to sort as the fetched schedule grids are already sorted by the DAO SQL Query
        
        for(int row=0; row<maxShifts ; row++){
              for(int day=0;day<7;day++){
                List<Schedules> dayShifts = week.get(day);
                

                if (row<dayShifts.size()) {
                     System.out.print( dayShifts.get(row)+" |");
                }else{
                    
                    // prints off only on first row 
                    if(row==0 && dayShifts.isEmpty()) System.out.printf("%-22s |","OFF");
                    else System.out.printf("%-22s |","");
                }

              }

              System.out.println(); //end row
        }
       
        

    }

    public static void printMonthlySchedule(List<Schedules> sch, int year , int month){

      LocalDate firstOfMonth = LocalDate.of(year, month, 1);

      LocalDate lastOfMonth = firstOfMonth.withDayOfMonth(firstOfMonth.lengthOfMonth());

      //the monday corresponding to the start of the month's first week
      LocalDateTime currentMonday = getPreviousOrCurrentMonday(firstOfMonth.atStartOfDay());

      while (!currentMonday.toLocalDate().isAfter(lastOfMonth)) {
        
        printWeeklyDays(currentMonday);
        printDivider();


        printWeeklySchedule(sch, currentMonday);

        printDivider();
        System.out.println();

        currentMonday = currentMonday.plusWeeks(1);
      }
    }
}

