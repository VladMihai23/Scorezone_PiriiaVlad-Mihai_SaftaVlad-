CREATE TABLE venue
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name     VARCHAR(255),
    city     VARCHAR(255),
    capacity INTEGER                                 NOT NULL,
    CONSTRAINT pk_venue PRIMARY KEY (id)
);