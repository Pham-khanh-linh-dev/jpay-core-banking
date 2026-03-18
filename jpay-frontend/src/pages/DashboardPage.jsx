import { useAuth } from '../contexts/AuthContext';
import { Activity, DollarSign } from 'lucide-react';

export default function DashboardPage() {
  const { user } = useAuth();
  
  return (
    <div>
      <div className="page-header">
        <h1>Dashboard</h1>
      </div>
      
      <div style={{display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '1.5rem'}}>
        <div className="card" style={{display:'flex', alignItems:'center', gap:'1.5rem'}}>
          <div style={{padding:'1rem', background:'rgba(79, 70, 229, 0.1)', borderRadius:'var(--radius)', color:'var(--primary)'}}>
            <Activity size={32} />
          </div>
          <div>
            <h3 className="text-muted text-sm">Xin chào</h3>
            <div style={{fontSize: '1.5rem', fontWeight: '600'}}>{user?.fullName || user?.username}</div>
          </div>
        </div>
        
        <div className="card" style={{display:'flex', alignItems:'center', gap:'1.5rem'}}>
          <div style={{padding:'1rem', background:'rgba(16, 185, 129, 0.1)', borderRadius:'var(--radius)', color:'var(--success)'}}>
            <DollarSign size={32} />
          </div>
          <div>
            <h3 className="text-muted text-sm">Trạng thái</h3>
            <div style={{fontSize: '1.5rem', fontWeight: '600'}}>Tài khoản hoạt động</div>
          </div>
        </div>
      </div>
      
      <div className="card mt-4">
        <h3>Thao tác nhanh</h3>
        <p className="text-muted mt-4">Sử dụng thanh điều hướng bên trái để truy cập ví, chuyển tiền, hoặc quản lý danh mục.</p>
      </div>
    </div>
  );
}
