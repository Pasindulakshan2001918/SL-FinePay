import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

export default function Sidebar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <aside className="sidebar">
      <div className="brand">
        <span className="brand-mark">SL</span>
        <span className="brand-text">FinePay<small>Admin</small></span>
      </div>

      <nav className="nav-links">
        <NavLink to="/" end className={({ isActive }) => `nav-link${isActive ? " active" : ""}`}>
          Dashboard
        </NavLink>
        <NavLink to="/fines" className={({ isActive }) => `nav-link${isActive ? " active" : ""}`}>
          Fines
        </NavLink>
      </nav>

      <div className="sidebar-footer">
        <div className="user-chip">
          <div className="avatar">{user?.username?.[0]?.toUpperCase()}</div>
          <div>
            <div className="user-name">{user?.username}</div>
            <div className="user-role">{user?.role}</div>
          </div>
        </div>
        <button className="btn btn-ghost" onClick={handleLogout}>Log out</button>
      </div>
    </aside>
  );
}
