/**
 * Centralized configuration for all environment variables.
 * All URLs and secrets should be accessed through this file.
 */

export const config = {
  // Backend API URLs
  api: {
    baseUrl: import.meta.env.VITE_API_URL || '',
    authUrl: import.meta.env.VITE_AUTH_API || import.meta.env.VITE_API_URL || '',
    pfeUrl: import.meta.env.VITE_PFE_API || import.meta.env.VITE_API_URL || '',
    uploadUrl: import.meta.env.VITE_UPLOAD_SERVICE_URL || '',
  },

  // App URLs
  app: {
    url: import.meta.env.VITE_APP_URL || '',
  },
} as const;

export type Config = typeof config;

export default config;
