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
INSERT INTO Users (FirstName, Lastname, Username, Password, Type, Address, ContactNo, Email) VALUES('Ray Vincent','Gomez','rgomez',SHA2("123456",0),'User','Quezon City','09876543210','rayvincent.gomez@gmail.com');
INSERT INTO Users (FirstName, Lastname, Username, Password, Type, Address, ContactNo, Email) VALUES('Jomel Christopher','Villar','jvillar',SHA2("123456",0),'User','Quezon City','09062840750','jomel.villar@gmail.com');
INSERT INTO Users (FirstName, Lastname, Username, Password, Type, Address, ContactNo, Email) VALUES('Anmuary','Pantaleon','apantaleon',SHA2("123456",0),'User','Malabon City','09175839123','anmuarypantaleon@gmail.com');
INSERT INTO Users (FirstName, Lastname, Username, Password, Type, Address, ContactNo, Email) VALUES('Janine','Lim','jlim',SHA2("123456",0),'User','Manila City','09156784309','kanra.cpg@gmail.com');
INSERT INTO Users (FirstName, Lastname, Username, Password, Type, Address, ContactNo, Email) VALUES('Jeph','Dizon','jdizon',SHA2("123456",0),'User','Quezon City','09173328750','jjrdizon@gmail.com');
INSERT INTO Users (FirstName, Lastname, Username, Password, Type, Address, ContactNo, Email) VALUES('Reiniel','Lozada','rlozada',SHA2("123456",0),'User','Quezon City','09173095104','nielorockz@gmail.com');

INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007222556','The Alchemist','Paulo Coelho','1st','HarperCollins Publishers Limited','','1990','5');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007155662','The Alchemist: A Fable about Following Your Dream','Paulo Coelho','1st','HarperCollins Publishers','','1991','1');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('1234567890122','Brida','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','2');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007214715','By the River Piedra I Sat down and Wept','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','9');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007116055','The Devil and Miss Prym: A Novel of Temptation','Paulo Coelho','1st','Zondervan','','1992','1');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0060589280','Eleven Minutes','Paulo Coelho','1st','HarperCollins Canada, Limited','','1992','1');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('000723578X','Like the Flowing River: Thoughts and Reflections','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','3');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007156324','Manual of the Warrior of the Light','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','3');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007214693','The Valkyries','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','2');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007214707','Pilgrimage','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','1');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007103468','Veronika Decides to Die','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','1');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007251866','The Witch of Portobello','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','2');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0007204310','The Zahir : A Novel of Love, Longing and Obsession','Paulo Coelho','1st','HarperCollins Publishers Limited','','1992','3');
INSERT INTO books (ISBN, Title, Author, Edition, Publisher, Description, YearPublish, Copies) VALUES('0060534184','Manual del Guerrero de la Luz','Paulo Coelho','1st','HarperCollins Publishers','','1992','9');
