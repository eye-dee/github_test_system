CREATE TABLE users (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  email       VARCHAR(100) NOT NULL,
  github_nick VARCHAR(100) NOT NULL,
  UNIQUE (email)
);

CREATE TABLE tasks (
  id            INT PRIMARY KEY                         AUTO_INCREMENT,
  user_id       INT REFERENCES users (id),
  register_time TIMESTAMP,
  successful    BOOL                                    DEFAULT FALSE,
  status        ENUM ('CREATED', 'PROGRESS', 'CHECKED') DEFAULT 'PROGRESS',
  log           TEXT
  /* add unique identifier of pull
  */
)