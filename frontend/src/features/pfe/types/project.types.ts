export type ProjectStatus = 'DRAFT' | 'PENDING_ADVISOR' | 'APPROVED' | 'REJECTED';

export interface TeamMember {
  id: string;
  userId: string;
  fullName: string;
  email: string;
  role: 'OWNER' | 'MEMBER';
}

export interface ProjectData {
  id: string;
  title: string;
  description: string;
  status: ProjectStatus;
  owner: TeamMember;
  advisor?: {
    id: string;
    fullName: string;
    email: string;
  };
  members: TeamMember[];
  createdAt: string;
}

export interface ProjectInvitation {
  id: string;
  projectId: string;
  projectTitle: string;
  ownerFullName: string;
  ownerEmail: string;
  createdAt: string;
}

export interface SelectionRequest {
  id: string;
  projectId: string;
  advisor: {
    id: string;
    fullName: string;
    email: string;
  };
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  message: string;
  createdAt: string;
}

export interface UploadedFile {
  draftId: string;
  publicId: string;
  url: string;
  fileName: string;
  mimeType: string;
  type: 'image' | 'video';
  uploadedAt: string;
}

export interface StudentProfileResponse {
  id: string;
  userId: string;
  fullName: string;
  email: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
  facultyId: string;
}
