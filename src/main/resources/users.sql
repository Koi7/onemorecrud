CREATE TABLE `hibernatedb`.`users` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `role_UNIQUE` (`role` ASC),
  UNIQUE INDEX `password_UNIQUE` (`password` ASC));

ALTER TABLE `hibernatedb`.`users`
  CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `hibernatedb`.`users`
  DROP INDEX `password_UNIQUE` ,
  DROP INDEX `role_UNIQUE` ;
