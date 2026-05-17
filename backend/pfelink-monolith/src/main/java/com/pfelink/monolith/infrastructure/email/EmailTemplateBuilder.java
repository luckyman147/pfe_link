package com.pfelink.monolith.infrastructure.email;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateBuilder {

    public String buildTemplate(String title, String body, String link, String linkText) {
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

    public String buildSelectionRequestTemplate(EmailService.SelectionEmailData data,
                                                  String approveUrl, String rejectUrl, String viewUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>New Student Selection Request</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>Hello ").append(data.advisorName())
          .append(", a student has requested you as their advisor:</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        addRow(sb, "Student Name", data.studentName(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Student Email", data.studentEmail(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Faculty", data.facultyName(), rowStyle, labelStyle, valueStyle);
        if (data.topic() != null) addRow(sb, "Proposed Topic", data.topic(), rowStyle, labelStyle, valueStyle);
        sb.append("</table>");

        if (data.description() != null && !data.description().isBlank()) {
            sb.append("<div style='margin:20px 0;padding:16px;background:#f9fafb;border-radius:8px'>");
            sb.append("<p style='color:#888;font-size:13px;margin:0 0 8px 0'>Message from student:</p>");
            sb.append("<p style='color:#444;font-size:14px;margin:0;line-height:1.5'>").append(data.description()).append("</p>");
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

    public String buildAdvisorApprovalTemplate(EmailService.AdvisorEmailData data,
                                                 String approveUrl, String rejectUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>Advisor Approval Required</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>A new advisor has requested to join your faculty. Please review their credentials:</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        addRow(sb, "Advisor Name", data.name(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Advisor Email", data.email(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Telephone", data.telephone(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "CIN Number", data.cinNumber(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Proposed Role", data.role(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Capacity", String.valueOf(data.capacity()), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Faculty Domain Email", data.facultyDomainEmail(), rowStyle, labelStyle, valueStyle);
        sb.append("</table>");

        if (data.proofUrl() != null && !data.proofUrl().isBlank()) {
            sb.append("<div style='margin:20px 0'>");
            sb.append("<p style='color:#888;font-size:13px;margin-bottom:8px'>Professional Proof / Screenshot:</p>");
            sb.append("<img src='").append(data.proofUrl()).append("' style='max-width:100%;border-radius:8px;border:1px solid #eee' />");
            sb.append("<p style='margin-top:8px'><a href='").append(data.proofUrl()).append("' style='color:#1D4ED8;font-size:12px'>View full size</a></p>");
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

    public String buildStudentApprovalTemplate(EmailService.StudentEmailData data,
                                                 String approveUrl, String rejectUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>Student Profile Approval Required</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>A new student has joined your faculty and requires your review:</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        addRow(sb, "Student Name", data.name(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Student Email", data.email(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "CIN Number", data.cinNumber(), rowStyle, labelStyle, valueStyle);
        sb.append("</table>");

        if (data.studentCardUrl() != null && !data.studentCardUrl().isBlank()) {
            sb.append("<div style='margin:20px 0'>");
            sb.append("<p style='color:#888;font-size:13px;margin-bottom:8px'>Student Card:</p>");
            sb.append("<img src='").append(data.studentCardUrl()).append("' style='max-width:100%;border-radius:8px;border:1px solid #eee' />");
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

    public String buildFacultyApprovalTemplate(EmailService.FacultyEmailData data,
                                                  String approveUrl, String rejectUrl) {
        String rowStyle = "padding:8px 0;border-bottom:1px solid #f0f0f0";
        String labelStyle = "color:#888;font-size:13px;width:140px;vertical-align:top";
        String valueStyle = "color:#333;font-size:14px;padding-left:12px";

        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;padding:20px'>");
        sb.append("<h2 style='color:#333'>New Faculty Pending Approval</h2>");
        sb.append("<p style='color:#666;line-height:1.6'>A new faculty has been submitted for your review:</p>");

        sb.append("<table style='width:100%;border-collapse:collapse;margin:16px 0'>");
        addRow(sb, "Faculty Name", data.name(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Abbreviation", data.abbreviation(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Email", data.email(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Website", data.websiteUrl(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Governorate", data.governorate(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "City", data.city(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Street", data.street(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Postal Code", data.postalCode(), rowStyle, labelStyle, valueStyle);
        addRow(sb, "Submitted By", data.submittedBy(), rowStyle, labelStyle, valueStyle);
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
