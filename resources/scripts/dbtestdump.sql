﻿DROP DATABASE IF EXISTS test;
CREATE DATABASE test;

USE test;

DROP TABLE IF EXISTS Reservations;
DROP TABLE IF EXISTS Borrows;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Books;

CREATE TABLE IF NOT EXISTS Users
	(FirstName VARCHAR(30) NOT NULL,
	LastName VARCHAR(30) NOT NULL,
	UserName VARCHAR(30) UNIQUE NOT NULL,
	Password CHAR(64) NOT NULL,
	Type ENUM('User', 'Librarian', 'Pending'),
	Address VARCHAR(100),
	ContactNo VARCHAR(20),
	Email VARCHAR(30),
	ID INT UNIQUE,
	PRIMARY KEY (ID));

CREATE TABLE IF NOT EXISTS Books 
	(ID INT UNIQUE,
	ISBN VARCHAR(13) UNIQUE NOT NULL,
	Title VARCHAR(100) NOT NULL,
	Author VARCHAR(100) NOT NULL,
	Edition VARCHAR(30),
	Publisher VARCHAR(100),
	Description VARCHAR(300),
	YearPublish INT,
	Copies INT,
	PRIMARY KEY (ID));
    
CREATE TABLE IF NOT EXISTS Borrows
	(BorrowID INT UNIQUE,
	UserID INT,
	BookID INT,
	DateRequested DATE,
	DateBorrowed DATE,
	DateReturned DATE,
	PRIMARY KEY (BorrowID)
	);

CREATE TABLE IF NOT EXISTS Reserves
	(UserID INT,
	BookID INT,
	DatetimeReserved TIMESTAMP DEFAULT NOW(),
	PRIMARY KEY (UserID,BookID)
	);