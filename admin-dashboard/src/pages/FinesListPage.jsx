import React, { useEffect, useState } from "react";
import { getFines } from "../api/adminService";

const PAGE_SIZE = 10;

export default function FinesListPage() {
  const [fines, setFines] = useState([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [notImplemented, setNotImplemented] = useState(false);

  useEffect(() => {
    setLoading(true);
    getFines({ page, size: PAGE_SIZE, search })
      .then((data) => {
        // Works whether the backend returns a Spring Page<> object or a plain array.
        const content = Array.isArray(data) ? data : data.content || [];
        setFines(content);
        setTotalPages(Array.isArray(data) ? 1 : data.totalPages || 1);
        setNotImplemented(false);
      })
      .catch((err) => {
        if (err.response?.status === 404) {
          setNotImplemented(true);
        }
        setFines([]);
      })
      .finally(() => setLoading(false));
  }, [page, search]);

  return (
    <div className="page-inner">
      <h1>All fines</h1>
      <p className="subtitle">Search and browse every fine issued in the system.</p>

      <div className="table-toolbar">
        <input
          type="text"
          placeholder="Search by reference number, district or category…"
          value={search}
          onChange={(e) => { setSearch(e.target.value); setPage(0); }}
        />
      </div>

      {notImplemented && (
        <div className="alert alert-error">
          The backend doesn't have a <code>GET /api/admin/fines</code> endpoint yet, so this
          list can't load. See the README for the Spring Boot code to add it.
        </div>
      )}

      <div className="card table-card">
        <table>
          <thead>
            <tr>
              <th>Reference</th>
              <th>Category</th>
              <th>District</th>
              <th>Amount</th>
              <th>Status</th>
              <th>Issued</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr><td colSpan={6} className="empty-note">Loading…</td></tr>
            ) : fines.length === 0 ? (
              <tr><td colSpan={6} className="empty-note">No fines found.</td></tr>
            ) : (
              fines.map((f) => (
                <tr key={f.id || f.referenceNumber}>
                  <td className="mono">{f.referenceNumber}</td>
                  <td>{f.categoryName}</td>
                  <td>{f.district}</td>
                  <td>{f.amount?.toFixed(2)}</td>
                  <td><span className={`badge badge-${f.status?.toLowerCase()}`}>{f.status}</span></td>
                  <td>{f.issuedAt ? new Date(f.issuedAt).toLocaleDateString("en-GB") : "—"}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      <div className="pagination">
        <button className="btn btn-ghost" disabled={page === 0} onClick={() => setPage((p) => p - 1)}>Previous</button>
        <span>Page {page + 1} of {totalPages}</span>
        <button className="btn btn-ghost" disabled={page + 1 >= totalPages} onClick={() => setPage((p) => p + 1)}>Next</button>
      </div>
    </div>
  );
}
