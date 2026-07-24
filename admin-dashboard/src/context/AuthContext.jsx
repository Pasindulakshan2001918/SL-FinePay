import React, { createContext, useContext, useEffect, useState } from "react";
import { decodeJwt, isTokenExpired } from "../api/jwt";
import { login as loginRequest } from "../api/adminService";

const AuthContext = createContext(null);
const TOKEN_KEY = "slfinepay_token";

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem(TOKEN_KEY));
  const [user, setUser] = useState(null);
  const [ready, setReady] = useState(false);

  useEffect(() => {
    if (token && !isTokenExpired(token)) {
      const claims = decodeJwt(token);
      setUser({ username: claims?.sub, role: claims?.role });
    } else {
      setToken(null);
      setUser(null);
      localStorage.removeItem(TOKEN_KEY);
    }
    setReady(true);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const login = async (username, password) => {
    const { token: newToken } = await loginRequest(username, password);
    const claims = decodeJwt(newToken);
    localStorage.setItem(TOKEN_KEY, newToken);
    setToken(newToken);
    setUser({ username: claims?.sub, role: claims?.role });
    return claims?.role;
  };

  const logout = () => {
    localStorage.removeItem(TOKEN_KEY);
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ token, user, ready, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
