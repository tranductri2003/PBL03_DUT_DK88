use DK88;

CREATE TABLE USER_TYPE (
	token varchar(66) PRIMARY KEY,
	type_code varchar(22)
)

CREATE TABLE NUser 
(
	userName varchar(100) PRIMARY KEY,
	passWord varchar(100),
	name nvarchar(100),
	phoneNumber varchar(20),
	token varchar(66) unique not null FOREIGN KEY REFERENCES USER_TYPE(token)
);

CREATE TABLE Student 
(
	userName varchar(100) FOREIGN KEY REFERENCES NUser(userName),
	studentID varchar(20) PRIMARY KEY,
	status int
);

CREATE TABLE Admin 
(
	userName varchar(100) FOREIGN KEY REFERENCES NUser(userName),
	email varchar(100)
);

CREATE TABLE USER_CLASS (
	studentID varchar(20) FOREIGN KEY REFERENCES Student(studentID),
	classID varchar(20),
	have bit,
	CONSTRAINT PK_USER_CLASS PRIMARY KEY (studentID, classID)
);

CREATE TABLE Request (
	requestID int IDENTITY(1, 1) PRIMARY KEY,
	targetID varchar(20) FOREIGN KEY REFERENCES Student(studentID),
	messageRequest nvarchar(100)
)

CREATE TABLE ActiveRequest (
	requestID int PRIMARY KEY FOREIGN KEY REFERENCES Request(requestID),
	imageFront varchar(100),
	imageBack varchar(100),
)

CREATE TABLE BanRequest (
	requestID int PRIMARY KEY FOREIGN KEY REFERENCES Request(requestID),
	moreDetail nvarchar(1000)
)

CREATE TABLE Image (
	fileName varchar(100) PRIMARY KEY,
	token varchar(66) FOREIGN KEY REFERENCES NUser(token)
)

CREATE TABLE ImageProof (
	requestID int FOREIGN KEY REFERENCES BanRequest(requestID),
	fileName varchar(100) FOREIGN KEY REFERENCES Image(fileName),
	CONSTRAINT PK_IP PRIMARY KEY (requestID, fileName)
)

