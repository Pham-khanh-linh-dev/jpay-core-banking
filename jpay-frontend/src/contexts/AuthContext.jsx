import { createContext, useState, useEffect, useContext } from 'react';
import { authService } from '../api/auth';
import { userService } from '../api/user';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initAuth = async () => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const userInfo = await userService.getMyInfo();
          setUser(userInfo);
        } catch (error) {
          console.error("Failed to load user info", error);
          localStorage.removeItem('token');
        }
      }
      setLoading(false);
    };
    initAuth();
  }, []);

  const login = async (username, password) => {
    const response = await authService.login(username, password);
    const token = response.token;
    localStorage.setItem('token', token);
    try {
      const userInfo = await userService.getMyInfo();
      setUser(userInfo);
      return response;
    } catch (error) {
      localStorage.removeItem('token');
      throw new Error('Đăng nhập thành công nhưng không thể tải thông tin tài khoản!');
    }
  };

  const logout = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        await authService.logout(token);
      }
    } catch (e) {
      console.error(e);
    } finally {
      localStorage.removeItem('token');
      setUser(null);
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, loading, setUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
