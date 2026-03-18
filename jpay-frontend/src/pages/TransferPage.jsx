import { useState } from 'react';
import { walletService } from '../api/wallet';
import { Send } from 'lucide-react';

export default function TransferPage() {
  const [receivedUsername, setReceivedUsername] = useState('');
  const [amount, setAmount] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [feedback, setFeedback] = useState({ text: '', isError: false });

  const handleTransfer = async (e) => {
    e.preventDefault();
    const numericAmount = Number(amount);
    if (!receivedUsername || isNaN(numericAmount) || numericAmount < 1000) {
      setFeedback({ text: 'Số tiền tối thiểu là 1,000 VND', isError: true });
      return;
    }
    try {
      setLoading(true);
      setFeedback({ text: '', isError: false });
      await walletService.transfer({
        receivedUsername,
        amount: numericAmount,
        categoryId: categoryId || undefined,
        message: message || undefined
      });
      setFeedback({ text: 'Chuyển tiền thành công!', isError: false });
      setAmount('');
      setReceivedUsername('');
      setCategoryId('');
      setMessage('');
    } catch (err) {
      setFeedback({ text: err?.message || 'Chuyển tiền thất bại', isError: true });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header"><h1>Chuyển tiền</h1></div>

      <div className="card" style={{ maxWidth: '600px' }}>
        <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '1.5rem' }}>
          <Send size={20} color="var(--primary)" /> Gửi tiền
        </h3>
        <form onSubmit={handleTransfer}>
          <div className="form-group">
            <label className="form-label">Username người nhận</label>
            <input
              className="form-input"
              type="text"
              placeholder="Nhập username người nhận"
              value={receivedUsername}
              onChange={e => setReceivedUsername(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label className="form-label">Số tiền (VND)</label>
            <input
              className="form-input"
              type="number"
              min="1000"
              placeholder="Tối thiểu 1,000 VND"
              value={amount}
              onChange={e => setAmount(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label className="form-label">Danh mục (tuỳ chọn)</label>
            <input
              className="form-input"
              type="text"
              placeholder="Category ID"
              value={categoryId}
              onChange={e => setCategoryId(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label className="form-label">Lời nhắn (tuỳ chọn)</label>
            <input
              className="form-input"
              type="text"
              placeholder="Nội dung chuyển khoản"
              value={message}
              onChange={e => setMessage(e.target.value)}
            />
          </div>

          {feedback.text && (
            <div style={{ padding: '0.75rem 1rem', borderRadius: 'var(--radius-sm)', marginBottom: '1rem', background: feedback.isError ? 'rgba(239,68,68,0.1)' : 'rgba(16,185,129,0.1)', color: feedback.isError ? 'var(--danger)' : 'var(--success)', border: `1px solid ${feedback.isError ? 'var(--danger)' : 'var(--success)'}` }}>
              {feedback.text}
            </div>
          )}

          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? 'Đang xử lý...' : 'Chuyển tiền'}
          </button>
        </form>
      </div>
    </div>
  );
}
