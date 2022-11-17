--
-- Category table definition
--
CREATE TABLE IF NOT EXISTS Category
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name      VARCHAR(50) NOT NULL,
    color     VARCHAR(50) NOT NULL
);

--
-- Transaction table definition
--
CREATE TABLE IF NOT EXISTS Transaction
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(250),
    amount       DOUBLE NOT NULL,
    type         ENUM('outgoing', 'incoming') NOT NULL,
    categoryId   BIGINT REFERENCES Category (id),
    createdAt    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--
-- Repeating Transaction table definition
--
CREATE TABLE IF NOT EXISTS RepeatingTransaction
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(250),
    amount       DOUBLE NOT NULL,
    type         ENUM('outgoing', 'incoming') NOT NULL,
    categoryId   BIGINT REFERENCES Category (id),
    createdAt    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    days         INT NOT NULL,
    dateFrom     DATE NOT NULL,
    dateTo       DATE NOT NULL,
    frequency    ENUM("daily", "weekly", "monthly", "everyThreeMonths", "everySixMonths", "yearly", "custom") NOT NULL
);