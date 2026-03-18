import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Home, Wallet, Send, History, Grid, User, LogOut } from 'lucide-react';

export default function Layout({ children }) {
  const { logout, user } = useAuth();
  const location = useLocation();

  const navLinks = [
    { name: 'Dashboard', path: '/', icon: Home },
    { name: 'Profile', path: '/profile', icon: User },
    { name: 'My Wallet', path: '/wallet', icon: Wallet },
    { name: 'Transfer', path: '/transfer', icon: Send },
    { name: 'History', path: '/history', icon: History },
    { name: 'Categories', path: '/categories', icon: Grid },
  ];

  return (
    <div className="layout">
      <div className="sidebar">
        <div style={{padding: '2rem 1.5rem', borderBottom: '1px solid var(--border)'}}>
          <h2 style={{color: 'var(--primary)', margin: 0}}>JPay Config</h2>
          <p className="text-sm text-muted mt-4">Xin chào, {user?.fullName || user?.username || 'User'}</p>
        </div>
        <div style={{display: 'flex', flexDirection: 'column', flex: 1, padding: '1rem 0'}}>
          {navLinks.map(link => {
            const Icon = link.icon;
            const isActive = location.pathname === link.path;
            return (
              <Link 
                key={link.path} 
                to={link.path} 
                className={`nav-item ${isActive ? 'active' : ''}`}
              >
                <Icon size={20} />
                <span>{link.name}</span>
              </Link>
            );
          })}
        </div>
        <div style={{padding: '1rem'}}>
          <button onClick={logout} className="btn btn-outline" style={{display:'flex', alignItems:'center', justifyContent:'center', gap:'0.5rem'}}>
            <LogOut size={18}/> Logout
          </button>
        </div>
      </div>
      <div className="main-content">
        {children}
      </div>
    </div>
  );
}
