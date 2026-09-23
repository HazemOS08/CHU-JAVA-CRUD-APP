package Testing;
import DAO.*;
import DAO.DAO_Implementation.*;
import Table_Classes.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/* 
  
  NOTE: This main class was generated with AI assistance (Gemini) strictly 
  for unit testing and verifying. logic and utilities were implemented independently

  Delete the file hospital.db for the testing to work properly
 */
public class DaoTesting {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       HOSPITAL MANAGEMENT SYSTEM - DAO TEST      ");
        System.out.println("==================================================\n");

        // 1. Instantiate DAOs
        UserDAO userDAO = new UserDAOImp();
        DepartmentsDAO deptDAO = new DepartmentsDAOImp();
        PositionsDAO posDAO = new PositionsDAOImp();
        WorksDAO worksDAO = new WorksDAOImp();
        SchedulesDAO schedulesDAO = new SchedulesDAOImp();
        RequestTypesDAO requestTypesDAO = new RequestTypesDAOImp();
        RequestsDAO requestsDAO = new RequestsDAOImp();
        AvailabilityDAO availabilityDAO = new AvailabilityDAOImp();

        try {
            // 2. Test Users Table
            System.out.println("--- 1. Testing Users ---");
            User user = new User();
            user.setFirstName("Hazem");
            user.setLastName("Saad");
            user.setBirthDate(LocalDate.parse("2002-05-14")); // Matches CHECK("birth_date" LIKE '____-__-__')
            user.setRole("regular");        // Matches CHECK("role" IN ('admin', 'regular'))
            user.setPassword("hashed_pass_123");
            user.setUsername("h_saad");
            user.setEmail("hazem@example.com"); // Matches CHECK("email" LIKE '_%@_%._%')
            user.setCellular("514-555-0199");

            boolean userInserted = userDAO.insert(user);
            System.out.println("Insert User: " + (userInserted ? "SUCCESS" : "FAILED"));

            User fetchedUser = userDAO.findByUsername("h_saad");
            System.out.println("Fetched User: " + fetchedUser);


            // 3. Test Departments Table
            System.out.println("\n--- 2. Testing Departments ---");
            Departments dept = new Departments();
            dept.setName("Emergency");

            boolean deptInserted = deptDAO.insert(dept);
            System.out.println("Insert Department: " + (deptInserted ? "SUCCESS" : "FAILED"));

            Departments fetchedDept = deptDAO.findByName("Emergency");
            System.out.println("Fetched Department: " + fetchedDept);


            // 4. Test Positions Table (FK -> Departments.id)
            System.out.println("\n--- 3. Testing Positions ---");
            Positions pos = new Positions();
            pos.setDeptId(fetchedDept != null ? fetchedDept.getId() : 1);
            pos.setName("Triage Nurse");

            boolean posInserted = posDAO.insert(pos);
            System.out.println("Insert Position: " + (posInserted ? "SUCCESS" : "FAILED"));

            List<Positions> positions = posDAO.findAll();
            System.out.println("All Positions: " + positions);
            int posId = positions.isEmpty() ? 1 : positions.get(0).getId();


            // 5. Test Works Table (Composite PK: user_id, pos_id)
            System.out.println("\n--- 4. Testing Works ---");
            if (fetchedUser != null) {
                Works works = new Works();
                works.setUserId(fetchedUser.getId());
                works.setPosId(posId);

                boolean worksInserted = worksDAO.insert(works);
                System.out.println("Insert Works Assignment: " + (worksInserted ? "SUCCESS" : "FAILED"));

                List<Works> userWorks = worksDAO.findByUserId(fetchedUser.getId());
                System.out.println("User Works Entries: " + userWorks);
            }


            // 6. Test Availability Table (FK -> Users.id, CHECK is_active IN (1, 0))
            System.out.println("\n--- 5. Testing Availability ---");
            if (fetchedUser != null) {
                Availability avail = new Availability();
                avail.setUserId(fetchedUser.getId());
                avail.setLastUpdated(LocalDate.now());
                avail.setFrequency(5); // e.g., 5 days/week
                avail.setInfo("Prefers morning shifts");
                avail.setActive(true); // 1 for active

                boolean availInserted = availabilityDAO.insert(avail);
                System.out.println("Insert Availability: " + (availInserted ? "SUCCESS" : "FAILED"));

                Availability fetchedAvail = availabilityDAO.findByUserId(fetchedUser.getId());
                System.out.println("Fetched Availability: " + fetchedAvail);
            }


            // 7. Test Schedules Table (FK -> Works(user_id, pos_id), CHECK start_time < end_time)
            System.out.println("\n--- 6. Testing Schedules ---");
            if (fetchedUser != null) {
                Schedules schedule = new Schedules();
                schedule.setUserId(fetchedUser.getId());
                schedule.setPosId(posId);
                schedule.setStartTime(LocalDateTime.parse("2026-10-05T08:00"));
                schedule.setEndTime(LocalDateTime.parse("2026-10-05T16:00")); // Must be greater than start_time

                boolean schedInserted = schedulesDAO.insert(schedule);
                System.out.println("Insert Schedule: " + (schedInserted ? "SUCCESS" : "FAILED"));

                // Define weekly date range (Monday 00:00 to Sunday 23:59:59)
                LocalDate targetDate = LocalDate.parse("2026-10-05");
                LocalDate monday = targetDate;
                while (monday.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
                    monday = monday.minusDays(1);
                }
                LocalDateTime startOfWeek = monday.atStartOfDay();
                LocalDateTime endOfWeek = monday.plusDays(6).atTime(java.time.LocalTime.MAX);

                // Test 1: Fetch user schedules by range
                List<Schedules> userSchedules = schedulesDAO.findByUserIdAndRange(startOfWeek, endOfWeek, fetchedUser.getId());
                System.out.println("Fetched User Schedules (Range): " + userSchedules);

                // Test 2: Fetch department schedules by range
                if (fetchedDept != null) {
                    List<Schedules> deptSchedules = schedulesDAO.findByDepartmentIdAndRange(startOfWeek, endOfWeek, fetchedDept.getId());
                    System.out.println("Fetched Department Schedules (Range): " + deptSchedules);
                }
            }


            // 8. Test RequestTypes & Requests Tables
            System.out.println("\n--- 7. Testing RequestTypes & Requests ---");
            RequestTypes reqType = new RequestTypes();
            reqType.setType("Time Off");
            requestTypesDAO.insert(reqType);

            List<RequestTypes> allTypes = requestTypesDAO.findAll();
            int typeId = allTypes.isEmpty() ? 1 : allTypes.get(0).getId();

            if (fetchedUser != null) {
                Requests request = new Requests();
                request.setUserId(fetchedUser.getId());
                request.setTypeId(typeId);
                request.setMessage("Requesting annual leave for October");
                request.setDate(LocalDate.now());
                request.setStatus(false); // Matches CHECK("status" IN ('solved', 'pending'))

                boolean reqInserted = requestsDAO.insert(request);
                System.out.println("Insert Request: " + (reqInserted ? "SUCCESS" : "FAILED"));

                List<Requests> pendingRequests = requestsDAO.findByStatus("pending");
                System.out.println("Pending Requests: " + pendingRequests);
            }

            System.out.println("\n==================================================");
            System.out.println("         ALL DAO TESTS COMPLETED SUCCESSFULLY     ");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("\n[ERROR] Test execution failed:");
            e.printStackTrace();
        }
    }
}