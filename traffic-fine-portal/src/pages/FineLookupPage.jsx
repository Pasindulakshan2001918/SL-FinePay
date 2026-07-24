import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { lookupFine } from "../api/fineService";

// NOTE: The backend only exposes GET /api/fines/{referenceNumber} — it looks the
// fine up purely by reference number, the category is stored on the fine itself
// and comes back in the response. So we only collect the reference number here
// rather than asking for a separate category ID.
export default function FineLookupPage() {
  const [refNumber, setRefNumber] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!refNumber.trim()) {
      setError("Please enter your fine reference number.");
      return;
    }

    setLoading(true);
    try {
      const fine = await lookupFine(refNumber.trim());
      if (fine.status === "PAID") {
        setError("This fine has already been paid. No further action needed.");
        return;
      }
      navigate(`/pay/${encodeURIComponent(fine.referenceNumber)}`, { state: { fine } });
    } catch (err) {
      if (err.response && err.response.status === 404) {
        setError("No fine found with that reference number. Please check and try again.");
      } else {
        setError("Something went wrong while looking up your fine. Please try again shortly.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page lookup-page">
      <div className="lookup-card card">
        <h1>Check &amp; pay your traffic fine</h1>
        <p className="subtitle">
          Enter the reference number printed on your fine notice to view its
          details and pay online.
        </p>

        <form onSubmit={handleSubmit} className="lookup-form">
          <label htmlFor="refNumber">Fine reference number</label>
          <input
            id="refNumber"
            type="text"
            placeholder="e.g. TF-2024-00123"
            value={refNumber}
            onChange={(e) => setRefNumber(e.target.value)}
            autoFocus
          />

          {error && <div className="alert alert-error">{error}</div>}

          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? "Searching…" : "Find my fine"}
          </button>
        </form>
      </div>
    </div>
  );
}
