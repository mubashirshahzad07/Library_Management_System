DROP DATABASE IF EXISTS LibraryManagement;
CREATE DATABASE LibraryManagement;
USE LibraryManagement;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('ADMIN', 'LIBRARIAN', 'STUDENT') NOT NULL,
    CONSTRAINT uq_username_role UNIQUE (username, role)
);

CREATE TABLE Books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    total_copies INT NOT NULL,
    available_copies INT NOT NULL,
    CONSTRAINT chk_total_copies CHECK (total_copies > 0),
    CONSTRAINT chk_available_copies CHECK (available_copies >= 0),
    CONSTRAINT chk_logic_copies CHECK (available_copies <= total_copies)
);

CREATE TABLE Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status ENUM('ISSUED', 'RETURNED') NOT NULL DEFAULT 'ISSUED',
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (book_id) REFERENCES Books(book_id),
    CONSTRAINT chk_return_date CHECK (return_date IS NULL OR return_date >= issue_date)
);

CREATE TABLE Fines (
    fine_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    user_id INT NOT NULL,
    fine_amount DECIMAL(5,2) NOT NULL,
    payment_status ENUM('PAID', 'UNPAID') DEFAULT 'UNPAID',
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
WHERE u.role = 'STUDENT';

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
WHERE t.status = 'ISSUED';

CREATE VIEW current_overdue_books AS
SELECT 
    t.transaction_id,
    u.username AS student_name,
    b.title AS book_title,
    t.due_date,
    DATEDIFF(CURDATE(), t.due_date) * 1.00 AS calculated_fine
FROM Transactions t
JOIN Users u ON t.user_id = u.user_id
JOIN Books b ON t.book_id = b.book_id
WHERE t.return_date IS NULL AND CURDATE() > t.due_date;

INSERT INTO Users (name, username, password, role) VALUES
('Mubashir Shahzad', 'Mubashir_Librarian', 'admin123', 'LIBRARIAN'),
('Shumaail Ali', 'Shumaail_Student', 'study123', 'STUDENT');

INSERT INTO Books (isbn, title, author, category, total_copies, available_copies) VALUES
('55-1579', 'The Maze Runner', 'James Dashner', 'Sci-fi', 5, 5),
('54-0857', 'Peer-e-Kamil', 'Umera Ahmed', 'Novel', 3, 3),
('55-5094', 'The Hobbit', 'Tolkien', 'Fantasy', 4, 4);
