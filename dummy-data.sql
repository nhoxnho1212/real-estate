INSERT INTO `real-estate`.`home_type` (`type_name`)
VALUES
	('Townhouses'),
	(' Villas'),
	('Apartment'),
	('Shophouse'),
	('Others')
    ;

INSERT INTO `real-estate`.`extra` (`extra_name`)
VALUES
	('Swimming Pool'),
    ('Gym'),
    ('Elevator'),
    ('Green Park'),
    ('Garden')
    ;

INSERT INTO `real-estate`.`jhi_user` (`login`,`password_hash`,`first_name`,`last_name`,`email`,`image_url`,`activated`,`lang_key`,`created_by`,`last_modified_by`, `phone_number`)
VALUES
	('oxfZu86kx3huDJ8eYAnRxA==','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','ItOjZv00YVIlcx2JCuKjow==','ItOjZv00YVIlcx2JCuKjow==','kb+iYWpVqwii/b0/cXV+9g==','gjW0jkDdoTC6MMy26ZaOhw==',true,'en','ORGP+7m6sWIVbJrS04N8uw==','ORGP+7m6sWIVbJrS04N8uw==', 'iKMlkF9VTR5aZTMj8qFsrg=='),
--     ('admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost','none',true,'en','system','system', '0911200200'),
    ('DX9frXe1J2sroKN7w+Ry0g==','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','R9NQ/27GaaxdojmEPNSlBw==','R9NQ/27GaaxdojmEPNSlBw==','pJAUn9tw7aAXX3+cYBAfLg==','gjW0jkDdoTC6MMy26ZaOhw==',true,'en','ORGP+7m6sWIVbJrS04N8uw==','ORGP+7m6sWIVbJrS04N8uw==','olPInQ0Q57AVoDagmlPsBw==')
--     ('user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost','none',true,'en','system','system','0911201201')
    ;

INSERT INTO `real-estate`.`jhi_authority` (`name`)
VALUES
	('ROLE_ADMIN'),
	('ROLE_USER')
    ;

INSERT INTO `real-estate`.`jhi_user_authority` (`user_id`, `authority_name`)
VALUES
	('1', 'ROLE_ADMIN'),
	('1', 'ROLE_USER'),
	('2', 'ROLE_USER')
	;


INSERT INTO `real-estate`.`project` (`address`, `attachment`, `city`, `desciption`, `floor_space`, `owner`, `owner_desciption`, `price`, `project_status`, `rooms`, `total_square`, `home_type_id`, `user_id`)
VALUES
	('371 Nguyen Kiem', 'none', 'HCM', 'desciption...', '200', 'City World', 'This  is a big company...', '9000', '1', '1000', '90000', 1, 2),
	('97 Vo Van Tan', 'none', 'Da Lat', 'desciption...', '150', 'Deve', 'This  is a big company...', '3306', '2', '990', '10000', 2, 1),
    ('411 Nguyen Kiem', 'none', 'HCM', 'desciption...', '310', 'Thien', 'This  is a big company...', '9000', '2', '910', '70011', 1, 2),
	('122 Vo Van Tan', 'none', 'Vung Tau', 'desciption...', '196', 'CenZe', 'This  is a big company...', '3306', '3', '1010', '19000', 1, 1),
    ('121 Nguyen Kiem', 'none', 'Quy Nhon', 'desciption...', '121', 'Thier', 'This  is a big company...', '9000', '1', '1212', '80000', 5, 1)
	;

INSERT INTO `real-estate`.`rel_project__extra` (`project_id`, `extra_id`)
VALUES
	('1', '2'),
	('1', '1'),
	('2', '1'),
	('3', '1'),
	('4', '4')
	;
