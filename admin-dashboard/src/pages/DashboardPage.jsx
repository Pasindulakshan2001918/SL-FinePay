import React, { useEffect, useMemo, useState } from "react";
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  PieChart, Pie, Cell, Legend,
} from "recharts";
import { getStats } from "../api/adminService";

const COLORS = ["#0b3d62", "#f2a93b", "#1c8a53", "#7a5cd1", "#c23b3b", "#2f8fb0"];

function formatLKR(amount) {
  return new Intl.NumberFormat("en-LK", { style: "currency", currency: "LKR", maximumFractionDigits: 0 }).format(amount || 0);
}

export default function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    getStats()
      .then(setStats)
      .catch(() => setError("Could not load dashboard stats."))
      .finally(() => setLoading(false));
  }, []);

  // Backend returns raw Object[] pairs, e.g. [["Colombo", 12500.0], ...]
  const districtData = useMemo(
    () => (stats?.districtStats || []).map(([district, total]) => ({ district, total: Number(total) })),
    [stats]
  );

  const categoryData = useMemo(
    () => (stats?.categoryStats || []).map(([name, count]) => ({ name, count: Number(count) })),
    [stats]
  );

  const totalIssued = useMemo(() => categoryData.reduce((sum, c) => sum + c.count, 0), [categoryData]);
  const totalCollected = useMemo(() => districtData.reduce((sum, d) => sum + d.total, 0), [districtData]);

  if (loading) return <div className="page-inner"><p>Loading dashboard…</p></div>;
  if (error) return <div className="page-inner"><div className="alert alert-error">{error}</div></div>;

  return (
    <div className="page-inner">
      <h1>Dashboard</h1>
      <p className="subtitle">Overview of fines issued and collected across all districts.</p>

      <div className="summary-cards">
        <div className="stat-card">
          <span className="stat-label">Total fines issued</span>
          <span className="stat-value">{totalIssued}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">Total collected (paid fines)</span>
          <span className="stat-value">{formatLKR(totalCollected)}</span>
        </div>
        <div className="stat-card">
          <span className="stat-label">Districts reporting</span>
          <span className="stat-value">{districtData.length}</span>
        </div>
      </div>

      <div className="chart-grid">
        <div className="card chart-card">
          <h2>Collections by district</h2>
          {districtData.length === 0 ? (
            <p className="empty-note">No paid fines yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={districtData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#e1e7ee" />
                <XAxis dataKey="district" tick={{ fontSize: 12 }} />
                <YAxis tick={{ fontSize: 12 }} />
                <Tooltip formatter={(v) => formatLKR(v)} />
                <Bar dataKey="total" fill="#0b3d62" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          )}
        </div>

        <div className="card chart-card">
          <h2>Fines by category</h2>
          {categoryData.length === 0 ? (
            <p className="empty-note">No fines recorded yet.</p>
          ) : (
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie data={categoryData} dataKey="count" nameKey="name" cx="50%" cy="50%" outerRadius={100} label>
                  {categoryData.map((_, i) => (
                    <Cell key={i} fill={COLORS[i % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          )}
        </div>
      </div>

      <p className="hint">
        Note: the "paid vs unpaid" breakdown per category needs one extra field
        from the backend — see the project README for the small stats endpoint tweak.
      </p>
    </div>
  );
}
