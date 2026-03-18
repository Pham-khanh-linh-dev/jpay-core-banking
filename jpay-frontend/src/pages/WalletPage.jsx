import { useState, useEffect } from 'react';
import { walletService } from '../api/wallet';
import { Wallet as WalletIcon, ArrowDownCircle, ArrowUpCircle } from 'lucide-react';

export default function WalletPage() {
  const [wallet, setWallet] = useState(null);
  const [depositAmount, setDepositAmount] = useState('');
  const [withdrawAmount, setWithdrawAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchWallet = async () => {
    try {
      const w = await walletService.getMyWallet();
      setWallet(w);
    } catch (e) {
      console.error(e);
    }
  };

  useEffect(() => {
    fetchWallet();
  }, []);

  const formatVND = (value) => {
    return new Intl.NumberFormat('vi-VN').format(value || 0) + ' ₫';
  };

  const handleAction = async (actionStr) => {
    const value = actionStr === 'deposit' ? depositAmount : withdrawAmount;

    if (!value || isNaN(value) || value < 500) {
      setError('Số tiền tối thiểu là 500 VND');
      return;
    }
    try {
      setLoading(true);
      setError(''); setSuccess('');
      if (actionStr === 'deposit') {
        const res = await walletService.deposit(Number(value));
        setWallet(res);
        setDepositAmount('');
      } else {
        const res = await walletService.withdraw(Number(value));
        setWallet(res);
        setWithdrawAmount('');
      }
      setSuccess(`${actionStr === 'deposit' ? 'Nạp' : 'Rút'} ${formatVND(value)} thành công!`);
    } catch (err) {
      setError(err?.message || `Thao tác thất bại`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-header"><h1>Ví của tôi</h1></div>

      <div className="card" style={{ background: 'linear-gradient(135deg, var(--primary) 0%, var(--primary-hover) 100%)', color: 'white' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <WalletIcon size={40} opacity={0.8} />
          <div>
            <div style={{ opacity: 0.8, fontSize: '0.875rem' }}>Số dư hiện tại</div>
            <div style={{ fontSize: '2.5rem', fontWeight: 'bold' }}>{formatVND(wallet?.balance)}</div>
            <div style={{ opacity: 0.7, fontSize: '0.8rem', marginTop: '0.25rem' }}>Đơn vị: {wallet?.currency || 'VND'}</div>
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '1.5rem' }}>
        <div className="card" style={{ borderTop: '4px solid var(--success)' }}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}><ArrowDownCircle size={20} color="var(--success)" /> Nạp tiền</h3>
          <div className="form-group mt-4">
            <label className="form-label">Số tiền (VND)</label>
            <input className="form-input" type="number" min="500" placeholder="Tối thiểu 500 VND" value={depositAmount} onChange={e => setDepositAmount(e.target.value)} />
          </div>
          <button className="btn btn-primary" style={{ background: 'var(--success)' }} disabled={loading} onClick={() => handleAction('deposit')}>
            Nạp tiền
          </button>
        </div>

        <div className="card" style={{ borderTop: '4px solid var(--danger)' }}>
          <h3 style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}><ArrowUpCircle size={20} color="var(--danger)" /> Rút tiền</h3>
          <div className="form-group mt-4">
            <label className="form-label">Số tiền (VND)</label>
            <input className="form-input" type="number" min="500" placeholder="Tối thiểu 500 VND" value={withdrawAmount} onChange={e => setWithdrawAmount(e.target.value)} />
          </div>
          <button className="btn btn-primary" style={{ background: 'var(--danger)' }} disabled={loading} onClick={() => handleAction('withdraw')}>
            Rút tiền
          </button>
        </div>
      </div>

      {(error || success) && (
        <div style={{ padding: '1rem', background: error ? 'rgba(239,68,68,0.1)' : 'rgba(16, 185, 129, 0.1)', color: error ? 'var(--danger)' : 'var(--success)', borderRadius: 'var(--radius)', marginTop: '1rem', border: `1px solid ${error ? 'var(--danger)' : 'var(--success)'}` }}>
          {error || success}
        </div>
      )}
    </div>
  );
}
