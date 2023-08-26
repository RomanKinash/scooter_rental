CREATE TABLE if not exists rentals
(
    id                 bigint NOT NULL AUTO_INCREMENT,
    rental_date        DATETIME   NOT NULL,
    return_date        DATETIME   NOT NULL,
    actual_return_date DATETIME,
    scooter_id             bigint NOT NULL,
    user_id            bigint NOT NULL,
    PRIMARY KEY (id),
    KEY user_fk_idx (user_id),
    KEY scooter_fk_idx (scooter_id),
    CONSTRAINT scooter_fk FOREIGN KEY (scooter_id) REFERENCES scooters (id),
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
