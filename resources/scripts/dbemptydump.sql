DROP DATABASE IF EXISTS pealib;
CREATE DATABASE pealib;

USE pealib;

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
	Email VARCHAR(30) UNIQUE NOT NULL,
	ID INT UNIQUE NOT NULL AUTO_INCREMENT,
	PRIMARY KEY (ID));

CREATE TABLE IF NOT EXISTS Books 
	(ID INT UNIQUE NOT NULL AUTO_INCREMENT,
	ISBN VARCHAR(13) UNIQUE NOT NULL,
	Title VARCHAR(100) NOT NULL,
	Author VARCHAR(100) NOT NULL,
	Edition VARCHAR(30),
	Publisher VARCHAR(100),
	Description VARCHAR(1000),
	YearPublish INT,
	Copies INT,
	PRIMARY KEY (ID));
    
CREATE TABLE IF NOT EXISTS Borrows
	(BorrowID INT UNIQUE NOT NULL AUTO_INCREMENT,
	UserID INT,
	BookID INT,
	DateRequested DATE,
	DateBorrowed DATE,
	DateReturned DATE,
	PRIMARY KEY (BorrowID),
  	FOREIGN KEY (UserID) REFERENCES Users(ID) ON DELETE CASCADE,
  	FOREIGN KEY (BookID) REFERENCES Books(ID) ON DELETE CASCADE
	);

CREATE TABLE IF NOT EXISTS Reserves 
	(UserID INT,
	BookID INT,
	DatetimeReserved TIMESTAMP DEFAULT NOW(),
	PRIMARY KEY (UserID,BookID),
  	FOREIGN KEY (UserID) REFERENCES Users(ID) ON DELETE CASCADE,
	FOREIGN KEY (BookID) REFERENCES Books (ID) ON DELETE CASCADE
	);

INSERT INTO Users (FirstName, Lastname, Username, Password, Type, Address, ContactNo, Email) VALUES('Librarian','Librarian','librarian',SHA2("123456",0),'Librarian','library','1234567','librarian@library.com');
