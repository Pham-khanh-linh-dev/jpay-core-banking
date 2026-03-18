import { useState } from 'react';
import { userService } from '../api/user';
import { useNavigate, Link } from 'react-router-dom';

export default function RegisterPage() {
  const [formData, setFormData] = useState({ 
    username: '', password: '', fullName: '', email: '', dob: '1990-01-01' 
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      setLoading(true);
      await userService.create(formData);
      navigate('/login');
    } catch (err) {
      setError(err?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({...formData, [e.target.name]: e.target.value});
  };

  return (
    <div className="auth-container">
      <div className="auth-card" style={{maxWidth: '500px'}}>
        <h2 className="auth-title">Create an Account</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Username</label>
            <input className="form-input" name="username" type="text" onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label className="form-label">Password</label>
            <input className="form-input" name="password" type="password" onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label className="form-label">Full Name</label>
            <input className="form-input" name="fullName" type="text" onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input className="form-input" name="email" type="email" onChange={handleChange} required />
          </div>
          <div className="form-group">
            <label className="form-label">Date of Birth</label>
            <input className="form-input" name="dob" type="date" value={formData.dob} onChange={handleChange} required />
          </div>
          {error && <div className="error-msg">{error}</div>}
          <button className="btn btn-primary mt-4" disabled={loading} type="submit">
            {loading ? 'Creating...' : 'Register'}
          </button>
        </form>
        <p className="text-center mt-4 text-sm text-muted">
          Already have an account? <Link to="/login" style={{color: 'var(--primary)'}}>Sign back in</Link>
        </p>
      </div>
    </div>
  );
}
