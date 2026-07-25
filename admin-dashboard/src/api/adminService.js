import api from "./axios";

// POST /api/auth/login  { username, password } -> { token }
export const login = (username, password) =>
  api.post("/auth/login", { username, password }).then((res) => res.data);

// GET /api/admin/stats  (ADMIN only)
export const getStats = () => api.get("/admin/stats").then((res) => res.data);

// GET /api/admin/fines?page=&size=&search=  (ADMIN only)
// NOTE: this endpoint does not exist in the backend yet — see the README in
// this project for the Spring Boot code to add it. Until then this call will
// 404 and the Fines list page will show a friendly empty state.
export const getFines = (params) => api.get("/admin/fines", { params }).then((res) => res.data);
