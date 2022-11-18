INSERT INTO Category
(name, color)
VALUES ('RedCategory', '#bada55'),
       ('BlueCategory', '#cacaca'),
       ('GreenCategory', '#ffffff')

INSERT INTO Transaction
(name, description, amount, type, categoryId)
VALUES
("Transaction1", "Desc1", 9.99, 'outgoing', 1)
("Transaction2", "Desc2", 10, 'outgoing', 2)
("Transaction3", "Desc3", 20, 'incoming', 3)
("Transaction4", "", 1000, 'incoming', NULL)

INSERT INTO RepeatingTransaction
(name, description, amount, type, categoryId, days, dateFrom, dateTo, frequency)
VALUES
("RepTransaction1", "Desc1", 12.99, 'outgoing', 1, 1, CURRENT_TIMESTAMP(), DATEADD('DAY', 1, CURRENT_DATE), 'daily')
("RepTransaction2", "Desc2", 0.99, 'outgoing', 2, 7, CURRENT_TIMESTAMP(), DATEADD('YEAR', 1, CURRENT_DATE), 'weekly')
("RepTransaction3", "Desc3", 2.5, 'incoming', 3, 1, CURRENT_TIMESTAMP(), DATEADD('MONTH', 6, CURRENT_DATE), 'daily')
("RepTransaction4", "", 1000, 'incoming', NULL, 30, CURRENT_TIMESTAMP(), DATEADD('YEAR', 1, CURRENT_DATE), 'monthly')
