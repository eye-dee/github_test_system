CREATE TABLE users (
  id       INT PRIMARY KEY AUTO_INCREMENT,
  email    VARCHAR(100) NOT NULL,
  git_nick VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  UNIQUE (email)
);

CREATE TABLE repos (
  id       INT PRIMARY KEY,
  name     VARCHAR(100) NOT NULL,
  git_nick VARCHAR(100) NOT NULL
);

CREATE TABLE tasks (
  id            INT PRIMARY KEY                         AUTO_INCREMENT,
  user_id       INT REFERENCES users (id),
  repo_id       INT,
  register_time TIMESTAMP NOT NULL,
  status        ENUM ('PROGRESS', 'CHECKED')            DEFAULT 'PROGRESS',
  successful    BOOL                                    DEFAULT FALSE,
  log           JSON,
  FOREIGN KEY (repo_id) REFERENCES repos (id)
    ON DELETE CASCADE
);

CREATE TABLE contacts (
  id      INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  type    VARCHAR(100) NOT NULL,
  inf     VARCHAR(100) NOT NULL,
  enabled BOOL,
  FOREIGN KEY (user_id) REFERENCES users (id)
    ON DELETE CASCADE
);

# CREATE TRIGGER unique_telegram_contact
# BEFORE INSERT ON contacts
# FOR EACH ROW
#   BEGIN
#         DECLARE flag INT;
#         SELECT COUNT(*)
#         INTO flag
#         FROM contacts
#         WHERE NEW.type = 'TELEGRAM' AND NEW.type = contacts.type;
#         IF (flag > 0)
#         THEN
#           SIGNAL SQLSTATE '45000'
#           SET MESSAGE_TEXT = 'Repeated telegram contact';
#         END IF;
#   END;