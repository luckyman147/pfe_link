package com.pfelink.monolith.infrastructure.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateBuilder templateBuilder;

    @Value("${spring.mail.username:iyedtouati@gmail.com}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public void sendVerificationEmail(String to, String name, String token) {
        String link = buildFrontendVerifyEmailLink(token);
        String html = templateBuilder.buildTemplate("Email Verification",
                "Hello " + name + ", please verify your email by clicking the link below:",
                link, "Verify Email");
        send(to, "Verify your PFE-Link email", html);
    }

    public void sendPasswordResetOtp(String to, String name, String otp) {
        String html = templateBuilder.buildTemplate("Password Reset",
                "Hello " + name + ", your password reset OTP is: <strong>" + otp + "</strong>",
                null, null);
        send(to, "PFE-Link Password Reset OTP", html);
    }

    public void sendWelcomeEmail(String to, String name) {
        String html = templateBuilder.buildTemplate("Welcome to PFE-Link",
                "Hello " + name + ", your account has been created. Please wait for admin approval.",
                null, null);
        send(to, "Welcome to PFE-Link", html);
    }

    public void sendApprovalEmail(String to, String name) {
        String html = templateBuilder.buildTemplate("Account Approved",
                "Hello " + name + ", your account has been approved. You can now log in!",
                "http://localhost:3000/login", "Login Now");
        send(to, "PFE-Link Account Approved", html);
    }

    public void sendFacultyApprovalEmail(String to, FacultyEmailData data) {
        String approveUrl = "http://localhost:8080/api/faculty-action/approve/" + data.pendingId();
        String rejectUrl = "http://localhost:8080/api/faculty-action/reject/" + data.pendingId();
        String html = templateBuilder.buildFacultyApprovalTemplate(data, approveUrl, rejectUrl);
        send(to, "New Faculty Pending Approval: " + data.name(), html);
    }

    public void sendFacultyApprovedEmail(String to, String facultyName) {
        String html = templateBuilder.buildTemplate("Faculty Approved",
            "Congratulations! Your faculty <strong>" + facultyName
                + "</strong> has been approved and is now live on PFE-Link.",
            "http://localhost:3000/login", "Access Dashboard");
        send(to, "Faculty Approved: " + facultyName, html);
    }

    public void sendFacultyRejectedEmail(String to, String facultyName) {
        String html = templateBuilder.buildTemplate("Faculty Rejected",
            "We regret to inform you that your faculty <strong>" + facultyName
                + "</strong> has been rejected. Please contact the admin for more details.",
            null, null);
        send(to, "Faculty Rejected: " + facultyName, html);
    }

    public record FacultyEmailData(
        String pendingId, String name, String abbreviation,
        String email, String websiteUrl, String imageUrl,
        String governorate, String city, String street,
        String postalCode, String submittedBy
    ) {}

    public void sendUserJoinedFacultyEmail(String facultyEmail, String userName,
                                            String userEmail, String userRole) {
        String html = templateBuilder.buildTemplate("New User Registered",
            "A new " + userRole.toLowerCase() + " has verified their email and joined PFE-Link:"
                + "<br/><br/><strong>Name:</strong> " + userName
                + "<br/><strong>Email:</strong> " + userEmail
                + "<br/><strong>Role:</strong> " + userRole,
            null, null);
        send(facultyEmail, "New " + userRole + " registered: " + userName, html);
    }

    public record AdvisorEmailData(
        String advisorId, String facultyId, String name, String email,
        String telephone, String cinNumber, String role, Integer capacity,
        String facultyDomainEmail, String proofUrl
    ) {}

    public void sendAdvisorApprovalEmail(String facultyEmail, AdvisorEmailData data) {
        String approveUrl = "http://localhost:8080/api/faculty-action/approve-advisor?advisorId=" + data.advisorId() + "&facultyId=" + data.facultyId();
        String rejectUrl = "http://localhost:8080/api/faculty-action/reject-advisor?advisorId=" + data.advisorId() + "&facultyId=" + data.facultyId();
        String html = templateBuilder.buildAdvisorApprovalTemplate(data, approveUrl, rejectUrl);
        send(facultyEmail, "Advisor Approval Required: " + data.name(), html);
    }

    public void sendAdvisorAssignmentApprovedEmail(String to, String advisorName,
                                                   String facultyName, String seasonName) {
        String html = templateBuilder.buildTemplate("Faculty Assignment Approved",
            "Congratulations " + advisorName + "! Your request to join the faculty <strong>"
                + facultyName + "</strong> for the " + seasonName
                + " academic season has been approved.",
            "http://localhost:3000/login", "Access Dashboard");
        send(to, "Faculty Assignment Approved: " + facultyName, html);
    }

    public void sendStudentApprovalEmail(String facultyEmail, StudentEmailData data) {
        String approveUrl = "http://localhost:8080/api/faculty-action/approve-student?id=" + data.profileId();
        String rejectUrl = "http://localhost:8080/api/faculty-action/reject-student?id=" + data.profileId();
        String html = templateBuilder.buildStudentApprovalTemplate(data, approveUrl, rejectUrl);
        send(facultyEmail, "Student Approval Required: " + data.name(), html);
    }

    public void sendStudentProfileApprovedEmail(String to, String studentName, String facultyName) {
        String html = templateBuilder.buildTemplate("Student Profile Approved",
            "Congratulations " + studentName + "! Your student profile for the faculty <strong>"
                + facultyName + "</strong> has been approved. You can now start selecting advisors.",
            "http://localhost:3000/login", "Access Dashboard");
        send(to, "Student Profile Approved: " + facultyName, html);
    }

    public record StudentEmailData(
        String profileId, String name, String email,
        String cinNumber, String studentCardUrl
    ) {}

    public record SelectionEmailData(
        String requestId, String studentName, String studentEmail,
        String advisorName, String facultyName, String topic, String description
    ) {}

    public void sendGenericNotification(String to, String subject, String body) {
        String html = templateBuilder.buildTemplate(subject, body, null, null);
        send(to, subject, html);
    }

    public void sendSelectionRequestToAdvisorEmail(String advisorEmail, SelectionEmailData data) {
        String approveUrl = "http://localhost:8080/api/faculty-action/approve-selection?id=" + data.requestId();
        String rejectUrl = "http://localhost:8080/api/faculty-action/reject-selection?id=" + data.requestId();
        String viewUrl = "http://localhost:3000/advisor/requests";
        String html = templateBuilder.buildSelectionRequestTemplate(data, approveUrl, rejectUrl, viewUrl);
        send(advisorEmail, "Student Selection Request: " + data.studentName(), html);
    }

    public void sendSelectionResponseToStudentEmail(String studentEmail, String studentName,
                                                    String advisorName, String status) {
        String statusColor = status.equals("APPROVED") ? "#16A34A" : "#DC2626";
        String html = templateBuilder.buildTemplate("Selection Request " + status,
            "Hello " + studentName + ", your advisor selection request for <strong>"
                + advisorName + "</strong> has been <span style='color:" + statusColor
                + ";font-weight:bold'>" + status.toLowerCase() + "</span>.",
            "http://localhost:3000/login", "Check Status");
        send(studentEmail, "Selection Request Update: " + status, html);
    }

    private String buildFrontendVerifyEmailLink(String token) {
        String base = (frontendUrl == null || frontendUrl.isBlank())
            ? "http://localhost:5173" : frontendUrl.stripTrailing();
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/auth/verify-email/confirm?token=" + token;
    }

    private void send(String to, String subject, String html) {
        try {
            var msg = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(msg);
            log.info("Email sent to {}: {}", to, subject);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
