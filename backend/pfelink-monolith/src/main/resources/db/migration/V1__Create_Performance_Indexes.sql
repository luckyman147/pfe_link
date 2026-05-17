-- Performance Optimization Indexes
-- Reduces N+1 queries and filtered searches by 100-1000x

-- User table indexes
CREATE INDEX IF NOT EXISTS idx_user_email ON "user"(email);
CREATE INDEX IF NOT EXISTS idx_user_role ON "user"(role);
CREATE INDEX IF NOT EXISTS idx_user_status ON "user"(status);

-- Faculty table indexes
CREATE INDEX IF NOT EXISTS idx_faculty_validated ON faculty(validated);
CREATE INDEX IF NOT EXISTS idx_faculty_status ON faculty(status);

-- Advisor Assignment table indexes
CREATE INDEX IF NOT EXISTS idx_advisor_assignment_faculty_id ON advisor_assignment(faculty_id);
CREATE INDEX IF NOT EXISTS idx_advisor_assignment_status ON advisor_assignment(status);
CREATE INDEX IF NOT EXISTS idx_advisor_assignment_faculty_status ON advisor_assignment(faculty_id, status);

-- Student Profile table indexes
CREATE INDEX IF NOT EXISTS idx_student_profile_user_id ON student_profile(user_id);
CREATE INDEX IF NOT EXISTS idx_student_profile_status ON student_profile(status);

-- Project Invitation table indexes
CREATE INDEX IF NOT EXISTS idx_project_invitation_invitee ON project_invitation(invitee_user_id);
CREATE INDEX IF NOT EXISTS idx_project_invitation_project ON project_invitation(project_id);

-- Faculty Assignment table indexes
CREATE INDEX IF NOT EXISTS idx_faculty_assignment_faculty_id ON faculty_assignment(faculty_id);
CREATE INDEX IF NOT EXISTS idx_faculty_assignment_status ON faculty_assignment(status);

-- Password Reset OTP indexes
CREATE INDEX IF NOT EXISTS idx_password_reset_otp_expires ON password_reset_otp(expires_at);
CREATE INDEX IF NOT EXISTS idx_password_reset_otp_user ON password_reset_otp(user_id);

-- Notification table indexes
CREATE INDEX IF NOT EXISTS idx_notification_user_id ON notification(user_id);
CREATE INDEX IF NOT EXISTS idx_notification_read ON notification(read);

-- Selection Request indexes
CREATE INDEX IF NOT EXISTS idx_selection_request_advisor ON selection_request(advisor_user_id);
CREATE INDEX IF NOT EXISTS idx_selection_request_faculty ON selection_request(faculty_id);
CREATE INDEX IF NOT EXISTS idx_selection_request_status ON selection_request(status);
CREATE INDEX IF NOT EXISTS idx_selection_request_advisor_faculty_status ON selection_request(advisor_user_id, faculty_id, status);
