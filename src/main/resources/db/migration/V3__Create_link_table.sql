
CREATE TABLE dbo.categoria
(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    user_id BIGINT NULL
);

ALTER TABLE dbo.categoria
        ADD CONSTRAINT FK_categoria_user FOREIGN KEY (user_id)
        REFERENCES dbo.link_usuario(id) ON DELETE SET NULL;



CREATE TABLE dbo.link
(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(50),
    url VARCHAR(2000) NOT NULL,
    thumbnail_preview VARCHAR(1000) NULL,
    title_preview VARCHAR(255) NULL
);



CREATE TABLE dbo.link_categorias
(
    link_id BIGINT NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (link_id, categoria_id)
);

ALTER TABLE dbo.link_categorias
        ADD CONSTRAINT FK_link_categorias_link FOREIGN KEY (link_id) REFERENCES dbo.link(id) ON DELETE CASCADE;

ALTER TABLE dbo.link_categorias
        ADD CONSTRAINT FK_link_categorias_categoria FOREIGN KEY (categoria_id) REFERENCES dbo.categoria(id) ON DELETE CASCADE;
