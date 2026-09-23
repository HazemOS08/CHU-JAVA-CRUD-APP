
INSERT INTO Departments (id, name) VALUES
(1, 'Emergency'),
(2, 'Pediatrics'),
(3, 'Radiology'),
(4, 'Cardiology'),
(5, 'Pharmacy');


INSERT INTO RequestTypes (id, type) VALUES
(1, 'Time Off'),
(2, 'Shift Swap'),
(3, 'Schedule Adjustment');


INSERT INTO Positions (id, dept_id, name) VALUES
(1, 1, 'ER Lead Nurse'),
(2, 1, 'Triage Specialist'),
(3, 2, 'Pediatric Nurse'),
(4, 2, 'Pediatric Assistant'),
(5, 3, 'Radiology Tech'),
(6, 3, 'MRI Specialist'),
(7, 4, 'Cardiac Specialist'),
(8, 4, 'ECG Tech'),
(9, 5, 'Staff Pharmacist'),
(10, 5, 'Pharmacy Tech');



INSERT INTO Users (id, first_name, last_name, birth_date, role, password, username, email, cellular) VALUES
(1, 'System', 'Admin', '1985-01-01', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin', 'admin@clinic.com', '5145550100');


INSERT INTO Users (id, first_name, last_name, birth_date, role, password, username, email, cellular) VALUES
(2, 'Sarah', 'Conner', '1988-04-12', 'manager', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'sconner', 's.conner@clinic.com', '5145550101'),
(3, 'Marcus', 'Vance', '1982-09-25', 'manager', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'mvance', 'm.vance@clinic.com', '5145550102'),
(4, 'Elena', 'Rostova', '1990-11-03', 'manager', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'erostova', 'e.rostova@clinic.com', '5145550103'),
(5, 'David', 'Kim', '1986-07-19', 'manager', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'dkim', 'd.kim@clinic.com', '5145550104'),
(6, 'Rachel', 'Green', '1991-02-14', 'manager', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'rgreen', 'r.green@clinic.com', '5145550105');


INSERT INTO Users (id, first_name, last_name, birth_date, role, password, username, email, cellular) VALUES
(7, 'Alex', 'Mercer', '1995-08-22', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'amercer', 'a.mercer@clinic.com', '5145550107'),
(8, 'James', 'Wilson', '1993-03-15', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'jwilson', 'j.wilson@clinic.com', '5145550108'),
(9, 'Maria', 'Garcia', '1997-12-01', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'mgarcia', 'm.garcia@clinic.com', '5145550109'),
(10, 'John', 'Doe', '1992-06-18', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'jdoe', 'j.doe@clinic.com', '5145550110'),
(11, 'Emily', 'Clark', '1996-01-30', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'eclark', 'e.clark@clinic.com', '5145550111'),
(12, 'Michael', 'Brown', '1994-09-10', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'mbrown', 'm.brown@clinic.com', '5145550112'),
(13, 'Jessica', 'Taylor', '1998-05-04', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'jtaylor', 'j.taylor@clinic.com', '5145550113'),
(14, 'Daniel', 'Anderson', '1991-10-21', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'danderson', 'd.anderson@clinic.com', '5145550114'),
(15, 'Sophia', 'Thomas', '1999-04-11', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'sthomas', 's.thomas@clinic.com', '5145550115'),
(16, 'Chris', 'Jackson', '1993-11-28', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'cjackson', 'c.jackson@clinic.com', '5145550116'),
(17, 'Laura', 'White', '1996-07-07', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'lwhite', 'l.white@clinic.com', '5145550117'),
(18, 'Kevin', 'Harris', '1990-02-17', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'kharris', 'k.harris@clinic.com', '5145550118'),
(19, 'Amanda', 'Martin', '1995-12-25', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'amartin', 'a.martin@clinic.com', '5145550119'),
(20, 'Brian', 'Thompson', '1992-08-08', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'bthompson', 'b.thompson@clinic.com', '5145550120'),
(21, 'Megan', 'Garcia', '1997-03-14', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'mgarcia2', 'm.garcia2@clinic.com', '5145550121'),
(22, 'Jason', 'Martinez', '1994-01-05', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'jmartinez', 'j.martinez@clinic.com', '5145550122'),
(23, 'Hannah', 'Robinson', '1998-09-19', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'hrobinson', 'h.robinson@clinic.com', '5145550123'),
(24, 'Eric', 'Clark', '1991-06-30', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'eclark2', 'e.clark2@clinic.com', '5145550124'),
(25, 'Olivia', 'Rodriguez', '1996-10-15', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'orodriguez', 'o.rodriguez@clinic.com', '5145550125'),
(26, 'Nathan', 'Lewis', '1993-04-22', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'nlewis', 'n.lewis@clinic.com', '5145550126'),
(27, 'Samantha', 'Lee', '1999-01-12', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'slee', 's.lee@clinic.com', '5145550127'),
(28, 'Tyler', 'Walker', '1995-05-18', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'twalker', 't.walker@clinic.com', '5145550128'),
(29, 'Chloe', 'Hall', '1997-08-09', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'chall', 'c.hall@clinic.com', '5145550129'),
(30, 'Brandon', 'Allen', '1992-12-03', 'regular', '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'ballen', 'b.allen@clinic.com', '5145550130');


INSERT INTO Works (user_id, pos_id) VALUES
-- Admin
(1, 1),

(2, 1), (2, 3), 
(3, 5),          
(4, 7),          
(5, 9),          
(6, 2),          

(7, 1), (8, 2), (9, 2), (10, 1), (11, 2),
(12, 3), (13, 4), (14, 3), (15, 4),
(16, 5), (17, 6), (18, 5), (19, 6),
(20, 7), (21, 8), (22, 7), (23, 8),
(24, 9), (25, 10), (26, 9), (27, 10),
(28, 1), (29, 3), (30, 5);


INSERT INTO Availability (last_updated, days_week, info, user_id, is_active) VALUES
('2026-09-01', 5, 'Morning preference', 1, 1),
('2026-09-01', 4, 'Full availability', 2, 1),
('2026-09-01', 3, 'Weekdays only', 3, 1),
('2026-09-01', 4, 'Flexible', 4, 1),
('2026-09-01', 3, 'No night shifts', 5, 1),
('2026-09-01', 4, 'Flexible', 6, 1),
('2026-09-01', 3, 'Weekend preference', 7, 1),
('2026-09-01', 2, 'Part-time', 8, 1),
('2026-09-01', 3, 'Full availability', 9, 1),
('2026-09-01', 3, 'Morning shifts only', 10, 1),
('2026-09-01', 2, 'Flexible', 11, 1),
('2026-09-01', 3, 'Flexible', 12, 1),
('2026-09-01', 3, 'Evenings preference', 13, 1),
('2026-09-01', 2, 'Flexible', 14, 1),
('2026-09-01', 3, 'Full availability', 15, 1),
('2026-09-01', 3, 'Weekdays only', 16, 1),
('2026-09-01', 2, 'Flexible', 17, 1),
('2026-09-01', 3, 'Flexible', 18, 1),
('2026-09-01', 3, 'Part-time', 19, 1),
('2026-09-01', 2, 'Morning shifts only', 20, 1),
('2026-09-01', 3, 'Flexible', 21, 1),
('2026-09-01', 3, 'Full availability', 22, 1),
('2026-09-01', 2, 'Weekends preference', 23, 1),
('2026-09-01', 3, 'Flexible', 24, 1),
('2026-09-01', 3, 'Part-time', 25, 1),
('2026-09-01', 2, 'Flexible', 26, 1),
('2026-09-01', 3, 'Full availability', 27, 1),
('2026-09-01', 3, 'Weekdays only', 28, 1),
('2026-09-01', 2, 'Flexible', 29, 1),
('2026-09-01', 3, 'Flexible', 30, 1);


INSERT INTO Requests (id, user_id, type_id, message, request_date, status) VALUES
(1, 7, 1, 'Requesting day off for medical appointment', '2026-10-02', 'pending'),
(2, 12, 2, 'Swapping shift with User 14', '2026-10-05', 'solved'),
(3, 20, 3, 'Need shift moved to morning slot', '2026-10-08', 'pending');


INSERT INTO Schedules (id, user_id, pos_id, start_time, end_time) VALUES

(1, 2, 1, '2026-10-01 08:00', '2026-10-01 16:00'),
(2, 2, 3, '2026-10-05 09:00', '2026-10-05 17:00'),
(3, 2, 1, '2026-10-08 08:00', '2026-10-08 16:00'),
(4, 2, 3, '2026-10-12 09:00', '2026-10-12 17:00'),

(5, 7, 1, '2026-10-01 08:00', '2026-10-01 12:00'),
(6, 7, 1, '2026-10-01 16:00', '2026-10-01 20:00'),
(7, 7, 1, '2026-10-04 08:00', '2026-10-04 16:00'),
(8, 7, 1, '2026-10-09 08:00', '2026-10-09 16:00'),
(9, 7, 1, '2026-10-11 08:00', '2026-10-11 16:00'),


(10, 8, 2, '2026-10-02 08:00', '2026-10-02 16:00'),
(11, 8, 2, '2026-10-06 08:00', '2026-10-06 16:00'),
(12, 8, 2, '2026-10-10 08:00', '2026-10-10 16:00'),


(13, 9, 2, '2026-10-02 16:00', '2026-10-02 23:59'),
(14, 9, 2, '2026-10-07 16:00', '2026-10-07 23:59'),
(15, 9, 2, '2026-10-13 16:00', '2026-10-13 23:59'),


(16, 10, 1, '2026-10-03 08:00', '2026-10-03 12:00'),
(17, 10, 1, '2026-10-03 14:00', '2026-10-03 18:00'),
(18, 10, 1, '2026-10-08 08:00', '2026-10-08 16:00'),
(19, 10, 1, '2026-10-14 08:00', '2026-10-14 16:00'),


(20, 11, 2, '2026-10-04 08:00', '2026-10-04 16:00'),
(21, 11, 2, '2026-10-11 08:00', '2026-10-11 16:00'),
(22, 12, 3, '2026-10-01 09:00', '2026-10-01 17:00'),
(23, 12, 3, '2026-10-06 09:00', '2026-10-06 17:00'),
(24, 12, 3, '2026-10-09 09:00', '2026-10-09 17:00'),
(25, 13, 4, '2026-10-02 09:00', '2026-10-02 17:00'),
(26, 13, 4, '2026-10-07 09:00', '2026-10-07 17:00'),
(27, 14, 3, '2026-10-03 09:00', '2026-10-03 17:00'),
(28, 14, 3, '2026-10-10 09:00', '2026-10-10 17:00'),
(29, 15, 4, '2026-10-05 09:00', '2026-10-05 17:00'),
(30, 15, 4, '2026-10-12 09:00', '2026-10-12 17:00'),


(31, 16, 5, '2026-10-01 08:00', '2026-10-01 16:00'),
(32, 16, 5, '2026-10-08 08:00', '2026-10-08 16:00'),
(33, 17, 6, '2026-10-02 08:00', '2026-10-02 16:00'),
(34, 17, 6, '2026-10-09 08:00', '2026-10-09 16:00'),
(35, 18, 5, '2026-10-03 08:00', '2026-10-03 16:00'),
(36, 18, 5, '2026-10-10 08:00', '2026-10-10 16:00'),
(37, 19, 6, '2026-10-04 08:00', '2026-10-04 16:00'),
(38, 19, 6, '2026-10-11 08:00', '2026-10-11 16:00'),

(39, 20, 7, '2026-10-01 08:00', '2026-10-01 16:00'),
(40, 20, 7, '2026-10-07 08:00', '2026-10-07 16:00'),
(41, 21, 8, '2026-10-02 08:00', '2026-10-02 16:00'),
(42, 21, 8, '2026-10-08 08:00', '2026-10-08 16:00'),
(43, 22, 7, '2026-10-03 08:00', '2026-10-03 16:00'),
(44, 22, 7, '2026-10-09 08:00', '2026-10-09 16:00'),
(45, 23, 8, '2026-10-04 08:00', '2026-10-04 16:00'),
(46, 23, 8, '2026-10-10 08:00', '2026-10-10 16:00'),


(47, 24, 9, '2026-10-01 08:00', '2026-10-01 16:00'),
(48, 24, 9, '2026-10-06 08:00', '2026-10-06 16:00'),
(49, 25, 10, '2026-10-02 08:00', '2026-10-02 16:00'),
(50, 25, 10, '2026-10-07 08:00', '2026-10-07 16:00'),
(51, 26, 9, '2026-10-03 08:00', '2026-10-03 16:00'),
(52, 26, 9, '2026-10-08 08:00', '2026-10-08 16:00'),
(53, 27, 10, '2026-10-04 08:00', '2026-10-04 16:00'),
(54, 27, 10, '2026-10-09 08:00', '2026-10-09 16:00'),


(55, 28, 1, '2026-10-05 08:00', '2026-10-05 16:00'),
(56, 28, 1, '2026-10-12 08:00', '2026-10-12 16:00'),
(57, 29, 3, '2026-10-06 09:00', '2026-10-06 17:00'),
(58, 29, 3, '2026-10-13 09:00', '2026-10-13 17:00'),
(59, 30, 5, '2026-10-07 08:00', '2026-10-07 16:00'),
(60, 30, 5, '2026-10-14 08:00', '2026-10-14 16:00');
