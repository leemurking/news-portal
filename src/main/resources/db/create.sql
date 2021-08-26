SET MODE PostgreSQL;

CREATE DATABASE grantnews_portal;
\c grantnews_portal;

CREATE TABLE departments (
 id int PRIMARY KEY,
 departmentName VARCHAR,
 description VARCHAR,
 numberOfEmployees VARCHAR
);

CREATE TABLE news (
 id int PRIMARY KEY ,
 title VARCHAR,
 content VARCHAR,
 departmentID INTEGER
);

CREATE TABLE employees (
 id int PRIMARY KEY,
 EmployeeName VARCHAR,
 position VARCHAR,
 role VARCHAR,
 email VARCHAR,
 phoneNumber VARCHAR,
 departmentId INTEGER

);

 CREATE TABLE departments_employees (
 id int PRIMARY KEY,
 employeeid INTEGER,
  departmentid INTEGER
);

CREATE DATABASE grantnews_portal_test;