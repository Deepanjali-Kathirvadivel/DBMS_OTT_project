-- OTT Platform Database Schema for MySQL
-- Run this script to create the database and tables

CREATE DATABASE IF NOT EXISTS ott_database;
USE ott_database;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    expiry_date DATE DEFAULT NULL
);

-- Content table (movies and series)
CREATE TABLE IF NOT EXISTS content (
    content_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    genre VARCHAR(50),
    release_year INT,
    rating DECIMAL(3,1),
    trailer_link VARCHAR(500),
    content_type VARCHAR(20) NOT NULL,
    poster VARCHAR(500),
    access_type VARCHAR(20) DEFAULT 'basic'
);

-- Movies table (additional movie info)
CREATE TABLE IF NOT EXISTS movies (
    movie_id INT PRIMARY KEY,
    duration INT,
    director VARCHAR(100),
    cast TEXT,
    FOREIGN KEY (movie_id) REFERENCES content(content_id) ON DELETE CASCADE
);

-- Series table
CREATE TABLE IF NOT EXISTS series (
    series_id INT AUTO_INCREMENT PRIMARY KEY,
    content_id INT NOT NULL,
    total_seasons INT,
    total_episodes INT,
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE
);

-- Episodes table
CREATE TABLE IF NOT EXISTS episodes (
    episode_id INT AUTO_INCREMENT PRIMARY KEY,
    series_id INT NOT NULL,
    season_number INT NOT NULL,
    episode_number INT NOT NULL,
    title VARCHAR(200),
    duration_minutes INT,
    FOREIGN KEY (series_id) REFERENCES series(series_id) ON DELETE CASCADE
);

-- Watch history table
CREATE TABLE IF NOT EXISTS watch_history (
    watch_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    content_id INT NOT NULL,
    progress INT DEFAULT 0,
    episode INT DEFAULT 1,
    watch_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (content_id) REFERENCES content(content_id) ON DELETE CASCADE,
    UNIQUE KEY unique_watch (user_id, content_id)
);

-- Insert sample admin user (password: admin123)
INSERT INTO users (name, email, password, role) VALUES
('Admin', 'admin@ott.com', 'admin123', 'admin');

-- Insert sample regular user (password: user123)
INSERT INTO users (name, email, password, role) VALUES
('Test User', 'user@ott.com', 'user123', 'user');