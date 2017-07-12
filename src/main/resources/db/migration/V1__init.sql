CREATE TABLE users (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  email       VARCHAR(100) NOT NULL,
  github_nick VARCHAR(100) NOT NULL,
  UNIQUE (email)
);

CREATE TABLE tasks (
  id            INT PRIMARY KEY AUTO_INCREMENT,
  user_id       INT REFERENCES users (id),
  register_time TIMESTAMP,
  successful    BOOL            DEFAULT FALSE,
  log           TEXT,
  pull_id       INT UNIQUE KEY
);

INSERT INTO users(email, .users.github_nick) VALUES('anton.nazarof@mail.ru', 'MortyMerr');
INSERT INTO tasks(user_id, register_time, log, pull_id, successful) VALUES (1, NOW(), 'log', 2, TRUE);

# Drop DATABASE testSystem;
# CREATE DATABASE testSystem;