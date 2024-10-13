CREATE TABLE IF NOT EXISTS Books (
    id SERIAL PRIMARY KEY,
    author VARCHAR(256) NOT NULL,
    title VARCHAR(256) NOT NULL,
    description TEXT NOT NULL,
    published INT NOT NULL,
    score INT NOT NULL
);


INSERT INTO Books (author, title, description, published, score) VALUES
('Александр Блок', 'Двенадцать', 'Поэма, описывающая революционные события в России.', 1999, 9);

INSERT INTO Books (author, title, description, published, score) VALUES
('Анна Ахматова', 'Реквием', 'Лирическое произведение о страданиях людей в сталинские репрессии.', 1999, 10);

INSERT INTO Books (author, title, description, published, score) VALUES
('Марина Цветаева', 'Волшебная сила', 'Стихи, отражающие внутренний мир и эмоции поэтессы.', 1999,  8);

INSERT INTO Books (author, title, description, published, score) VALUES
('Осип Мандельштам', 'Стихотворения', 'Сборник стихотворений, в которых автор говорит о жизни и судьбе.', 1999,  9);

INSERT INTO Books (author, title, description, published, score) VALUES
('Андрей Белый', 'Петербург', 'Роман о судьбах людей в контексте исторических событий начала 20 века.', 1999,  10);
