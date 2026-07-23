# SL FinePay — Customer Web Portal

React (Vite) SPA for drivers to look up and pay a traffic fine online, matching
the SL-FinePay backend at `com.trafficfine.backend`.

## Setup

```bash
npm install
npm run dev
```

Runs on http://localhost:5173. The backend is expected at
`http://localhost:8080` (change `VITE_API_BASE_URL` in `.env` if different).
Make sure the backend's CORS/`@CrossOrigin` allows this origin (it currently
allows `*`, which is fine for dev but should be locked down for production).

## Pages / routes

- `/` — fine lookup (reference number only, see note below)
- `/pay/:refNumber` — fine details + demo card payment form
- `/success` — confirmation after payment

## Notes on matching the backend as-built

- The lookup page only asks for the **reference number**. The task brief
  mentioned a second "category ID" box, but `GET /api/fines/{referenceNumber}`
  doesn't take or need one — the category comes back as part of the fine
  itself (`categoryName`, `amount`).
- `FineResponse` doesn't include driver name/phone, so the payment page shows
  reference number, category, district, amount, status and issue date — not
  "driver details", since that isn't returned by the API.
- The payment form is a **demo card form** (name, number, expiry, CVV) that,
  on submit, calls `POST /api/fines/{refNumber}/pay` directly. Swap in a real
  gateway (e.g. PayHere) before going live — that's a bigger integration
  (merchant account, hash generation, redirect/callback handling) that's worth
  doing as its own step.

## Deploy

Push this folder to its own GitHub repo (or a subfolder) and connect it to
Netlify or Vercel. Set `VITE_API_BASE_URL` as an environment variable in the
hosting dashboard to point at your deployed backend.
