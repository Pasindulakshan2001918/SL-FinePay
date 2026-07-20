import React from "react";
import { Routes, Route } from "react-router-dom";
import FineLookupPage from "./pages/FineLookupPage.jsx";
import PaymentPage from "./pages/PaymentPage.jsx";
import SuccessPage from "./pages/SuccessPage.jsx";
import Header from "./components/Header.jsx";

export default function App() {
  return (
    <div className="app-shell">
      <Header />
      <main className="app-main">
        <Routes>
          <Route path="/" element={<FineLookupPage />} />
          <Route path="/pay/:refNumber" element={<PaymentPage />} />
          <Route path="/success" element={<SuccessPage />} />
          <Route path="*" element={<FineLookupPage />} />
        </Routes>
      </main>
      <footer className="app-footer">
        <span>SL FinePay · Sri Lanka Traffic Fine Payment Portal</span>
      </footer>
    </div>
  );
}
