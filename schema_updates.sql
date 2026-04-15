-- ============================================
-- OTT PROJECT - SCHEMA UPDATE SCRIPT
-- ============================================
-- Run this script in MySQL to add all new columns
-- Execute: mysql -u root -p ott_database < schema_updates.sql
-- ============================================

-- 1. Add progress and episode columns to watch_history
ALTER TABLE watch_history ADD COLUMN progress INT DEFAULT 0;
ALTER TABLE watch_history ADD COLUMN episode INT DEFAULT 1;

-- 2. Add poster column to content table
ALTER TABLE content ADD COLUMN poster VARCHAR(255);

-- 3. Add subscription expiry_date to users
ALTER TABLE users ADD COLUMN expiry_date DATE;

-- 4. Add access_type column to content (basic/premium)
ALTER TABLE content ADD COLUMN access_type VARCHAR(20) DEFAULT 'basic';

-- 5. Verify columns were added
-- SELECT progress, episode FROM watch_history LIMIT 1;
-- SELECT poster FROM content LIMIT 1;
-- SELECT expiry_date FROM users LIMIT 1;
-- SELECT access_type FROM content LIMIT 1;

-- ============================================
-- OPTIONAL: Sample data for testing
-- ============================================

-- UPDATE content SET poster = 'images/inception.jpg' WHERE title LIKE '%Inception%';
-- UPDATE content SET access_type = 'premium' WHERE title LIKE '% Oppenheimer%';
-- UPDATE users SET expiry_date = '2026-12-31' WHERE email = 'admin@test.com';