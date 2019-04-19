CREATE DATABASE IF NOT EXISTS Bookstore;

USE Bookstore;

DROP TABLE IF EXISTS Books;
DROP TABLE IF EXISTS Clients;

CREATE TABLE IF NOT EXISTS Books(id INT PRIMARY KEY, 
					name VARCHAR(70));
CREATE TABLE IF NOT EXISTS Clients(id INT PRIMARY KEY, 
					name VARCHAR(70));
                    