# LedgerSplit — Authentication Notes

Documentation of the JWT-based authentication system: what was built, why, and how the pieces fit together. Written as a running log so it can be re-read later without re-deriving everything from scratch.

---

## 1. Architecture Overview

Spring Security is a **filter-based framework**. Every HTTP request passes through a chain of filters before it reaches a controller. Each filter checks something specific (is there a token? is it valid? is this URL public?) and either lets the request through or rejects it.

```
Request comes in
      ↓
JwtAuthFilter (custom) → reads token, sets who's logged in (if valid)
      ↓
SecurityFilterChain rules → is this URL public, or does it need auth?
      ↓
   (passes) → reaches Controller
   (fails)  → rejected with 401, controller never runs
```

This app uses **stateless JWT auth**, not session/cookie-based login. That means:
- No server-side session is created or remembered (`SessionCreationPolicy.STATELESS`)
- Every single request must prove who it is independently, via a token in the `Authorization` header
- The backend's only jobs are: **generate** tokens (on login) and **verify** tokens (on every protected request)

```
Generate → Store → Send → Verify
```
- **Generate** and **Verify**: backend responsibility
- **Store** and **Send**: client responsibility (e.g. React frontend decides where to keep the token and attaches it to requests)

---

## 2. Request Flow (End-to-End)

### Registration
```
POST /api/v1/auth/register
   ↓
AuthController.register()
   ↓
AuthService.register() → hashes password (BCrypt) → saves User to DB
   ↓
Returns saved User
```
No authentication involved — this endpoint is public (`permitAll()`), since a not-yet-registered user obviously has no token.

### Login
```
POST /api/v1/auth/login  { email, password }
   ↓
AuthController.login()
   ↓
AuthenticationManager.authenticate(email, password)
   ↓
   → delegates to DaoAuthenticationProvider
   → which calls CustomUserDetailsService.loadUserByUsername(email)
   → which calls PasswordEncoder.matches(rawPassword, storedHash)
   ↓
If valid → JWTService.generateToken(email) → returns JWT to client
If invalid → throws BadCredentialsException
```

### Accessing a protected endpoint
```
GET /api/v1/groups/1/members
Authorization: Bearer <token>
   ↓
JwtAuthFilter.doFilterInternal()
   ↓
Extract token from header → JWTService.extractEmail(token)
   ↓
CustomUserDetailsService.loadUserByUsername(email) → loads UserPrincipal
   ↓
JWTService.isTokenValid(token, email) → checks signature + expiry
   ↓
If valid → SecurityContextHolder.setAuthentication(...) → request proceeds
If invalid/missing → request continues unauthenticated → rejected later with 401
```

---

## 3. Class-by-Class Reference

| Class | Package | Responsibility |
|---|---|---|
| `SecurityConfig` | `config` | Defines all Spring Security beans: password encoder, auth provider, auth manager, and the filter chain (which URLs are public vs protected) |
| `OpenApiConfig` | `config` | Cosmetic/testing only — tells Swagger UI this API uses Bearer auth, so the "Authorize" button appears |
| `UserPrincipal` | `security` | Wraps the `User` entity so Spring Security can understand it (implements `UserDetails`) |
| `CustomUserDetailsService` | `security` | Loads a `User` from the DB by email, wraps it in `UserPrincipal`. Used both during login (via `AuthenticationManager`) and on every request (via `JwtAuthFilter`) |
| `JWTService` | `security` | Generates JWTs, extracts claims (email, expiry) from them, validates them |
| `JwtAuthFilter` | `security` | Runs on every request; reads the `Authorization` header, validates the token, and sets the authenticated user in `SecurityContextHolder` |
| `AuthController` | `controller` | Exposes `/register` and `/login` endpoints |
| `AuthService` | `service` | Registration business logic — hashes password, saves user with default role `USER` |

---

## 4. Key Concepts (in plain terms)

**`UserDetailsService` vs `CustomUserDetailsService`**
`UserDetailsService` is Spring's interface (just a contract: "must have a `loadUserByUsername` method"). `CustomUserDetailsService` is our implementation of that contract, using our own `UserRepository`. The `Custom` prefix is just naming convention, not a Spring requirement.

**`AuthenticationManager` vs `DaoAuthenticationProvider`**
Analogy: a nightclub bouncer.
- `AuthenticationManager` = the bouncer himself — the one thing we actually call (`.authenticate(...)`) from the login endpoint.
- `DaoAuthenticationProvider` = the bouncer's specific method for checking IDs — in our case, "look the user up in the database and compare password hashes." ("Dao" = Data Access Object.)

**Why `OncePerRequestFilter`?**
Guarantees our filter logic runs exactly once per request, even in edge cases like internal request forwarding. This is the standard Spring base class for this kind of filter — used almost universally for JWT/session-checking filters.

**Why `extractClaim` is generic**
A JWT holds multiple fields (claims) — subject/email, issued-at, expiry. Parsing + verifying the token is identical work regardless of which field you want, so `extractClaim` does that shared work once, then takes a function argument (e.g. `Claims::getSubject` or `Claims::getExpiration`) to decide which specific field to return. Avoids duplicating the parse/verify logic per field.

**Default-deny security model**
```java
.requestMatchers("/api/v1/auth/register", "/api/v1/auth/login", ...).permitAll()
.anyRequest().authenticated()
```
Everything is locked by default; only explicitly listed paths are public. This means new endpoints are automatically protected without needing to remember to add them anywhere — you only touch this list when adding a new *public* route.

---

## 5. Gotchas Hit Along the Way (worth remembering)

1. **Adding a custom `PasswordEncoder` bean silently disables Spring Boot's auto-generated default login.** This is why the console-generated password stopped appearing once `SecurityConfig` was added.
2. **No `SecurityFilterChain` = everything locked, including your own public endpoints.** Registration and Swagger docs were blocked until explicit `permitAll()` rules were added.
3. **Path mismatches cause silent 401s.** `/api/auth/register` vs the actual `/api/v1/auth/register` — always compare the *exact* request URL (visible in Swagger) against the `requestMatchers()` list.
4. **`Jwts.parserBuilder()` / `.setSubject()` / `.parseClaimsJws()` are from the old jjwt 0.11.x API.** On jjwt 0.12.x, use `Jwts.parser()`, `.subject(...)`, `.parseSignedClaims()`, `.getPayload()`.
5. **`jwt.secret` should be Base64-encoded**, generated via `openssl rand -base64 32` — not a typed-out plaintext string. Decoded via `Decoders.BASE64.decode(secret)` before building the signing key.
6. **Swagger UI does not show an "Authorize" button by default.** It needs an explicit `OpenAPI` bean (`OpenApiConfig`) declaring a Bearer security scheme — this is unrelated to actual Spring Security config, purely for the docs/testing UI.
7. **An unhandled `BadCredentialsException` on bad login currently returns a raw 500**, not a clean 401 — flagged as a TODO (see below).
8. **Never commit the real `jwt.secret` value.** Should be injected via environment variable (`${JWT_SECRET}`) rather than hardcoded in `application.properties`.

---

## 6. Known TODOs / Not Yet Done

- [ ] Handle `BadCredentialsException` (and similar) with a proper `@ExceptionHandler` → clean 401 JSON response instead of a raw 500
- [ ] Move `jwt.secret` to an environment variable, confirm `.gitignore` excludes any local secrets file
- [ ] Convert `role` field on `User` from a raw `String` to an `enum Role { USER, ADMIN }` (currently hardcoded as `"USER"` string in `AuthService`)
- [ ] Strip `passwordHash` out of the `User` object returned by `/register` (currently exposes the hash in the response)
- [ ] Decide on refresh-token strategy (current token is short-lived with no renewal path)
- [ ] Build a protected test endpoint (e.g. `GET /api/v1/users/me`) as a permanent sanity check for the auth chain

---

## 7. Environment Properties Reference

```properties
jwt.secret=${JWT_SECRET}          # Base64-encoded, generate via: openssl rand -base64 32
jwt.expiration=86400000           # milliseconds — 24 hours
```

Different secrets should be used per environment (dev/staging/prod) — never shared across them.

---

*Last updated: after implementing JwtAuthFilter + wiring it into SecurityFilterChain + adding Swagger Bearer auth support.*