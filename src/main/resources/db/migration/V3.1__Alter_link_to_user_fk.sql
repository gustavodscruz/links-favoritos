ALTER TABLE dbo.link
        ADD user_id BIGINT NULL;

ALTER TABLE dbo.link
        ADD CONSTRAINT FK_link_user FOREIGN KEY (user_id)
        REFERENCES dbo.link_usuario(id) ON DELETE SET NULL;