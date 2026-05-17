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

    @Value("${spring.mail.username:iyedtouati@gmail.com}")
    private String fromEmail;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    public void sendVerificationEmail(String to, String name, String token) {
        String link = buildFrontendVerifyEmailLink(token);
        String html = buildTemplate("Email Verification",
                "Hello " + name + ", please verify your email by clicking the link below:",
                link, "Verify Email");
        send(to, "Verify your PFE-Link email", html);
    }

    public void sendPasswordResetOtp(String to, String name, String otp) {
        String html = buildTemplate("Password Reset",
                "Hello " + name + ", your password reset OTP is: <strong>" + otp + "</strong>",
                null, null);
        send(to, "PFE-Link Password Reset OTP", html);
    }

    public void sendWelcomeEmail(String to, String name) {
        String html = buildTemplate("Welcome to PFE-Link",
                "Hello " + name + ", your account has been created. Please wait for admin approval.",
                null, null);
        send(to, "Welcome to PFE-Link", html);
    }

    public void sendApprovalEmail(String to, String name) {
        String html = buildTemplate("Account Approved",
                "Hello " + name + ", your account has been approved. You can now log in!",
                "http://localhost:3000/login", "Login Now");
        send(to, "PFE-Link Account Approved", html);
    }

    public void sendFacultyApprovalEmail(String to, FacultyEmailData data) {
        String approveUrl = "http://localhost:8080/api/faculty-action/approve/" + data.pendingId;
        String rejectUrl = "http://localhost:8080/api/faculty-action/reject/" + data.pendingId;
        String html = buildFacultyApprovalTemplate(data, approveUrl, rejectUrl);
        send(to, "New Faculty Pending Approval: " + data.name, html);
    }

    public void sendFacultyApprovedEmail(String to, String facultyName) {
        String html = buildTemplate("Faculty Approved",
            "Congratulations! Your faculty <strong>" + facultyName
                + "</strong> has been approved and is now live on PFE-Link.",
            "http://localhost:3000/login", "Access Dashboard");
        send(to, "Faculty Approved: " + facultyName, html);
    }

    public void sendFacultyRejectedEmail(String to, String facultyName) {
        String html = buildTemplate("Faculty Rejected",
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
        String html = buildTemplate("New User Registered",
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
        String approveUrl = "http://localhost:8080/api/faculty-action/approve-advisor?advisorId=" + data.advisorId + "&facultyId=" + data.facultyId;
        String rejectUrl = "http://localhost:8080/api/faculty-action/reject-advisor?advisorId=" + data.advisorId + "&facultyId=" + data.facultyId;
        
        String html = buildAdvisorApprovalTemplate(data, approveUrl, rejectUrl);

        send(facultyEmail, "Advisor Approval Required: " + data.name, html);
    }

    public void sendAdvisorAssignmentApprovedEmail(String to, String advisorName, 
                                                   String facultyName, String seasonName) {
        String html = buildTemplate("Faculty Assignment Approved",
            "Congratulations " + advisorName + "! Your request to join the faculty <strong>" 
                + facultyName + "</strong> for the " + seasonName 
                + " academic season has been approved.",
            "http://localhost:3000/login", "Access Dashboard");
        send(to, "Faculty Assignment Approved: " + facultyName, html);
    }

    public void sendStudentApprovalEmail(String facultyEmail, StudentEmailData data) {
        String approveUrl = "http://localhost:8080/api/faculty-action/approve-student?id=" + data.profileId;
        String rejectUrl = "http://localhost:8080/api/faculty-action/reject-student?id=" + data.profileId;
        
        String html = buildStudentApprovalTemplate(data, approveUrl, rejectUrl);
            
        send(facultyEmail, "Student Approval Required: " + data.name, html);
    }

    public void sendStudentProfileApprovedEmail(String to, String studentName, String facultyName) {
        String html = buildTemplate("Student Profile Approved",
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
        String html = buildTemplate(subject, body, null, null);
        send(to, subject, html);
    }

    public void sendSelectionRequestToAdvisorEmail(String advisorEmail, SelectionEmailData data) {
        String approveUrl = "http://localhost:8080/api/faculty-action/approve-selection?id=" + data.requestId;
        String rejectUrl = "http://localhost:8080/api/faculty-action/reject-selection?id=" + data.requestId;
        String viewUrl = "http://localhost:3000/advisor/requests"; // App link
        
        String html = buildSelectionRequestTemplate(data, approveUrl, rejectUrl, viewUrl);
        send(advisorEmail, "Student Selection Request: " + data.studentName, html);
    }

    public void sendSelectionResponseToStudentEmail(String studentEmail, String studentName, 
                                                    String advisorName, String status) {
        String statusColor = status.equals("APPROVED") ? "#16A34A" : "#DC2626";
        String html = buildTemplate("Selection Request " + status,
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

    private String buildTemplate(String title, String body, String link, String linkText) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>").append(title).append("</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>").append(body).append("</p>");
        if (link != null && linkText != null) {
            sb.append("<a href='").append(link).append("' style='display:inline-block;background:#4F46E5;color:#fff;")
              .append("padding:12px 24px;text-decoration:none;border-radius:6px;margin-top:16px'>")
              .append(linkText).append("</a>");
        }
        sb.append("<hr style='margin-top:32px;border:none;border-top:1px solid #eee'/>");
        sb.append("<p style='color:#999;font-size:12px'>PFE-Link Platform</p></div>");
        return sb.toString();
    }

    private String buildActionTemplate(String title, String body, String label1, String value1,
                                     String label2, String value2, String approveUrl, String approveText,
                                     String rejectUrl, String rejectText) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>").append(title).append("</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>").append(body).append("</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        addRow(sb, label1, value1, rowStyle, labelStyle, valueStyle);
        addRow(sb, label2, value2, rowStyle, labelStyle, valueStyle);
        sb.append("</table>");

        sb.append("<div style='margin-top:24px'>");
        sb.append("<a href='").append(approveUrl).append("' style='display:inline-block;background:#16A34A;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px;margin-right:12px'>").append(approveText).append("</a>");
        sb.append("<a href='").append(rejectUrl).append("' style='display:inline-block;background:#DC2626;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px'>").append(rejectText).append("</a>");
        sb.append("</div>");
        sb.append("<hr style='margin-top:32px;border:none;border-top:1px solid #eee'/>");
        sb.append("<p style='color:#999;font-size:12px'>PFE-Link Platform</p></div>");
        return sb.toString();
    }

    private String buildSelectionRequestTemplate(SelectionEmailData data,
                                                  String approveUrl, String rejectUrl, String viewUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>New Student Selection Request</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>Hello ").append(data.advisorName)
          .append(", a student has requested you as their advisor:</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        addRow(sb, "Student Name", data.studentName, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Student Email", data.studentEmail, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Faculty", data.facultyName, rowStyle, labelStyle, valueStyle);
        if (data.topic != null) addRow(sb, "Proposed Topic", data.topic, rowStyle, labelStyle, valueStyle);
        sb.append("</table>");

        if (data.description != null && !data.description.isBlank()) {
            sb.append("<div style='margin:20px 0;padding:16px;background:#f9fafb;border-radius:8px'>");
            sb.append("<p style='color:#888;font-size:13px;margin:0 0 8px 0'>Message from student:</p>");
            sb.append("<p style='color:#444;font-size:14px;margin:0;line-height:1.5'>").append(data.description).append("</p>");
            sb.append("</div>");
        }

        sb.append("<div style='margin-top:24px'>");
        sb.append("<a href='").append(approveUrl).append("' style='display:inline-block;background:#16A34A;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px;margin-right:12px'>Approve Request</a>");
        sb.append("<a href='").append(rejectUrl).append("' style='display:inline-block;background:#DC2626;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px;margin-right:12px'>Reject Request</a>");
        sb.append("<a href='").append(viewUrl).append("' style='display:inline-block;color:#4B5563;text-decoration:underline;font-size:14px'>View in App</a>");
        sb.append("</div>");
        
        sb.append("<hr style='margin-top:32px;border:none;border-top:1px solid #eee'/>");
        sb.append("<p style='color:#999;font-size:12px'>PFE-Link Platform</p></div>");
        return sb.toString();
    }

    private String buildAdvisorApprovalTemplate(AdvisorEmailData data,
                                                 String approveUrl, String rejectUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>Advisor Approval Required</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>A new advisor has requested to join your faculty. Please review their credentials:</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        addRow(sb, "Advisor Name", data.name, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Advisor Email", data.email, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Telephone", data.telephone, rowStyle, labelStyle, valueStyle);
        addRow(sb, "CIN Number", data.cinNumber, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Proposed Role", data.role, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Capacity", String.valueOf(data.capacity), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Faculty Domain Email", data.facultyDomainEmail, rowStyle, labelStyle, valueStyle);
        sb.append("</table>");

        if (data.proofUrl != null && !data.proofUrl.isBlank()) {
            sb.append("<div style='margin:20px 0'>");
            sb.append("<p style='color:#888;font-size:13px;margin-bottom:8px'>Professional Proof / Screenshot:</p>");
            sb.append("<img src='").append(data.proofUrl).append("' style='max-width:100%;border-radius:8px;border:1px solid #eee' />");
            sb.append("<p style='margin-top:8px'><a href='").append(data.proofUrl).append("' style='color:#1D4ED8;font-size:12px'>View full size</a></p>");
            sb.append("</div>");
        }

        sb.append("<div style='margin-top:24px'>");
        sb.append("<a href='").append(approveUrl).append("' style='display:inline-block;background:#16A34A;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px;margin-right:12px'>Approve Advisor</a>");
        sb.append("<a href='").append(rejectUrl).append("' style='display:inline-block;background:#DC2626;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px'>Reject Request</a>");
        sb.append("</div>");
        sb.append("<hr style='margin-top:32px;border:none;border-top:1px solid #eee'/>");
        sb.append("<p style='color:#999;font-size:12px'>PFE-Link Platform</p></div>");
        return sb.toString();
    }

    private String buildStudentApprovalTemplate(StudentEmailData data,
                                                 String approveUrl, String rejectUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>Student Profile Approval Required</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>A new student has joined your faculty and requires your review:</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        addRow(sb, "Student Name", data.name, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Student Email", data.email, rowStyle, labelStyle, valueStyle);
        addRow(sb, "CIN Number", data.cinNumber, rowStyle, labelStyle, valueStyle);
        sb.append("</table>");

        if (data.studentCardUrl != null && !data.studentCardUrl.isBlank()) {
            sb.append("<div style='margin:20px 0'>");
            sb.append("<p style='color:#888;font-size:13px;margin-bottom:8px'>Student Card:</p>");
            sb.append("<img src='").append(data.studentCardUrl).append("' style='max-width:100%;border-radius:8px;border:1px solid #eee' />");
            sb.append("</div>");
        }

        sb.append("<div style='margin-top:24px'>");
        sb.append("<a href='").append(approveUrl).append("' style='display:inline-block;background:#16A34A;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px;margin-right:12px'>Approve Student</a>");
        sb.append("<a href='").append(rejectUrl).append("' style='display:inline-block;background:#DC2626;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px'>Reject Student</a>");
        sb.append("</div>");
        sb.append("<hr style='margin-top:32px;border:none;border-top:1px solid #eee'/>");
        sb.append("<p style='color:#999;font-size:12px'>PFE-Link Platform</p></div>");
        return sb.toString();
    }

    private String buildFacultyApprovalTemplate(FacultyEmailData data,
                                                  String approveUrl, String rejectUrl) {
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>New Faculty Pending Approval</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>A new faculty has been submitted for your review:</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        addRow(sb, "Faculty Name", data.name, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Abbreviation", data.abbreviation, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Email", data.email, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Website", data.websiteUrl, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Governorate", data.governorate, rowStyle, labelStyle, valueStyle);
        addRow(sb, "City", data.city, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Street", data.street, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Postal Code", data.postalCode, rowStyle, labelStyle, valueStyle);
        addRow(sb, "Submitted By", data.submittedBy, rowStyle, labelStyle, valueStyle);
        sb.append("</table>");

        sb.append("<div style='margin-top:24px'>");
        sb.append("<a href='").append(approveUrl).append("' style='display:inline-block;background:#16A34A;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px;margin-right:12px'>Approve</a>");
        sb.append("<a href='").append(rejectUrl).append("' style='display:inline-block;background:#DC2626;color:#fff;")
          .append("padding:12px 24px;text-decoration:none;border-radius:6px'>Reject</a>");
        sb.append("</div>");
        sb.append("<hr style='margin-top:32px;border:none;border-top:1px solid #eee'/>");
        sb.append("<p style='color:#999;font-size:12px'>PFE-Link Platform</p></div>");
        return sb.toString();
    }

    private void addRow(StringBuilder sb, String label, String value,
                        String rowStyle, String labelStyle, String valueStyle) {
        if (value == null || value.isBlank()) return;
        sb.append("<tr style='").append(rowStyle).append("'>");
        sb.append("<td style='").append(labelStyle).append("'>").append(label).append("</td>");
        sb.append("<td style='").append(valueStyle).append("'>").append(value).append("</td>");
        sb.append("</tr>");
    }
}
