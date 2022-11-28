use contribution;
DROP TABLE IF EXISTS `spark`;
CREATE TABLE `spark` (
                         `project` varchar(255) NOT NULL,
                         `author` varchar(255) NOT NULL,
                         `score` double NOT NULL,
                         `rate` double NOT NULL,
                         PRIMARY KEY (`project`,`author`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;