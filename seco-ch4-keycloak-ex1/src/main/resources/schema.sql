CREATE TABLE IF NOT EXISTS `workout`
(
    `id`
    INT
    NOT
    NULL
    AUTO_INCREMENT,
    `user`
    VARCHAR
(
    45
) NULL,
    `start` DATETIME NULL,
    `end` DATETIME NULL,
    `difficulty` INT NULL,
    PRIMARY KEY
(
    `id`
));
