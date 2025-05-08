
ALTER TABLE tournament_match ADD COLUMN venue_id BIGINT;


ALTER TABLE tournament_match
    ADD CONSTRAINT fk_tournament_match_venue FOREIGN KEY (venue_id) REFERENCES venue(id);
