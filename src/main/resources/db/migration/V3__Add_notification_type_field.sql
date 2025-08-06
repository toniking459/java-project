
DO $$ 
BEGIN 
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                  WHERE table_name='notifications' AND column_name='type') THEN
        ALTER TABLE notifications ADD COLUMN type VARCHAR(50);
    END IF;
END $$;


UPDATE notifications SET type = 'GENERAL' WHERE type IS NULL;