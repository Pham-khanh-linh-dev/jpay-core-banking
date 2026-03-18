import { useState, useEffect } from 'react';
import { walletService } from '../api/wallet';
import { Clock } from 'lucide-react';

export default function HistoryPage() {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);

  const formatVND = (value) => {
    return new Intl.NumberFormat('vi-VN').format(value || 0) + ' ₫';
  };

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        const data = await walletService.getHistory();
        setHistory(data || []);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchHistory();
  }, []);

  const getTypeColor = (type) => {
    if (type === 'DEPOSIT' || type === 'TRANSFER_IN') return 'var(--success)';
    return 'var(--danger)';
  };

  const getTypePrefix = (type) => {
    if (type === 'DEPOSIT' || type === 'TRANSFER_IN') return '+';
    return '-';
  };

  const formatDate = (dateInput) => {
    if (!dateInput) return '—';
    if (Array.isArray(dateInput)) {
      return `${dateInput[2].toString().padStart(2, '0')}/${dateInput[1].toString().padStart(2, '0')}/${dateInput[0]}`;
    }
    return new Date(dateInput).toLocaleDateString('vi-VN');
  };

  return (
    <div>
      <div className="page-header">
        <h1>Lịch sử giao dịch</h1>
      </div>

      <div className="card">
        {loading ? (
          <p className="text-muted">Đang tải...</p>
        ) : history.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '3rem 0', color: 'var(--text-muted)' }}>
            <Clock size={48} opacity={0.5} style={{ marginBottom: '1rem' }} />
            <p>Chưa có giao dịch nào.</p>
          </div>
        ) : (
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
              <thead>
                <tr style={{ borderBottom: '2px solid var(--border)', color: 'var(--text-muted)' }}>
                  <th style={{ padding: '1rem' }}>Loại</th>
                  <th style={{ padding: '1rem' }}>Số tiền</th>
                  <th style={{ padding: '1rem' }}>Số dư sau</th>
                  <th style={{ padding: '1rem' }}>Lời nhắn</th>
                  <th style={{ padding: '1rem' }}>Ngày</th>
                </tr>
              </thead>
              <tbody>
                {history.map((tx, index) => (
                  <tr key={index} style={{ borderBottom: '1px solid var(--border)' }}>
                    <td style={{ padding: '1rem' }}>
                      <span style={{ padding: '0.25rem 0.75rem', borderRadius: '1rem', fontSize: '0.75rem', background: getTypeColor(tx.type) === 'var(--success)' ? 'rgba(16,185,129,0.1)' : 'rgba(239,68,68,0.1)', color: getTypeColor(tx.type) }}>
                        {tx.type}
                      </span>
                    </td>
                    <td style={{ padding: '1rem', fontWeight: 'bold', color: getTypeColor(tx.type) }}>
                      {getTypePrefix(tx.type)}{formatVND(tx.amount)}
                    </td>
                    <td style={{ padding: '1rem' }}>{formatVND(tx.balanceAfter)}</td>
                    <td style={{ padding: '1rem', color: 'var(--text-muted)' }}>{tx.message || '—'}</td>
                    <td style={{ padding: '1rem', color: 'var(--text-muted)' }}>{formatDate(tx.createdAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
