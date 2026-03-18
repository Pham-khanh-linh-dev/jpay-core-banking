import { useState, useEffect } from 'react';
import { categoryService } from '../api/category';
import { PlusCircle, Tag, TrendingUp } from 'lucide-react';

export default function CategoriesPage() {
  const now = new Date();

  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);

  // Create Category state
  const [catName, setCatName] = useState('');
  const [catAmount, setCatAmount] = useState('');
  const [catMonth, setCatMonth] = useState(now.getMonth() + 1);
  const [catYear, setCatYear] = useState(now.getFullYear());

  const [msg, setMsg] = useState({ text: '', isError: false });

  const fetchCategories = async () => {
    try {
      setLoading(true);
      const data = await categoryService.getMyCategories();
      setCategories(data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const formatVND = (value) => {
    return new Intl.NumberFormat('vi-VN').format(value || 0) + ' ₫';
  };

  const handleCreateCategory = async (e) => {
    e.preventDefault();
    try {
      setMsg({ text: '', isError: false });

      const payload = { categoryName: catName };
      if (catAmount) {
        payload.amount = Number(catAmount);
        payload.month = Number(catMonth);
        payload.year = Number(catYear);
      }

      await categoryService.create(payload);
      setMsg({ text: 'Tạo danh mục thành công!', isError: false });
      setCatName('');
      setCatAmount('');
      fetchCategories(); // Refresh list
    } catch (err) {
      setMsg({ text: err?.message || 'Tạo danh mục thất bại', isError: true });
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Quản lý Danh mục & Ngân sách</h1>
      </div>

      {msg.text && (
        <div style={{ padding: '1rem', background: msg.isError ? 'rgba(239,68,68,0.1)' : 'rgba(16, 185, 129, 0.1)', color: msg.isError ? 'var(--danger)' : 'var(--success)', borderRadius: 'var(--radius)', marginBottom: '1.5rem', border: `1px solid ${msg.isError ? 'var(--danger)' : 'var(--success)'}` }}>
          {msg.text}
        </div>
      )}

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '1.5rem' }}>
        
        {/* Cột Danh sách danh mục */}
        <div className="card" style={{ order: 2 }}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
            <Tag size={20} color="var(--primary)" /> Danh sách của bạn
          </h3>
          
          {loading ? (
            <p className="text-muted">Đang tải danh sách...</p>
          ) : categories.length === 0 ? (
            <div style={{ textAlign: 'center', padding: '2rem 0', color: 'var(--text-muted)' }}>
              Chưa có danh mục nào. Hãy tạo mới bên cạnh!
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {categories.map((cat, idx) => (
                <div key={idx} style={{ padding: '1rem', border: '1px solid var(--border)', borderRadius: 'var(--radius)' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                    <div style={{ fontWeight: 'bold', fontSize: '1.1rem' }}>{cat.categoryName}</div>
                  </div>
                  
                  {/* Hiển thị ngân sách */}
                  {cat.budgets && cat.budgets.length > 0 ? (
                    cat.budgets.map((b, bIdx) => (
                      <div key={bIdx} style={{ fontSize: '0.9rem', marginTop: '0.5rem', background: 'var(--bg-color)', padding: '0.75rem', borderRadius: 'var(--radius-sm)' }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                          <span style={{color: 'var(--text-muted)'}}>Ngân sách Tháng {b.month}/{b.year}</span>
                          <span style={{ fontWeight: 'bold' }}>{formatVND(b.spentAmount)} / {formatVND(b.amount)}</span>
                        </div>
                        <div style={{ width: '100%', height: '8px', background: 'var(--border)', borderRadius: '4px', overflow: 'hidden' }}>
                          <div style={{ 
                            height: '100%', 
                            background: (b.spentAmount || 0) > (b.amount || 1) ? 'var(--danger)' : 'var(--primary)', 
                            width: `${Math.min(((b.spentAmount || 0) / (b.amount || 1)) * 100, 100)}%` 
                          }}></div>
                        </div>
                        {(b.spentAmount || 0) > (b.amount || 0) && b.amount > 0 && (
                          <div style={{ color: 'var(--danger)', fontSize: '0.8rem', marginTop: '0.25rem' }}>Đã vượt quá hạn mức ngân sách!</div>
                        )}
                      </div>
                    ))
                  ) : (
                    <div style={{ fontSize: '0.9rem', color: 'var(--text-muted)' }}>Chưa có ngân sách</div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Cột Form tạo danh mục */}
        <div className="card" style={{ height: 'fit-content', order: 1 }}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <PlusCircle size={20} color="var(--primary)" /> Thêm danh mục mới
          </h3>
          <p style={{fontSize: '0.85rem', color: 'var(--text-muted)', marginTop: '0.5rem', marginBottom: '1.5rem'}}>
            Tạo danh mục mới và thiết lập hạn mức để tự động khởi tạo ngân sách.
          </p>
          <form onSubmit={handleCreateCategory}>
            <div className="form-group">
              <label className="form-label">Tên danh mục</label>
              <input className="form-input" placeholder="VD: Ăn uống, Giải trí..." value={catName} onChange={e => setCatName(e.target.value)} required />
            </div>
            <div className="form-group">
              <label className="form-label">Hạn mức ngân sách (tuỳ chọn)</label>
              <input className="form-input" type="number" placeholder="Số tiền ngân sách" value={catAmount} onChange={e => setCatAmount(e.target.value)} />
            </div>
            <div style={{ display: 'flex', gap: '1rem' }}>
              <div className="form-group" style={{ flex: 1 }}>
                <label className="form-label">Tháng áp dụng</label>
                <input className="form-input" type="number" min="1" max="12" value={catMonth} onChange={e => setCatMonth(e.target.value)} required={!!catAmount} disabled={!catAmount} />
              </div>
              <div className="form-group" style={{ flex: 1 }}>
                <label className="form-label">Năm</label>
                <input className="form-input" type="number" value={catYear} onChange={e => setCatYear(e.target.value)} required={!!catAmount} disabled={!catAmount} />
              </div>
            </div>
            <button className="btn btn-primary" style={{width: '100%', marginTop: '0.5rem'}} type="submit">Lưu danh mục</button>
          </form>
        </div>

      </div>
    </div>
  );
}
