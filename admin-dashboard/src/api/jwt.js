// Decodes a JWT payload without verifying the signature. Verification always
// happens server-side (SecurityConfig + @PreAuthorize) — this is purely so the
// UI knows which role/username to show and route around.
export function decodeJwt(token) {
  try {
    const payload = token.split(".")[1];
    const json = atob(payload.replace(/-/g, "+").replace(/_/g, "/"));
    return JSON.parse(json);
  } catch {
    return null;
  }
}

export function isTokenExpired(token) {
  const claims = decodeJwt(token);
  if (!claims?.exp) return true;
  return Date.now() >= claims.exp * 1000;
}
