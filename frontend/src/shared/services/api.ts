import config from '@/config/env';
import axios from 'axios';

// Create Axios instance with centralized config
const api = axios.create({
  baseURL: config.api.baseUrl,
  withCredentials: true, // Crucial for HttpOnly cookies
  xsrfCookieName: 'XSRF-TOKEN',
  xsrfHeaderName: 'X-XSRF-TOKEN',
  headers: {
    'Content-Type': 'application/json',
  },
});

interface FailedRequest {
  resolve: (value?: unknown) => void;
  reject: (reason?: unknown) => void;
}

let isRefreshing = false;
let failedQueue: FailedRequest[] = [];

const processQueue = (error: Error | null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve();
    }
  });
  failedQueue = [];
};

// Response interceptor to handle background token rotation
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Handle 401 Unauthorized errors by attempting a transparent refresh
    if (error.response?.status === 401 && !originalRequest._retry) {
      
      // If the refresh endpoint itself returns 401, the session is completely dead
      if (originalRequest.url?.includes('/api/auth/refresh')) {
        localStorage.removeItem('user'); // User data is safe in localStorage, but tokens are in cookies
        window.location.href = '/login';
        return Promise.reject(error);
      }

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then(() => {
            return api(originalRequest);
          })
          .catch((err) => {
            return Promise.reject(err);
          });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // We don't need to send the refreshToken in the body anymore (backend reads from cookie)
        // but we send an empty body to satisfy POST requirements if needed.
        await axios.post(`${config.api.baseUrl}/api/auth/refresh`, {}, { withCredentials: true });
        
        processQueue(null);
        return api(originalRequest);
      } catch (refreshError: any) {
        processQueue(refreshError);
        localStorage.removeItem('user');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default api;
