import React from "react";
import { Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import DashboardPage from "./pages/DashboardPage.jsx";
import FinesListPage from "./pages/FinesListPage.jsx";
import ProtectedRoute from "./components/ProtectedRoute.jsx";
import Sidebar from "./components/Sidebar.jsx";

function AdminLayout({ children }) {
  return (
    <div className="admin-shell">
      <Sidebar />
      <main className="admin-main">{children}</main>
    </div>
  );
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <AdminLayout>
              <DashboardPage />
            </AdminLayout>
          </ProtectedRoute>
        }
      />
      <Route
        path="/fines"
        element={
          <ProtectedRoute>
            <AdminLayout>
              <FinesListPage />
            </AdminLayout>
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}
