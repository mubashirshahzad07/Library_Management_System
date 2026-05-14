DROP DATABASE IF EXISTS LibraryManagement;
CREATE DATABASE LibraryManagement;
USE LibraryManagement;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('Admin', 'Librarian', 'Student') NOT NULL,
    CONSTRAINT uq_username_role UNIQUE (username, role)
);

CREATE TABLE Books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    CONSTRAINT chk_available_copies CHECK (available_copies >= 0)
);

CREATE TABLE Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status ENUM('Issued', 'Returned') NOT NULL DEFAULT 'Issued',
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (book_id) REFERENCES Books(book_id)
);

CREATE TABLE Fines (
    fine_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    user_id INT NOT NULL,
    fine_amount DECIMAL(5,2) NOT NULL,
    payment_status ENUM('Paid', 'Unpaid') DEFAULT 'Unpaid',
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (transaction_id) REFERENCES Transactions(transaction_id)
);


CREATE VIEW overdue_report AS
SELECT 
    u.username AS student_name, 
    b.title AS book_title, 
    f.fine_amount, 
    f.payment_status
FROM Fines f
JOIN Users u ON f.user_id = u.user_id
JOIN Transactions t ON f.transaction_id = t.transaction_id
JOIN Books b ON t.book_id = b.book_id;

CREATE VIEW student_transaction_history AS
SELECT
    u.user_id,
    u.name,
    u.username,
    b.title AS book_title,
    b.author,
    t.issue_date,
    t.due_date,
    t.return_date,
    t.status,
    f.fine_amount,
    f.payment_status
FROM Transactions t
JOIN Users u ON t.user_id = u.user_id
JOIN Books b ON t.book_id = b.book_id
LEFT JOIN Fines f ON f.transaction_id = t.transaction_id
WHERE u.role = 'Student';

CREATE VIEW librarian_issued_books AS
SELECT
    t.transaction_id,
    u.name AS student_name,
    u.username,
    b.title AS book_title,
    b.author,
    b.category,
    t.issue_date,
    t.due_date,
    t.status
FROM Transactions t
JOIN Users u ON t.user_id = u.user_id
JOIN Books b ON t.book_id = b.book_id
WHERE t.status = 'Issued';

INSERT INTO Users (name, username, password, role) VALUES
('Mubashir Shahzad', 'Mubashir_Librarian', 'admin123', 'Librarian'),
('Shumaail Ali', 'Shumaail_Student', 'study123', 'Student');

INSERT INTO Books (title, author, category, total_copies, available_copies) VALUES
('The Maze Runner', 'James Dashner', 'Sci-fi', 5, 5),
('Peer-e-Kamil', 'Umera Ahmed', 'Novel', 3, 3),
('The Hobbit', 'Tolkien', 'Fantasy', 4, 4);

INSERT INTO Transactions (user_id, book_id, issue_date, due_date)
VALUES (2, 2, '2026-04-01', '2026-04-10');

UPDATE Transactions 
SET return_date = CURDATE(), status = 'Returned'
WHERE user_id = 2 AND book_id = 2 AND return_date IS NULL;

SELECT * FROM overdue_report;

SELECT * FROM student_transaction_history;


INSERT INTO Transactions (user_id, book_id, issue_date, due_date, status)
VALUES (2, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'Issued');


SELECT * FROM librarian_issued_books;


SELECT * FROM Books;
UPDATE Books 
SET available_copies = available_copies - 1 
WHERE book_id = 1;

SELECT * FROM Books;


DELETE FROM Users WHERE username = 'shumaail';
INSERT INTO Users (name, username, password, role) VALUES 
('Shumaail Ali', 'shumaail', 'pass123', 'Admin'),
('Shumaail Ali', 'shumaail', 'pass123', 'Librarian'),
('Shumaail Ali', 'shumaail', 'pass123', 'Student');

SELECT * FROM Users WHERE username = 'shumaail';

UPDATE Books SET available_copies = -1 WHERE book_id = 1;   -- err so wrks


UPDATE Transactions 
SET return_date = CURDATE(), status = 'Returned' 
WHERE user_id = 2 AND book_id = 1 AND status = 'Issued';


SELECT * FROM student_transaction_history;


-- test data : (ignore)

INSERT INTO Books (title, author, category, total_copies, available_copies) 
VALUES ('The Metamorphosis', 'Franz Kafka', 'Philosophy', 1, 1);


INSERT INTO Transactions (user_id, book_id, issue_date, due_date, status)
VALUES (3, (SELECT book_id FROM Books WHERE title = 'The Metamorphosis'), 
        CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'Issued');

SET SQL_SAFE_UPDATES = 0; -- using becuz tried to update using title , better to always use primary key instead ie: id.
UPDATE Books 
SET available_copies = available_copies - 1 
WHERE title = 'The Metamorphosis';
SET SQL_SAFE_UPDATES = 1;


SELECT * FROM librarian_issued_books WHERE book_title = 'The Metamorphosis';


SELECT * FROM librarian_issued_books;