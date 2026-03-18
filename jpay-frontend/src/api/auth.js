import axiosClient from './axiosClient';

export const authService = {
  login: async (username, password) => {
    return axiosClient.post('/jpay/authen/login', { username, password });
  },
  logout: async (token) => {
    return axiosClient.post('/jpay/authen/logout', { token });
  },
  refresh: async (token) => {
    return axiosClient.post('/jpay/authen/refresh', { token });
  }
};
