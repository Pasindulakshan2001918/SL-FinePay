# SL FinePay — Admin Dashboard

React (Vite) + Recharts SPA for admins to monitor fines, matching the
SL-FinePay backend at `com.trafficfine.backend`.

## Setup

```bash
npm install
npm run dev
```

Runs on http://localhost:5174. Backend expected at `http://localhost:8080`
(change `VITE_API_BASE_URL` in `.env`). Log in with the seeded admin account:
**username `admin` / password `admin123`**.

## Pages / routes

- `/login` — username + password, calls `POST /api/auth/login`, stores JWT
- `/` — dashboard: bar chart (collections per district), pie chart (fines per
  category), summary cards, from `GET /api/admin/stats`
- `/fines` — searchable, paginated table of all fines

Every request goes through an Axios interceptor that attaches
`Authorization: Bearer <token>`, and a response interceptor that logs the user
out on 401/403.

## ⚠️ Two backend gaps you'll need to close

### 1. No endpoint to list all fines

The brief asks for a searchable, paginated fines table, but the backend only
has fine-by-reference lookup — no "get all fines" endpoint. The frontend
(`FinesListPage.jsx`) is already wired to call `GET /api/admin/fines` and will
show a clear "not implemented" message until it exists. Add this to the
backend:

**`FineRepository.java`** — add a search query:
```java
@Query("SELECT f FROM Fine f WHERE " +
       "(:search IS NULL OR LOWER(f.referenceNumber) LIKE LOWER(CONCAT('%', :search, '%')) " +
       "OR LOWER(f.district) LIKE LOWER(CONCAT('%', :search, '%')) " +
       "OR LOWER(f.category.name) LIKE LOWER(CONCAT('%', :search, '%')))")
Page<Fine> search(@Param("search") String search, Pageable pageable);
```
(needs `import org.springframework.data.domain.Page;`, `Pageable;` and
`org.springframework.data.repository.query.Param;`)

**`FineService.java`** — add:
```java
public Page<FineResponse> getAllFines(String search, Pageable pageable) {
    return fineRepository.search(search, pageable).map(FineResponse::from);
}
```

**`AdminController.java`** — add:
```java
@GetMapping("/fines")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Page<FineResponse>> getFines(
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(
        fineService.getAllFines(search, PageRequest.of(page, size, Sort.by("issuedAt").descending()))
    );
}
```

Once this exists, `getFines()` in `src/api/adminService.js` will just work —
it already handles both a plain array and a Spring `Page<>` response shape.

### 2. Paid vs. unpaid breakdown isn't in `/api/admin/stats`

`getCountByCategory()` counts *all* fines per category (paid + unpaid) and
`getTotalCollectionByDistrict()` only sums *paid* amounts — there's no paid
vs. unpaid count anywhere. The dashboard currently shows total issued and
total collected, with a note about this gap. If you want the split, add a
second grouped query, e.g.:

```java
@Query("SELECT f.category.name, f.status, COUNT(f) FROM Fine f GROUP BY f.category.name, f.status")
List<Object[]> getCountByCategoryAndStatus();
```

and merge it into the `/api/admin/stats` response — then update
`DashboardPage.jsx` to plot it.

## A quick security note

`src/main/resources/application.properties` in the backend repo has the real
MySQL password and JWT secret committed in plain text. Worth moving those to
environment variables / a `.env` that's gitignored before this goes any
further, especially if the repo is public.

## Deploy

Same as the customer portal — push to GitHub, connect to Netlify/Vercel, set
`VITE_API_BASE_URL` in the hosting dashboard's environment variables.
