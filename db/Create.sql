
CREATE TABLE IF NOT EXISTS Users (
    "id" INTEGER PRIMARY KEY,
    "first_name" TEXT NOT NULL,
    "last_name" TEXT NOT NULL,
    "birth_date" TEXT CHECK("birth_date" LIKE '____-__-__') NOT NULL,
    "role" TEXT CHECK("role" IN ('admin','manager', 'regular')) NOT NULL,
    "password" TEXT NOT NULL,
    "username" TEXT NOT NULL UNIQUE,
    "email" TEXT CHECK("email" LIKE '_%@_%._%'),
    "cellular" TEXT
);

CREATE TABLE IF NOT EXISTS Departments (
    "id" INTEGER PRIMARY KEY,
    "name" TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS RequestTypes (
    "id" INTEGER PRIMARY KEY,
    "type" TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Positions (
    "id" INTEGER PRIMARY KEY,
    "dept_id" INTEGER NOT NULL,
    "name" TEXT NOT NULL,
    FOREIGN KEY ("dept_id") REFERENCES "Departments"("id")
);

CREATE TABLE IF NOT EXISTS Availability (
    "id" INTEGER PRIMARY KEY,
    "last_updated" TEXT NOT NULL,
    "days_week" INTEGER NOT NULL,
    "info" TEXT,
    "user_id" INTEGER UNIQUE NOT NULL,
    "is_active" INTEGER NOT NULL CHECK("is_active" IN (1, 0)),
    FOREIGN KEY ("user_id") REFERENCES "Users"("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Requests (
    "id" INTEGER PRIMARY KEY,
    "user_id" INTEGER,
    "type_id" INTEGER,
    "message" TEXT NOT NULL,
    "request_date" TEXT NOT NULL,
    "status" TEXT CHECK("status" IN ('solved', 'pending')),
    FOREIGN KEY ("user_id") REFERENCES "Users"("id") ON DELETE CASCADE,
    FOREIGN KEY ("type_id") REFERENCES "RequestTypes"("id")
);


CREATE TABLE IF NOT EXISTS Works (
    "user_id" INTEGER,
    "pos_id" INTEGER,
    PRIMARY KEY ("user_id", "pos_id"),
    FOREIGN KEY ("user_id") REFERENCES "Users"("id") ON DELETE CASCADE,
    FOREIGN KEY ("pos_id") REFERENCES "Positions"("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Schedules (
    "id" INTEGER PRIMARY KEY,
    "user_id" INTEGER NOT NULL,
    "start_time" TEXT NOT NULL,
    "end_time" TEXT NOT NULL,
    "pos_id" INTEGER NOT NULL,
    CHECK("start_time" < "end_time"),
    FOREIGN KEY ("user_id", "pos_id") REFERENCES "Works"("user_id", "pos_id") ON DELETE CASCADE
);


