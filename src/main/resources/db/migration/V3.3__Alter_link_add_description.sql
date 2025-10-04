ALTER TABLE dbo.link
ADD description VARCHAR(255);
GO

UPDATE dbo.link
SET description = 'No description'
WHERE description IS NULL;

GO