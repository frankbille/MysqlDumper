# Create table structure

CREATE TABLE person
(
  id          INT PRIMARY KEY,
  person_name VARCHAR(50) NOT NULL,
  birth_date  DATE,
  gender      CHAR(1)
)
  ENGINE = InnoDB;


CREATE TABLE person_relationship
(
  person1_id        INT NOT NULL,
  person2_id        INT NOT NULL,
  relationship_type VARCHAR(20),
  PRIMARY KEY PK_PERSON_RELATIONSHIP (person1_id, person2_id),
  CONSTRAINT FOREIGN KEY FK_PERSON1 (person1_id) REFERENCES person (id),
  CONSTRAINT FOREIGN KEY FK_PERSON2 (person2_id) REFERENCES person (id)
)
  ENGINE = InnoDB;


CREATE TABLE pet
(
  id       INT PRIMARY KEY,
  pet_name VARCHAR(50) NOT NULL,
  type     VARCHAR(20) NOT NULL,
  owner    INT         NOT NULL,
  CONSTRAINT FOREIGN KEY FK_OWNER (owner) REFERENCES person (id)
)
  ENGINE = InnoDB;


CREATE TABLE accessories
(
  id             INT PRIMARY KEY,
  accessory_type VARCHAR(50) NOT NULL,
  color          CHAR(6)
)
  ENGINE = InnoDB;


CREATE TABLE pet_accessories
(
  pet_id       INT NOT NULL,
  accessory_id INT NOT NULL,
  PRIMARY KEY PK_PET_ACCESSORY (pet_id, accessory_id),
  CONSTRAINT FOREIGN KEY FK_PET (pet_id) REFERENCES pet (id),
  CONSTRAINT FOREIGN KEY FK_ACCESSORY (accessory_id) REFERENCES accessories (id)
)
  ENGINE = InnoDB;

# Insert a bunch of data
INSERT INTO person (id, person_name, birth_date, gender) VALUES
  (1, 'John', '1982-03-02', 'M'),
  (2, 'Jane', '1979-07-03', 'F'),
  (3, 'Joe', '1984-02-12', 'M');

INSERT INTO person_relationship (person1_id, person2_id, relationship_type) VALUES
  (1, 2, 'Married'),
  (1, 3, 'Friends');

INSERT INTO pet (id, pet_name, type, owner) VALUES
  (1, 'Coolio', 'Dog', 1),
  (2, 'Kitty', 'Cat', 2),
  (3, 'TMNT', 'Turtle', 3),
  (4, 'Squeaky', 'Parrot', 1),
  (5, 'Mighty', 'Mouse', 3),
  (6, 'Pretty', 'Parrot', 3);

INSERT INTO accessories (id, accessory_type, color) VALUES
  (1, 'Bracelet', 'F00F00'),
  (2, 'Birdcage', NULL),
  (3, 'Leash', '000000'),
  (4, 'Terrarium', NULL);

INSERT INTO pet_accessories (pet_id, accessory_id) VALUES
  (2, 1),
  (5, 1),
  (3, 4),
  (1, 3),
  (4, 2),
  (6, 2);