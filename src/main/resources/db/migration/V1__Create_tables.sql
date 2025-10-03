
CREATE TABLE dbo.link_usuario
(
    id BIGINT IDENTITY(1,1) PRIMARY KEY ,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);
