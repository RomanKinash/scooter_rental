CREATE TABLE IF NOT EXISTS payments
(
    id            bigint NOT NULL AUTO_INCREMENT,
    payment_status        VARCHAR(255) DEFAULT NULL,
    payment_type          VARCHAR(255) DEFAULT NULL,
    payment_url   VARCHAR(10000) DEFAULT NULL,
    payment_session_id    VARCHAR(255) DEFAULT NULL,
    payment_amount DECIMAL      DEFAULT NULL,
    rental_id bigint DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT rental_fk FOREIGN KEY (rental_id) REFERENCES rentals (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
