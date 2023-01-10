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
    categoryId   BIGINT,
    createdAt    TIMESTAMP NOT NULL
);

--
-- Repeating Transaction table definition
-- 'EachSecond' only for showcase
--
CREATE TABLE IF NOT EXISTS RepeatingTransaction
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(250),
    amount       DOUBLE NOT NULL,
    type         ENUM('outgoing', 'incoming') NOT NULL,
    categoryId   BIGINT,
    createdAt    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nextCreateAt TIMESTAMP NOT NULL,
    days         INT NOT NULL,
    dateFrom     TIMESTAMP NOT NULL,
    dateTo       TIMESTAMP NOT NULL,
    frequency    ENUM('daily', 'weekly', 'monthly', 'everyThreeMonths', 'everySixMonths', 'yearly', 'custom', 'EachSecond') NOT NULL
);