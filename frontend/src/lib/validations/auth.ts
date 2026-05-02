import { z } from "zod";

export const loginSchema = z.object({
  email: z.string().email("Email invalide"),
  password: z.string().min(8, "Le mot de passe doit contenir au moins 8 caractères"),
});

export const signupSchema = loginSchema.extend({
  confirmPassword: z.string()
}).refine((data) => data.password === data.confirmPassword, {
  message: "Les mots de passe ne correspondent pas",
  path: ["confirmPassword"],
});

export const onboardingStudentSchema = z.object({
  studentCard: z.string().url("Veuillez télécharger votre carte d'étudiant"),
});

export const onboardingTeacherSchema = z.object({
  supervisionCapacity: z.number().min(1, "La capacité doit être d'au moins 1"),
});

export type LoginInput = z.infer<typeof loginSchema>;
export type SignupInput = z.infer<typeof signupSchema>;
export type OnboardingStudentInput = z.infer<typeof onboardingStudentSchema>;
export type OnboardingTeacherInput = z.infer<typeof onboardingTeacherSchema>;
