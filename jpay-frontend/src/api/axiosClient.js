import axios from 'axios';

const axiosClient = axios.create({
  baseURL: '', // Using Vite proxy — requests go to same origin, proxied to backend
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor for Authorization token
axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Add a response interceptor to handle ApiResponse wrapper globally
axiosClient.interceptors.response.use(
  (response) => {
    // Backend returns { code: 1000, result: ... } for success
    const data = response.data;
    if (data && data.code !== undefined && data.code !== 1000) {
      return Promise.reject(new Error(data.message || 'API Error'));
    }
    return data.result !== undefined ? data.result : data;
  },
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    const msg = error.response?.data?.message || error.message || 'Network Error';
    return Promise.reject(new Error(msg));
  }
);

export default axiosClient;
