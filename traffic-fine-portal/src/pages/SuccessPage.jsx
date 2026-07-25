import React from "react";
import { useLocation, useNavigate, Navigate } from "react-router-dom";

export default function SuccessPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const fine = location.state?.fine;

  if (!fine) {
    // Someone navigated here directly without paying — send them home.
    return <Navigate to="/" replace />;
  }

  return (
    <div className="page">
      <div className="card success-card">
        <div className="success-icon">✓</div>
        <h1>Payment successful</h1>
        <p>Your fine has been marked as paid. The officer who issued it has been notified.</p>
        <dl>
          <div className="row"><dt>Reference number</dt><dd>{fine.referenceNumber}</dd></div>
          <div className="row"><dt>Category</dt><dd>{fine.categoryName}</dd></div>
          <div className="row"><dt>Amount paid</dt><dd>LKR {fine.amount?.toFixed(2)}</dd></div>
        </dl>
        <button className="btn btn-primary" onClick={() => navigate("/")}>Look up another fine</button>
      </div>
    </div>
  );
}
