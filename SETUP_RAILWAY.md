# Deploy Backend ke Railway

## Prerequisites
- Akun [Railway](https://railway.app)
- GitHub repository backend sudah di-push

## Langkah Deployment

### 1. Push Backend ke GitHub
```bash
cd BE-Java-mvn-spring-boot
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/username/backend-repo.git
git push -u origin main
```

### 2. Deploy di Railway
1. Login ke [Railway Dashboard](https://railway.app/dashboard)
2. Klik **New Project** → **Deploy from GitHub repo**
3. Pilih repository backend
4. Railway akan auto-detect `Dockerfile` dan build

### 3. Setup Database (MySQL/MariaDB)
1. Di project Railway, klik **New** → **Database** → **Add MySQL** (atau **Add MariaDB**)
2. Railway akan membuat database dengan environment variables:
   - `MYSQL_URL` / `DATABASE_URL`
   - `MYSQL_USER` / `DATABASE_USERNAME`
   - `MYSQL_PASSWORD` / `DATABASE_PASSWORD`

### 4. Set Environment Variables
Di Railway dashboard → Variables, tambahkan:

```
PORT = 4000
DATABASE_URL = jdbc:mariadb://<host>:<port>/<db> (isi dari Railway MySQL)
DATABASE_USERNAME = (isi dari Railway MySQL)
DATABASE_PASSWORD = (isi dari Railway MySQL)
JWT_SECRET = (generate random 256-bit hex key)
JWT_EXPIRATION = 86400000
SHOW_SQL = false
```

> **Catatan:** Railway MySQL otomatis menyediakan env var `MYSQL_URL`, `MYSQL_USER`, `MYSQL_PASSWORD`. Jika menggunakan Railway MySQL, pastikan `DATABASE_URL` menggunakan value dari env var Railway.

### 5. Deploy
- Railway akan otomatis build & deploy
- Backend akan running di `https://<project>.railway.app`

### 6. CORS Update
Setelah dapat URL Railway, update **SecurityConfig.java**:
```java
config.setAllowedOrigins(Arrays.asList(
    "https://<frontend>.vercel.app",
    "http://localhost:5173"
));
```

---

## Struktur File untuk Railway

| File | Fungsi |
|------|--------|
| `Dockerfile` | Build & run container (Java 21) |
| `Procfile` | Start command dengan `$PORT` |
| `railway.json` | Konfigurasi Railway project |
| `src/main/resources/application.yml` | Config dengan env vars |

---

## Environment Variables

| Variable | Default | Keterangan |
|----------|---------|------------|
| `PORT` | `4000` | Port aplikasi |
| `DATABASE_URL` | `jdbc:mariadb://localhost:3306/inventori_lab` | JDBC URL |
| `DATABASE_USERNAME` | `root` | DB username |
| `DATABASE_PASSWORD` | `alif` | DB password |
| `JWT_SECRET` | (default) | Secret key JWT (256-bit hex) |
| `JWT_EXPIRATION` | `86400000` | Token expiry (24h) |
| `SHOW_SQL` | `true` | Show JPA SQL logs |

---

## Testing

```bash
# Test health check
curl https://<project>.railway.app/api/auth/login

# Test register
curl -X POST https://<project>.railway.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nama":"User","email":"user@test.com","password":"123456"}'

# Test login
curl -X POST https://<project>.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"123456"}'
```
