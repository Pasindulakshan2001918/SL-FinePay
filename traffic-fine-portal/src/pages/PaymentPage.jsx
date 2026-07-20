import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { lookupFine, payFine } from "../api/fineService";

function formatDate(iso) {
  if (!iso) return "—";
  const d = new Date(iso);
  return d.toLocaleString("en-GB", { dateStyle: "medium", timeStyle: "short" });
}

function formatLKR(amount) {
  if (amount == null) return "—";
  return new Intl.NumberFormat("en-LK", {
    style: "currency",
    currency: "LKR",
    maximumFractionDigits: 2,
  }).format(amount);
}

export default function PaymentPage() {
  const { refNumber } = useParams();
  const location = useLocation();
  const navigate = useNavigate();

  const [fine, setFine] = useState(location.state?.fine || null);
  const [loadingFine, setLoadingFine] = useState(!location.state?.fine);
  const [loadError, setLoadError] = useState("");

  const [cardName, setCardName] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [expiry, setExpiry] = useState("");
  const [cvv, setCvv] = useState("");

  const [paying, setPaying] = useState(false);
  const [payError, setPayError] = useState("");

  // If the page was opened directly (refresh / shared link), re-fetch the fine.
  useEffect(() => {
    if (fine) return;
    setLoadingFine(true);
    lookupFine(refNumber)
      .then(setFine)
      .catch(() => setLoadError("We couldn't find that fine. Please search again from the home page."))
      .finally(() => setLoadingFine(false));
  }, [refNumber, fine]);

  const handlePay = async (e) => {
    e.preventDefault();
    setPayError("");

    if (!cardName.trim() || cardNumber.replace(/\s/g, "").length < 12 || !expiry.trim() || cvv.trim().length < 3) {
      setPayError("Please fill in all card details correctly.");
      return;
    }

    setPaying(true);
    try {
      // In production this is where you'd integrate a real gateway (e.g. PayHere)
      // before confirming payment. Here we call the backend directly once the
      // (simulated) card charge succeeds.
      const paidFine = await payFine(refNumber);
      navigate("/success", { state: { fine: paidFine } });
    } catch (err) {
      if (err.response?.status === 400) {
        setPayError("This fine has already been paid.");
      } else {
        setPayError("Payment failed. Please try again.");
      }
    } finally {
      setPaying(false);
    }
  };

  if (loadingFine) {
    return (
      <div className="page">
        <div className="card"><p>Loading fine details…</p></div>
      </div>
    );
  }

  if (loadError || !fine) {
    return (
      <div className="page">
        <div className="card">
          <div className="alert alert-error">{loadError || "Fine not found."}</div>
          <button className="btn btn-secondary" onClick={() => navigate("/")}>Back to search</button>
        </div>
      </div>
    );
  }

  return (
    <div className="page payment-page">
      <div className="card fine-summary">
        <h2>Fine details</h2>
        <dl>
          <div className="row"><dt>Reference number</dt><dd>{fine.referenceNumber}</dd></div>
          <div className="row"><dt>Category</dt><dd>{fine.categoryName}</dd></div>
          <div className="row"><dt>District</dt><dd>{fine.district}</dd></div>
          <div className="row"><dt>Issued on</dt><dd>{formatDate(fine.issuedAt)}</dd></div>
          <div className="row"><dt>Status</dt><dd><span className={`badge badge-${fine.status?.toLowerCase()}`}>{fine.status}</span></dd></div>
          <div className="row total"><dt>Amount due</dt><dd>{formatLKR(fine.amount)}</dd></div>
        </dl>
      </div>

      <div className="card payment-form-card">
        <h2>Pay now</h2>
        <form onSubmit={handlePay} className="payment-form">
          <label htmlFor="cardName">Name on card</label>
          <input id="cardName" type="text" value={cardName} onChange={(e) => setCardName(e.target.value)} placeholder="e.g. Nimal Perera" />

          <label htmlFor="cardNumber">Card number</label>
          <input id="cardNumber" type="text" inputMode="numeric" value={cardNumber} onChange={(e) => setCardNumber(e.target.value)} placeholder="1234 5678 9012 3456" maxLength={19} />

          <div className="form-row">
            <div>
              <label htmlFor="expiry">Expiry</label>
              <input id="expiry" type="text" value={expiry} onChange={(e) => setExpiry(e.target.value)} placeholder="MM/YY" maxLength={5} />
            </div>
            <div>
              <label htmlFor="cvv">CVV</label>
              <input id="cvv" type="password" value={cvv} onChange={(e) => setCvv(e.target.value)} placeholder="123" maxLength={4} />
            </div>
          </div>

          {payError && <div className="alert alert-error">{payError}</div>}

          <button type="submit" className="btn btn-primary" disabled={paying}>
            {paying ? "Processing payment…" : `Pay ${formatLKR(fine.amount)}`}
          </button>
          <p className="hint">This is a demo card form. Swap it for a real gateway (e.g. PayHere) before going live.</p>
        </form>
      </div>
    </div>
  );
}
