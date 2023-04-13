use DK88;

CREATE TABLE NUser 
(
	userName varchar(100) PRIMARY KEY,
	passWord varchar(100),
	name nvarchar(100),
	phoneNumber varchar(20),
	roleCode int
);

CREATE TABLE Student 
(
	userName varchar(100) PRIMARY KEY FOREIGN KEY REFERENCES NUser(userName),
	studentID varchar(20) unique not null,
	status int
);

CREATE TABLE QueryClass (
	idQuery int IDENTITY(1, 1) PRIMARY KEY,
	targetID varchar(20) unique FOREIGN KEY REFERENCES Student(studentID)
)

CREATE TABLE StudentClass (
	studentID varchar(20) FOREIGN KEY REFERENCES QueryClass(targetID),
	classID varchar(33),
	have bit,
	CONSTRAINT PK_SC PRIMARY KEY (studentID, classID)
)

CREATE TABLE Admin 
(
	userName varchar(100) PRIMARY KEY FOREIGN KEY REFERENCES NUser(userName),
	email varchar(100)
);

CREATE TABLE Request (
	requestID int IDENTITY(1, 1) PRIMARY KEY,
	targetID varchar(20) FOREIGN KEY REFERENCES Student(studentID),
	requestCode int
)

CREATE TABLE BanRequest (
	requestID int PRIMARY KEY FOREIGN KEY REFERENCES Request(requestID),
	moreDetail nvarchar(1000)
)

CREATE TABLE Image (
	fileName varchar(100) PRIMARY KEY,
	owner varchar(100) FOREIGN KEY REFERENCES NUser(userName)
)

CREATE TABLE ImageRequest (
	requestID int FOREIGN KEY REFERENCES BanRequest(requestID),
	fileName varchar(100) FOREIGN KEY REFERENCES Image(fileName),
	CONSTRAINT PK_IP PRIMARY KEY (requestID, fileName)
)

CREATE TABLE ActiveRequest (
	requestID int PRIMARY KEY FOREIGN KEY REFERENCES Request(requestID),
	imageFront varchar(100) FOREIGN KEY REFERENCES Image(fileName),
	imageBack varchar(100) FOREIGN KEY REFERENCES Image(fileName),
)

