DROP DATABASE chatprojectdatabase;
CREATE DATABASE chatprojectdatabase CHARACTER SET utf8 COLLATE utf8_general_ci;
USE chatprojectdatabase;
CREATE TABLE users (
    pseudo VARCHAR(25),
    IPaddress VARCHAR(15) PRIMARY KEY NOT NULL
);
CREATE TABLE conversation (
    IPaddress VARCHAR(15) PRIMARY KEY NOT NULL,
    message VARCHAR(500),
    horodatage TIMESTAMP
);
-- INSERT INTO users(pseudo, IPaddress) VALUES('PapéYo','10.5.255.255');
SELECT * FROM users;
-- INSERT INTO conversation(IPaddress, message, horodatage) VALUES ('10.5.255.255','Salut tu vas bien ?', now());
SELECT * FROM conversation;