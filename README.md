# Sistem Inventaris Laboratorium - Backend API

REST API untuk sistem manajemen inventaris laboratorium dengan fitur peminjaman barang, pengelolaan kategori, dan laporan. Dibangun dengan Spring Boot 3.x dan JWT Authentication.

**Stack:** Java 17+ · Spring Boot 3.2.5 · MariaDB/MySQL · JWT · Maven

---

## Daftar Isi

- [Fitur](#fitur)
- [Teknologi](#teknologi)
- [Prasyarat](#prasyarat)
- [Cara Coba (Quick Start)](#cara-coba-quick-start)
- [Docker](#docker)
- [Akun Bawaan (Seeder)](#akun-bawaan-seeder)
- [API Endpoints](#api-endpoints)
- [Struktur Project](#struktur-project)
- [Struktur Database](#struktur-database)
- [Format Response](#format-response)
- [Dokumentasi Lain](#dokumentasi-lain)

---

## Fitur

### Autentikasi & Otorisasi
- Register & Login dengan JWT token
- Role-based access: `ADMIN`, `PETUGAS`, `MAHASISWA`
- Stateless session (tidak pakai session/cookie)

### Manajemen Barang (Inventory)
- CRUD barang inventaris
- Setiap barang memiliki kategori, kondisi (BAIK/RUSAK_RINGAN/RUSAK_BERAT), stok, lokasi, harga
- Stok otomatis berkurang saat peminjaman disetujui dan bertambah saat dikembalikan

### Manajemen Kategori
- CRUD kategori barang
- Setiap kategori memiliki deskripsi

### Manajemen Peminjaman
- Mahasiswa mengajukan peminjaman (status: MENUNGGU)
- Petugas/Admin menyetujui atau menolak
- Barang dikembalikan dengan pengecekan kondisi
- Mahasiswa hanya bisa melihat peminjaman miliknya sendiri

### Laporan
- Laporan stok barang (total, kondisi, nilai aset)
- Laporan statistik peminjaman (total, disetujui, ditolak, dikembalikan)

### Fitur Teknis
- Pagination, sorting pada semua endpoint list
- Standardized response wrapper (code, status, message, data, paging)
- Exception handling global (@RestControllerAdvice)
- Database auto-seeder (CommandLineRunner)
- CORS support untuk frontend development
- OpenAPI Specification (openapi.yaml)
- Siap dijalankan dengan Docker

---

## Teknologi

| Komponen | Versi |
|----------|-------|
| Java | 17+ (17 untuk Docker, 21 untuk development) |
| Spring Boot | 3.2.5 |
| Spring Data JPA | - |
| Spring Security | - |
| MariaDB Driver | 3.5.8 |
| MySQL Connector | - |
| Lombok | edge-SNAPSHOT |
| JWT (JJWT) | 0.11.5 |
| Maven | 3.8+ |
| Docker | 24+ (opsional) |

---

## Prasyarat

- **Java 17+** (JDK)
  ```bash
  java -version
  ```
- **Maven 3.8+**
  ```bash
  mvn -version
  ```
- **MariaDB** atau **MySQL** (running di port 3306)
- Atau **Docker** + **Docker Compose** (alternatif)

---

## Cara Coba (Quick Start)

### 1. Clone & masuk direktori

```bash
cd BE-Java-mvn-spring-boot
```

### 2. Setup database

Buat database MariaDB/MySQL:

```sql
CREATE DATABASE IF NOT EXISTS inventori_lab CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Atau jika pakai Docker, langsung jalankan:

```bash
docker-compose up -d mysql
```

### 3. Jalankan aplikasi

```bash
mvn spring-boot:run
```

Aplikasi akan jalan di `http://localhost:4000`.

> Database table dan data awal (3 user, 2 kategori, 3 barang) akan **otomatis dibuat** saat pertama kali aplikasi dijalankan.

### 4. Coba login

```bash
curl -X POST http://localhost:4000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@lab.com", "password": "admin123"}'
```

Gunakan response token untuk request berikutnya:

```bash
curl -X GET http://localhost:4000/api/barang \
  -H "Authorization: Bearer <token>"
```

---

## Docker

### Build & Jalankan (Full Stack)

```bash
docker-compose up --build
```

Akses di `http://localhost:8080`.

### Services

| Service | Port | Deskripsi |
|---------|------|-----------|
| `mysql` | 3307 | Database MySQL 8.0 |
| `app` | 8080 | Aplikasi Spring Boot |

> Environment variable `SPRING_DATASOURCE_URL` akan otomatis mengarah ke container MySQL.

---

## Akun Bawaan (Seeder)

Saat pertama kali jalan, aplikasi otomatis membuat data berikut:

### Users

| Email | Password | Role |
|-------|----------|------|
| `admin@lab.com` | `admin123` | ADMIN |
| `petugas@lab.com` | `petugas123` | PETUGAS |
| `mahasiswa@lab.com` | `mahasiswa123` | MAHASISWA |

### Kategori & Barang

| Kategori | Barang |
|----------|--------|
| Alat Elektronik | Oscilloscope Digital (5), Digital Multimeter (10) |
| Alat Gelas | Beaker Glass 250ml (25) |

---

## API Endpoints

**Base URL:** `http://localhost:4000` (development) / `http://localhost:8080` (Docker)

**Auth:** `Authorization: Bearer <token>` (kecuali login & register)

### Authentication (PUBLIC)

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| POST | `/api/auth/register` | Register user baru |
| POST | `/api/auth/login` | Login, mengembalikan JWT token |

### Barang

| Method | Endpoint | Akses | Deskripsi |
|--------|----------|-------|-----------|
| GET | `/api/barang` | All roles | Daftar barang (paginated) |
| GET | `/api/barang/{id}` | All roles | Detail barang |
| POST | `/api/barang` | ADMIN, PETUGAS | Tambah barang |
| PUT/PATCH | `/api/barang/{id}` | ADMIN, PETUGAS | Update barang |
| DELETE | `/api/barang/{id}` | ADMIN, PETUGAS | Hapus barang |

### Kategori

| Method | Endpoint | Akses | Deskripsi |
|--------|----------|-------|-----------|
| GET | `/api/kategori` | All roles | Daftar kategori (paginated) |
| GET | `/api/kategori/{id}` | All roles | Detail kategori |
| POST | `/api/kategori` | ADMIN, PETUGAS | Tambah kategori |
| PUT/PATCH | `/api/kategori/{id}` | ADMIN, PETUGAS | Update kategori |
| DELETE | `/api/kategori/{id}` | ADMIN, PETUGAS | Hapus kategori |

### Peminjaman

| Method | Endpoint | Akses | Deskripsi |
|--------|----------|-------|-----------|
| POST | `/api/peminjaman` | All roles | Buat permintaan peminjaman |
| GET | `/api/peminjaman` | All roles | Daftar peminjaman (paginated, MAHASISWA hanya miliknya) |
| GET | `/api/peminjaman/{id}` | All roles | Detail peminjaman (MAHASISWA hanya miliknya) |
| PATCH | `/api/peminjaman/{id}/approve` | ADMIN, PETUGAS | Setujui peminjaman |
| PATCH | `/api/peminjaman/{id}/reject` | ADMIN, PETUGAS | Tolak peminjaman |
| PATCH | `/api/peminjaman/{id}/kembalikan` | ADMIN, PETUGAS | Kembalikan barang |

### Laporan

| Method | Endpoint | Akses | Deskripsi |
|--------|----------|-------|-----------|
| GET | `/api/laporan/stok` | ADMIN, PETUGAS | Laporan stok & nilai aset |
| GET | `/api/laporan/peminjaman` | ADMIN, PETUGAS | Statistik peminjaman |

### Query Parameters (untuk endpoint list)

| Parameter | Default | Deskripsi |
|-----------|---------|-----------|
| `page` | 0 | Halaman (0-index) |
| `size` | 10 | Jumlah data per halaman |
| `sortBy` | `id` | Field sorting |
| `sortDir` | `ASC` | Arah sorting (`ASC` / `DESC`, peminjaman default `DESC`) |

---

## Struktur Project

```
src/
├── main/java/com/inventorilab/
│   ├── InventoriLabApplication.java      # Entry point
│   ├── controller/                        # REST controllers
│   ├── service/
│   │   ├── interfaces/                    # Service interfaces
│   │   └── impl/                          # Implementations
│   ├── entity/                            # JPA entities
│   ├── repository/                        # Spring Data JPA repositories
│   ├── dto/
│   │   ├── request/                       # Request DTOs
│   │   └── response/                      # Response DTOs
│   ├── security/                          # JWT, SecurityConfig, filter
│   ├── enums/                             # Role, StatusPeminjaman, KondisiBarang
│   ├── mapper/                            # Entity ↔ DTO mapper
│   ├── response/                          # WebResponse wrapper, PagingResponse
│   ├── exception/                         # Custom exceptions + GlobalExceptionHandler
│   └── seeder/                            # DatabaseSeeder (data awal)
│
├── main/resources/
│   └── application.yml                    # Konfigurasi utama
│
└── test/java/                             # Unit test (TODO)
```

---

## Struktur Database

### Entity Relationship

```
┌──────────┐       ┌───────────────┐
│   User   │       │   Kategori    │
└────┬─────┘       └───────┬───────┘
     │                     │
     │ (peminjam/          │ (one-to-many)
     │  petugas)           │
     │                     ▼
     │              ┌───────────┐
     ├──────────────┤  Barang   │
     │              └───────────┘
     │
     │ (peminjam)       ┌──────────────┐
     └─────────────────►│  Peminjaman  │
                        └──────┬───────┘
                               │ (one-to-many)
                               ▼
                        ┌──────────────────┐
                        │ DetailPeminjaman │
                        └──────────────────┘
```

### Tabel `users`

| Field | Type | Keterangan |
|-------|------|------------|
| id | BIGINT (PK) | Auto increment |
| nama | VARCHAR | Nama lengkap |
| email | VARCHAR (unique) | Email login |
| password | VARCHAR | Hash BCrypt |
| role | ENUM | `MAHASISWA`, `PETUGAS`, `ADMIN` |
| created_at | TIMESTAMP | Auto generate |

### Tabel `kategori`

| Field | Type | Keterangan |
|-------|------|------------|
| id | BIGINT (PK) | Auto increment |
| nama_kategori | VARCHAR | Nama kategori |
| deskripsi | TEXT | Deskripsi kategori |

### Tabel `barang`

| Field | Type | Keterangan |
|-------|------|------------|
| id | BIGINT (PK) | Auto increment |
| id_kategori | BIGINT (FK) | Ref ke tabel kategori |
| nama_barang | VARCHAR | Nama barang |
| kode_barang | VARCHAR (unique) | Kode unik barang |
| jumlah_total | INT | Total stok |
| jumlah_tersedia | INT | Stok tersedia |
| kondisi | ENUM | `BAIK`, `RUSAK_RINGAN`, `RUSAK_BERAT` |
| lokasi | VARCHAR | Lokasi penyimpanan |
| harga | DECIMAL | Harga satuan |

### Tabel `peminjaman`

| Field | Type | Keterangan |
|-------|------|------------|
| id | BIGINT (PK) | Auto increment |
| id_peminjam | BIGINT (FK) | Ref ke users (MAHASISWA) |
| id_petugas | BIGINT (FK) | Ref ke users (PETUGAS/ADMIN yang approve) |
| tgl_pinjam | TIMESTAMP | Tanggal peminjaman |
| tgl_kembali | TIMESTAMP | Tanggal pengembalian |
| status | ENUM | `MENUNGGU`, `DISETUJUI`, `DITOLAK`, `DIKEMBALIKAN` |
| catatan | VARCHAR | Catatan peminjaman |

### Tabel `detail_peminjaman`

| Field | Type | Keterangan |
|-------|------|------------|
| id | BIGINT (PK) | Auto increment |
| id_peminjaman | BIGINT (FK) | Ref ke peminjaman |
| id_barang | BIGINT (FK) | Ref ke barang |
| jumlah | INT | Jumlah barang |
| kondisi_kembali | ENUM | Diisi saat return: `BAIK`, `RUSAK_RINGAN`, `RUSAK_BERAT` |

---

## Format Response

Semua response menggunakan wrapper standar:

```json
{
  "code": 200,
  "status": "OK",
  "message": "Pesan sukses/gagal",
  "data": { ... },
  "paging": {
    "currentPage": 0,
    "totalPages": 5,
    "size": 10,
    "totalElements": 50
  }
}
```

- `paging` hanya ada pada endpoint list
- `data = null` saat error atau delete

### Error Response (Global Exception Handler)

```json
{
  "code": 404,
  "status": "NOT_FOUND",
  "message": "Data tidak ditemukan",
  "data": null
}
```

---

## Dokumentasi Lain

| File | Deskripsi |
|------|-----------|
| [openapi.yaml](openapi.yaml) | OpenAPI 3.0.3 Specification (full) |
| [CORS_CONFIGURATION.md](CORS_CONFIGURATION.md) | Konfigurasi CORS detail |
| [CORS_QUICK_START.md](CORS_QUICK_START.md) | Panduan cepat CORS |
| [implementation_plan.md](implementation_plan.md) | Rencana implementasi |
| [artifacts/postman_collection.json](artifacts/postman_collection.json) | Postman collection untuk testing |

---

**Last Updated:** 31 Mei 2026
**Status:** Development (v0.0.1-SNAPSHOT)
