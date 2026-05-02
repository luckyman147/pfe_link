---
description: How students create projects, invite friends and advisors
---

# Project Management Workflow

This workflow describes the step-by-step process for students to initiate projects and build their teams.

## 1. Project Creation
- **Actor**: Any validated Student.
- **Action**: Use the "Create Project" button on the Dashboard.
- **Constraint**: A student can only lead one project per active season.
- **Result**: A new `Project` is created in `DRAFT` status.

## 2. Inviting Friends (Collaborators)
- **Actor**: Project Owner (Creator).
- **Action**: Click "Invite Member" and enter the friend's email or student ID.
- **Notification**: The friend receives an in-app and email notification for the project invitation.
- **Constraint**: Friends must be in the same faculty as the owner.

## 3. Team Acceptance
- **Actor**: Invited Student.
- **Action**: Review the invitation in the "My Notifications" or "Invites" section and click "Accept".
- **Result**: The student is added to the project `members` list.

## 4. Inviting an Advisor
- **Actor**: Project Owner.
- **Action**: Once the team is formed, navigate to "Find Advisors". After selecting an advisor, click "Invite to Project".
- **Requirement**: The project must have at least one member (the owner).
- **Result**: A `SelectionRequest` is sent to the advisor, linked to the project.

## 5. Advisor Approval
- **Actor**: Advisor.
- **Action**: Review the project description and the list of student members. Click "Approve" (or "Reject").
- **Result**: 
  - On **Approve**: The project status moves to `APPROVED`, the advisor is assigned, and student capacities are updated.
  - On **Reject**: The owner can invite a different advisor.
