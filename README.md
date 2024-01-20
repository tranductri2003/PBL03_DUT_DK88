# DK88 - Android app allow student to trade courses

## Introduction

Registering for courses at university is a long and painful process, sometimes Students have to register for classes they don't want to learn yet or the teachers they don't like because there are no other classes. A lot of time, you take a class another student desires and that student also takes a class you desire to register. That is just a simple example, the chain can be 3 to 4 students. DK88 was built to solve this problem by letting students register all the classes they want and all the classes they can give, the system will group students into trade groups so that anyone get the class they want.

## How the system works & Demo

How system works:
* Student creates an account and requests admin to active.
* After active, the student can update contact info (facebook, mail..).
* Students update classes they have and want, the system will recommend trade groups for students to join and trade.
* Students trade and leave the group, update the class they want and have if want to continue trading.

Demo images: https://drive.google.com/drive/folders/1AdDYeZGnRMoTY6pYBEyG_IomrngoByFR?usp=sharing

## Installation

Because the AWS free tier has expired, I've turned off the server so you can't use it directly. So this tutorial will be on how to set up your own server and run the client. Also, although the android client is not included in this repository, I will still guide you here.

### Server

1. Install Java, Maven, Tomcat, Spring Boot, SQL Server.
2. Clone this project.
3. Open SQL Server, create database name "DK88", run all SQL query in folder /backend/sql_query/new_query of this project.
4. Open file DatabaseHelper.java and change "connectionUrl" string depend on your computer.
5. Maven build and run.

### Client (Android)

1. Install Java, Android SDK, Android Studio
2. Clone from: https://github.com/nvkuy/DK88_CLIENT
3. Open and change server address in ApiRequester.java
4. Build and run.

## Will do if have time

* Make UI more colorful, fix double transition when trade group form.
* Stress test the system.
* Make chat in trade group.
