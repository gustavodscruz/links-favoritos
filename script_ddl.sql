-- --------------------------------------------------
-- Combined DDL Script (manual execution sequence)
-- --------------------------------------------------

-- V0: Create database (run this manually in SQL Server)
--   CREATE DATABASE linksdb;

-- V1: Create link_usuario table
CREATE TABLE dbo.link_usuario (
	id BIGINT IDENTITY(1,1) PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL
);

-- V2: Insert initial users
INSERT INTO dbo.link_usuario (name, email, password)
VALUES ('Gustavo Dias', 'gustavodiasdsc@gmail.com',
		'$2a$12$FvuzAyZhvzmBHBXYg3KFTuvUZuclqZwKDNJjkvQclYW2ebEfUihoe');

-- V3: Create categoria and link tables with relationships
CREATE TABLE dbo.categoria (
	id BIGINT IDENTITY(1,1) PRIMARY KEY,
	name VARCHAR(50) NOT NULL,
	user_id BIGINT NULL
);
ALTER TABLE dbo.categoria
	ADD CONSTRAINT FK_categoria_user FOREIGN KEY (user_id)
	REFERENCES dbo.link_usuario(id) ON DELETE SET NULL;

CREATE TABLE dbo.link (
	id BIGINT IDENTITY(1,1) PRIMARY KEY,
	name VARCHAR(50),
	url VARCHAR(2000) NOT NULL,
	thumbnail_preview VARCHAR(1000) NULL,
	title_preview VARCHAR(255) NULL
);

CREATE TABLE dbo.link_categorias (
	link_id BIGINT NOT NULL,
	categoria_id BIGINT NOT NULL,
	PRIMARY KEY (link_id, categoria_id)
);
ALTER TABLE dbo.link_categorias
	ADD CONSTRAINT FK_link_categorias_link FOREIGN KEY (link_id)
	REFERENCES dbo.link(id) ON DELETE CASCADE;
ALTER TABLE dbo.link_categorias
	ADD CONSTRAINT FK_link_categorias_categoria FOREIGN KEY (categoria_id)
	REFERENCES dbo.categoria(id) ON DELETE CASCADE;

-- V3.1: Alter link table to add user_id FK
ALTER TABLE dbo.link
	ADD user_id BIGINT NULL;
ALTER TABLE dbo.link
	ADD CONSTRAINT FK_link_user FOREIGN KEY (user_id)
	REFERENCES dbo.link_usuario(id) ON DELETE SET NULL;

-- V3.2: Add favorited column with default
ALTER TABLE dbo.link
	ADD favorited BIT DEFAULT 0;
UPDATE dbo.link
SET favorited = 0
WHERE favorited IS NULL;

-- V3.3: Add description column with default text
ALTER TABLE dbo.link
	ADD description VARCHAR(255);
UPDATE dbo.link
SET description = 'No description'
WHERE description IS NULL;
