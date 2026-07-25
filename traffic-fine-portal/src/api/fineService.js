import api from "./axios";

// GET /api/fines/{referenceNumber}
export const lookupFine = (referenceNumber) =>
  api.get(`/fines/${encodeURIComponent(referenceNumber)}`).then((res) => res.data);

// POST /api/fines/{referenceNumber}/pay
export const payFine = (referenceNumber) =>
  api.post(`/fines/${encodeURIComponent(referenceNumber)}/pay`).then((res) => res.data);
