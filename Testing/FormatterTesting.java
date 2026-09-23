package Testing;
import Table_Classes.Schedules;
import Utilities.ConsoleFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/* 
  
  NOTE: This main class was generated with AI assistance (Gemini) strictly 
  for unit testing and verifying.
 
 logic and utilities were implemented independently
 */
public class FormatterTesting {

public static void main(String[] args) {

        // Target date for weekly schedule test (Wednesday, Sep 23, 2026)
        LocalDateTime targetDate = LocalDateTime.of(2026, 9, 23, 0, 0);

        // Mock data sorted by startTime ASC
        List<Schedules> mockShifts = new ArrayList<>();

        // Monday (Sep 21, 2026) - 1 Shift
        mockShifts.add(new Schedules(
            101, 
            LocalDateTime.of(2026, 9, 21, 8, 0), 
            LocalDateTime.of(2026, 9, 21, 16, 0), 
            5
        ));

        // Tuesday (Sep 22, 2026) - 2 Shifts (Multi-shift test)
        mockShifts.add(new Schedules(
            102, 
            LocalDateTime.of(2026, 9, 22, 8, 0), 
            LocalDateTime.of(2026, 9, 22, 16, 0), 
            5
        ));
        mockShifts.add(new Schedules(
            103, 
            LocalDateTime.of(2026, 9, 22, 16, 0), 
            LocalDateTime.of(2026, 9, 23, 0, 0), 
            6
        ));

        // Thursday (Sep 24, 2026) - 1 Shift
        mockShifts.add(new Schedules(
            101, 
            LocalDateTime.of(2026, 9, 24, 12, 0), 
            LocalDateTime.of(2026, 9, 24, 20, 0), 
            5
        ));

        // Next Week: Wednesday (Sep 30, 2026) - 1 Shift
        mockShifts.add(new Schedules(
            101, 
            LocalDateTime.of(2026, 9, 30, 8, 0), 
            LocalDateTime.of(2026, 9, 30, 16, 0), 
            5
        ));

        // ---------------------------------------------------------------------
        // 1. TEST: Weekly Schedule View with Dynamic Dates
        // ---------------------------------------------------------------------
        ConsoleFormatter.printHeader("WEEKLY SCHEDULE VIEW");
        
        // Compute Monday for the target week so header shows MON (21) ... SUN (27)
        LocalDateTime targetMonday = ConsoleFormatter.getPreviousOrCurrentMonday(targetDate);
        ConsoleFormatter.printWeeklyDays(targetMonday);
        ConsoleFormatter.printDivider();
        ConsoleFormatter.printWeeklySchedule(mockShifts, targetDate);
        ConsoleFormatter.printDivider();

        // ---------------------------------------------------------------------
        // 2. TEST: Monthly Schedule View with Dynamic Dates & Weekly Dividers
        // ---------------------------------------------------------------------
        ConsoleFormatter.printHeader("MONTHLY SCHEDULE VIEW (SEPTEMBER 2026)");
        ConsoleFormatter.printMonthlySchedule(mockShifts, 2026, 9);
    }
}

