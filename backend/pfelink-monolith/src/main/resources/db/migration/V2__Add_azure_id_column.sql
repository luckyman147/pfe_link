-- Add azure_id column to users table
ALTER TABLE users ADD COLUMN IF NOT EXISTS azure_id VARCHAR(255) UNIQUE;

-- Create index for fast lookups by azure_id
CREATE INDEX IF NOT EXISTS idx_users_azure_id ON users(azure_id);
