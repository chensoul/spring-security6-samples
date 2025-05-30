INSERT
IGNORE INTO `user` (`id`, `username`, `password`) VALUES ('1', 'user', '{noop}password');

INSERT
IGNORE INTO `role` (`id`, `name`, `user`) VALUES ('1', 'READ', '1');
INSERT
IGNORE INTO `role` (`id`, `name`, `user`) VALUES ('2', 'WRITE', '1');

