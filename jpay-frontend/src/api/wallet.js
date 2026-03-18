import axiosClient from './axiosClient';

export const walletService = {
  getMyWallet: async () => {
    return axiosClient.get('/jpay/wallet/my-wallet');
  },
  deposit: async (amount) => {
    return axiosClient.post('/jpay/wallet/deposit', { amount });
  },
  withdraw: async (amount) => {
    return axiosClient.post('/jpay/wallet/withdraw', { amount });
  },
  transfer: async ({ receivedUsername, amount, categoryId, message }) => {
    return axiosClient.post('/jpay/wallet/transfer', { receivedUsername, amount, categoryId, message });
  },
  getHistory: async () => {
    return axiosClient.get('/jpay/wallet/myTransferHistory');
  }
};
