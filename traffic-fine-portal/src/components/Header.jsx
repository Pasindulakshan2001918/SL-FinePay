import React from "react";
import { Link } from "react-router-dom";

export default function Header() {
  return (
    <header className="app-header">
      <Link to="/" className="brand">
        <span className="brand-mark">SL</span>
        <span className="brand-text">
          FinePay
          <small>Traffic Fine Payments · Sri Lanka</small>
        </span>
      </Link>
    </header>
  );
}
