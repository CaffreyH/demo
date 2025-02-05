CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              amount DECIMAL(10, 2) NOT NULL,
                              type VARCHAR(50) NOT NULL,
                              category VARCHAR(100),
                              description TEXT,
                              timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);