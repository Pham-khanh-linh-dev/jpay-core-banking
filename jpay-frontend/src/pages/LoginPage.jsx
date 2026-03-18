import { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate, Link } from 'react-router-dom';

export default function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      setLoading(true);
      await login(username, password);
      navigate('/');
    } catch (err) {
      setError(err?.message || 'Failed to login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2 className="auth-title">Welcome to JPay</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Username</label>
            <input 
              className="form-input" 
              type="text" 
              value={username} 
              onChange={e => setUsername(e.target.value)} 
              required
            />
          </div>
          <div className="form-group">
            <label className="form-label">Password</label>
            <input 
              className="form-input" 
              type="password" 
              value={password} 
              onChange={e => setPassword(e.target.value)} 
              required
            />
          </div>
          {error && <div className="error-msg">{error}</div>}
          <button className="btn btn-primary mt-4" disabled={loading} type="submit">
            {loading ? 'Logging in...' : 'Sign In'}
          </button>
        </form>
        <p className="text-center mt-4 text-sm text-muted">
          Don't have an account? <Link to="/register" style={{color: 'var(--primary)'}}>Register here</Link>
        </p>
      </div>
    </div>
  );
}
