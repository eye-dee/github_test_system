CREATE TABLE users (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  email       VARCHAR(100) NOT NULL,
  github_nick VARCHAR(100) NOT NULL,
  password    VARCHAR(100) NOT NULL,
  UNIQUE (email)
);

CREATE TABLE repos (
  id    INT PRIMARY KEY,
  name  VARCHAR(100) NOT NULL,
  owner VARCHAR(100) NOT NULL
);

CREATE TABLE tasks (
  id            INT PRIMARY KEY AUTO_INCREMENT,
  user_id       INT REFERENCES users (id),
  repo_id       INT,
  register_time TIMESTAMP,
  successful    BOOL            DEFAULT FALSE,
  log           JSON,
  FOREIGN KEY (repo_id) REFERENCES repos (id)
    ON DELETE CASCADE
);
