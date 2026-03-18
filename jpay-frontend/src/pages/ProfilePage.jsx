import { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { userService } from '../api/user';
import { User as UserIcon } from 'lucide-react';

export default function ProfilePage() {
  const { user, setUser } = useAuth();
  const [formData, setFormData] = useState({
    fullName: user?.fullName || '',
    email: user?.email || '',
    dob: user?.dob || ''
  });
  const [msg, setMsg] = useState({ text: '', isError: false });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      setMsg({ text: '', isError: false });
      const updatedUser = await userService.update(user.id, formData);
      setUser(updatedUser);
      setMsg({ text: 'Cập nhật thành công!', isError: false });
    } catch (err) {
      setMsg({ text: err?.message || 'Cập nhật thất bại', isError: true });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Hồ sơ cá nhân</h1>
      </div>

      <div className="card" style={{maxWidth: '600px'}}>
        <div style={{display:'flex', alignItems:'center', gap:'1.5rem', marginBottom:'2rem'}}>
          <div style={{padding:'1.5rem', background:'var(--bg-gradient)', borderRadius:'50%', color:'var(--primary)'}}>
            <UserIcon size={48} />
          </div>
          <div>
            <h2 style={{margin:0}}>{user?.username}</h2>
            <p className="text-muted">User ID: {user?.id}</p>
          </div>
        </div>

        {msg.text && (
          <div style={{padding:'1rem', marginBottom:'1rem', borderRadius:'var(--radius)', background: msg.isError ? 'rgba(239,68,68,0.1)' : 'rgba(16,185,129,0.1)', color: msg.isError ? 'var(--danger)' : 'var(--success)'}}>
            {msg.text}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">Họ và tên</label>
            <input className="form-input" value={formData.fullName} onChange={e=>setFormData({...formData, fullName: e.target.value})} required />
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input className="form-input" type="email" value={formData.email} onChange={e=>setFormData({...formData, email: e.target.value})} required />
          </div>
          <div className="form-group">
            <label className="form-label">Ngày sinh</label>
            <input className="form-input" type="date" value={formData.dob} onChange={e=>setFormData({...formData, dob: e.target.value})} required />
          </div>
          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? 'Đang lưu...' : 'Lưu thay đổi'}
          </button>
        </form>
      </div>
    </div>
  );
}
