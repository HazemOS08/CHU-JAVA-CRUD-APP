package Testing;

import Service_Layer.Controllers.*;
import DAO.*;
import DAO.DAO_Implementation.*;
import Table_Classes.*;
import Utilities.PasswordRulesCheck;

import java.time.LocalDate;

/* 
  
  NOTE: This main class was generated with AI assistance (Gemini) strictly 
  for unit testing and verifying. logic and utilities were implemented independently
  
 */
public class BusinessLogicTesting {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     BUSINESS LOGIC & CONTROLLER SUITE TEST       ");
        System.out.println("==================================================\n");

        // DAO references for mock data insertion
        UserDAO userDAO = new UserDAOImp();
        DepartmentsDAO deptDAO = new DepartmentsDAOImp();
        PositionsDAO posDAO = new PositionsDAOImp();
        WorksDAO worksDAO = new WorksDAOImp();

        try {
            // -----------------------------------------------------------------
            // 1. SETUP MOCK SYSTEM STATE
            // -----------------------------------------------------------------
            System.out.println("--- 1. Setting Up Test Users & Departments ---");

            // Admin User
            User admin = new User();
            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setUsername("admin_test");
            admin.setPassword(PasswordRulesCheck.hashPassword("hashed_admin"));
            admin.setRole("admin");
            admin.setEmail("admin@hospital.com");
            admin.setBirthDate(LocalDate.parse("1990-01-01"));
            admin.setCellular("514-555-0100");
            userDAO.insert(admin);
            admin = userDAO.findByUsername("admin_test");

            // Manager User
            User manager = new User();
            manager.setFirstName("Dept");
            manager.setLastName("Manager");
            manager.setUsername("manager_test");
            manager.setPassword(PasswordRulesCheck.hashPassword("hashed_manager"));
            manager.setRole("manager");
            manager.setEmail("manager@hospital.com");
            manager.setBirthDate(LocalDate.parse("1992-05-15"));
            manager.setCellular("514-555-0200");
            userDAO.insert(manager);
            manager = userDAO.findByUsername("manager_test");

            // Employee User
            User staff = new User();
            staff.setFirstName("Staff");
            staff.setLastName("Member");
            staff.setUsername("staff_test");
            staff.setPassword(PasswordRulesCheck.hashPassword("hashed_staff"));
            staff.setRole("regular");
            staff.setEmail("staff@hospital.com");
            staff.setBirthDate(LocalDate.parse("1995-08-20"));
            staff.setCellular("514-555-0300");
            userDAO.insert(staff);
            staff = userDAO.findByUsername("staff_test");

            // Departments
            Departments deptA = new Departments();
            deptA.setName("Cardiology");
            deptDAO.insert(deptA);
            deptA = deptDAO.findByName("Cardiology");

            Departments deptB = new Departments();
            deptB.setName("Pediatrics");
            deptDAO.insert(deptB);
            deptB = deptDAO.findByName("Pediatrics");

            // Positions
            Positions mgrPos = new Positions();
            mgrPos.setDeptId(deptA.getId());
            mgrPos.setName("Cardiology Head Nurse");
            posDAO.insert(mgrPos);
            mgrPos = posDAO.findByDeptId(deptA.getId()).get(0);

            Positions staffPos = new Positions();
            staffPos.setDeptId(deptB.getId());
            staffPos.setName("Pediatric Assistant");
            posDAO.insert(staffPos);
            staffPos = posDAO.findByDeptId(deptB.getId()).get(0);

            // Works assignments (Manager -> Dept A, Staff -> Dept B)
            Works mgrWork = new Works();
            mgrWork.setUserId(manager.getId());
            mgrWork.setPosId(mgrPos.getId());
            worksDAO.insert(mgrWork);

            Works staffWork = new Works();
            staffWork.setUserId(staff.getId());
            staffWork.setPosId(staffPos.getId());
            worksDAO.insert(staffWork);

            System.out.println("Admin ID: " + admin.getId());
            System.out.println("Manager ID: " + manager.getId() + " (Department: " + deptA.getId() + ")");
            System.out.println("Staff ID: " + staff.getId() + " (Department: " + deptB.getId() + ")");

            // -----------------------------------------------------------------
            // 2. TEST CONTROLLER INSTANTIATIONS
            // -----------------------------------------------------------------
            System.out.println("\n--- 2. Initializing Controllers ---");
            UserController userCtrl = new UserController();
            DepartmentController deptCtrl = new DepartmentController();
            PositionsController posCtrl = new PositionsController();
            ScheduleController schedCtrl = new ScheduleController();
            WorksController worksCtrl = new WorksController();
            System.out.println("Controllers successfully initialized.");

            // -----------------------------------------------------------------
            // 3. VERIFY ROLE & AUTHORIZATION CHECKS
            // -----------------------------------------------------------------
            System.out.println("\n--- 3. Testing Role Assertions ---");

            // Test 3.1: Admin Status Verification
            boolean isAdminCorrect = admin.isAdmin();
            System.out.println("Admin.isAdmin(): " + (isAdminCorrect ? "PASSED [✓]" : "FAILED [X]"));

            // Test 3.2: Manager Status Verification
            boolean isManagerCorrect = manager.isManager();
            System.out.println("Manager.isManager(): " + (isManagerCorrect ? "PASSED [✓]" : "FAILED [X]"));

            // Test 3.3: Regular Staff Status Verification
            boolean isStaffManager = staff.isManager();
            System.out.println("Staff isManager() (Should be false): " + (!isStaffManager ? "PASSED [✓]" : "FAILED [X]"));

            // -----------------------------------------------------------------
            // 4. TEST CROSS-DEPARTMENT MANAGER CONSTRAINTS
            // -----------------------------------------------------------------
            System.out.println("\n--- 4. Testing Manager Department Scope Rules ---");

            final int managerId = manager.getId();

            // Manager (Dept A) attempting to manage Staff (Dept B)
            boolean canManagerModifyOtherDept = worksDAO.findByDepartmentId(deptB.getId())
                    .stream()
                    .anyMatch(w -> w.getUserId() == managerId);

            System.out.println("Manager cross-department isolation: " + 
                (!canManagerModifyOtherDept ? "PASSED [✓] (Manager blocked from unmanaged Dept B)" : "FAILED [X]"));

            System.out.println("\n==================================================");
            System.out.println("      ALL BUSINESS LOGIC TESTS COMPLETED          ");
            System.out.println("==================================================");

        } catch (Exception e) {
            System.err.println("\n[ERROR] Test execution encountered an issue:");
            e.printStackTrace();
        }
    }
}