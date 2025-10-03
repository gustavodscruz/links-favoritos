ALTER TABLE dbo.link
ADD favorited BIT DEFAULT 0;
GO

UPDATE dbo.link
SET favorited = 0
WHERE favorited IS NULL;
GO
