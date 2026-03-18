import axiosClient from './axiosClient';

export const userService = {
  create: async (userData) => {
    return axiosClient.post('/jpay/users', userData);
  },
  update: async (userId, userData) => {
    return axiosClient.put(`/jpay/users/${userId}`, userData);
  },
  getAll: async () => {
    return axiosClient.get('/jpay/users');
  },
  getMyInfo: async () => {
    return axiosClient.get('/jpay/users/my-info');
  }
};
