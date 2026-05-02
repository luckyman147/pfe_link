export interface CreateFacultyRequest {
  name: string;
  abbreviation: string;
  email: string;
  websiteUrl?: string;
  imageUrl?: string;
  adminPassword?: string;
  
  // Detailed Address Fields
  path: string;
  displayName: string;
}

export interface FacultyResponse {
  id: string;
  name: string;
  abbreviation: string;
  email: string;
  websiteUrl?: string;
  imageUrl?: string;
  path: string;
  latitude: number;
  longitude: number;
  governorate: string;
  city: string;
  displayName: string;
}

export interface StudentProfile {
  id: string;
  userId: string;
  fullName: string;
  email: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  facultyId: string;
}

export interface AdvisorProfile {
  id: string;
  userId: string;
  fullName: string;
  email: string;
  telephone?: string;
  department?: string;
  specialization?: string;
  facultyId: string;
}

export interface SubmitSelectionRequest {
  studentUserId: string;
  advisorProfileId: string;
  facultyId: string;
  projectTitle: string;
  message: string;
}

export interface SelectionRequest {
  id: string;
  studentId: string;
  advisorId: string;
  projectTitle: string;
  message: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
}
