import axiosClient from './axiosClient';

export const categoryService = {
  create: async (payload) => {
    return axiosClient.post('/jpay/categories', payload);
  },
  upsertBudget: async (payload) => {
    return axiosClient.post('/jpay/categories/upsertBudget', payload);
  },
  getMyCategories: async () => {
    return axiosClient.get('/jpay/categories/my-categories');
  }
};
