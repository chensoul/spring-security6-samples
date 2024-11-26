CREATE TABLE IF NOT EXISTS `users`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `username`
    VARCHAR
(
    45
) NULL,
    `password` VARCHAR
(
    45
) NULL,
    `enabled` INT NOT NULL,
    PRIMARY KEY
(
    `id`
));

CREATE TABLE IF NOT EXISTS `authorities`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `username`
    VARCHAR
(
    45
) NULL,
    `authority` VARCHAR
(
    45
) NULL,
    PRIMARY KEY
(
    `id`
));
