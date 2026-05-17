/**
 * User Role Enum
 * Defines the different roles users can have in the system
 */
export enum UserRole {
  STUDENT = 'STUDENT',
  ADVISOR = 'ADVISOR',
  ADMIN = 'ADMIN',
  FACULTY_ADMIN = 'FACULTY_ADMIN',
}

/**
 * Account Status Enum
 * Defines the status of a user account
 */
export enum AccountStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  SUSPENDED = 'SUSPENDED',
}

/**
 * Student Status Enum
 * Defines the status specific to student accounts
 */
export enum StudentStatus {
  PENDING_APPROVAL = 'PENDING_APPROVAL',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  VERIFIED = 'VERIFIED',
  UNVERIFIED = 'UNVERIFIED',
}

/**
 * Verification Status Enum
 * Defines email/account verification status
 */
export enum VerificationStatus {
  VERIFIED = 'VERIFIED',
  UNVERIFIED = 'UNVERIFIED',
  PENDING = 'PENDING',
}

/**
 * Advisor Status Enum
 * Defines the status specific to advisor accounts
 */
export enum AdvisorStatus {
  PENDING_ASSIGNMENT = 'PENDING_ASSIGNMENT',
  ASSIGNED = 'ASSIGNED',
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
}
