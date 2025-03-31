
CREATE TABLE IF NOT EXISTS annonce (
    id SERIAL PRIMARY KEY,
    title VARCHAR(64) NOT NULL,
    description VARCHAR(256) NOT NULL,
    adress VARCHAR(64) NOT NULL,
    mail VARCHAR(64) NOT NULL,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


INSERT INTO annonce (title, description, adress, mail, date)
VALUES ('Titre Exemple', 'Ceci est une description exemple', 'Adresse Exemple', 'exemple@mail.com', CURRENT_TIMESTAMP);
