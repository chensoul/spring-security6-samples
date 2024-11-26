INSERT
IGNORE INTO `user` (`id`, `username`, `password`) VALUES ('1', 'user', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG');

INSERT
IGNORE INTO `authority` (`id`, `name`, `user`) VALUES ('1', 'READ', '1');
INSERT
IGNORE INTO `authority` (`id`, `name`, `user`) VALUES ('2', 'WRITE', '1');

