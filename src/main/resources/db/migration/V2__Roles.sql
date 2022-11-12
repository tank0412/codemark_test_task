CREATE TABLE role (
role_id INT AUTO_INCREMENT PRIMARY KEY,
name varchar(64)
);

INSERT INTO role (name) VALUES ('Администратор');
INSERT INTO role (name) VALUES ('Модератор');
INSERT INTO role (name) VALUES ('Оператор');

--many to many
CREATE TABLE user_role (
user_login varchar(64) REFERENCES "user"(login),
role_id INT REFERENCES role(role_id)
);

INSERT INTO user_role VALUES ('xmillin0', '1');
INSERT INTO user_role VALUES ('xmillin0', '2');
INSERT INTO user_role VALUES ('thaskell2', '3');
INSERT INTO user_role VALUES ('abodimeade3', '2');
INSERT INTO user_role VALUES ('lkeuneke6', '1');