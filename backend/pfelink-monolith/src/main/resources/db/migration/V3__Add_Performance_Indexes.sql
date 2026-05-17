-- Performance optimization indexes for frequently queried columns

-- User table indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_email_verified ON users(email_verified);
CREATE INDEX IF NOT EXISTS idx_users_email_verification_token ON users(email_verification_token);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);

-- RefreshToken table indexes (if exists)
CREATE INDEX IF NOT EXISTS idx_refresh_token_user_id ON refresh_token(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_token_token ON refresh_token(token);

-- Composite index for common queries
CREATE INDEX IF NOT EXISTS idx_users_email_verified_composite ON users(email, email_verified);
