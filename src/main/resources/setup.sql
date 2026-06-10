-- =============================================================
-- SETUP DATABASE - Sistem Inventaris Laboratorium
-- =============================================================
-- Jalankan file ini di MariaDB sebelum pertama kali menjalankan
-- aplikasi. Script ini membuat database, tabel, dan akun awal.
--
-- Cara menjalankan:
--   mysql -u root -p < setup.sql
--   atau: SOURCE /path/to/setup.sql; (dari dalam mysql shell)
-- =============================================================


-- -------------------------------------------------------------
-- 1. DATABASE
-- -------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS inventori_lab
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE inventori_lab;


-- -------------------------------------------------------------
-- 2. TABEL USERS
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    nama       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       ENUM('ADMIN','PETUGAS','MAHASISWA') NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -------------------------------------------------------------
-- 3. TABEL KATEGORI
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS kategori (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    nama_kategori  VARCHAR(100) NOT NULL,
    deskripsi      TEXT,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -------------------------------------------------------------
-- 4. TABEL BARANG
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS barang (
    id               BIGINT         NOT NULL AUTO_INCREMENT,
    id_kategori      BIGINT         NOT NULL,
    nama_barang      VARCHAR(150)   NOT NULL,
    kode_barang      VARCHAR(50)    NOT NULL UNIQUE,
    jumlah_total     INT            NOT NULL DEFAULT 0,
    jumlah_tersedia  INT            NOT NULL DEFAULT 0,
    kondisi          ENUM('BAIK','RUSAK_RINGAN','RUSAK_BERAT') NOT NULL DEFAULT 'BAIK',
    lokasi           VARCHAR(100),
    harga            DECIMAL(15,2)  NOT NULL DEFAULT 0.00,
    PRIMARY KEY (id),
    CONSTRAINT fk_barang_kategori FOREIGN KEY (id_kategori) REFERENCES kategori(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -------------------------------------------------------------
-- 5. TABEL PEMINJAMAN
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS peminjaman (
    id           BIGINT NOT NULL AUTO_INCREMENT,
    id_peminjam  BIGINT NOT NULL,
    id_petugas   BIGINT,
    tgl_pinjam   DATE   NOT NULL,
    tgl_kembali  DATE   NOT NULL,
    status       ENUM('MENUNGGU','DISETUJUI','DITOLAK','DIKEMBALIKAN') NOT NULL DEFAULT 'MENUNGGU',
    catatan      TEXT,
    PRIMARY KEY (id),
    CONSTRAINT fk_peminjaman_peminjam FOREIGN KEY (id_peminjam) REFERENCES users(id),
    CONSTRAINT fk_peminjaman_petugas  FOREIGN KEY (id_petugas)  REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -------------------------------------------------------------
-- 6. TABEL DETAIL PEMINJAMAN
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS detail_peminjaman (
    id              BIGINT NOT NULL AUTO_INCREMENT,
    id_peminjaman   BIGINT NOT NULL,
    id_barang       BIGINT NOT NULL,
    jumlah          INT    NOT NULL DEFAULT 1,
    kondisi_kembali ENUM('BAIK','RUSAK_RINGAN','RUSAK_BERAT'),
    PRIMARY KEY (id),
    CONSTRAINT fk_detail_peminjaman FOREIGN KEY (id_peminjaman) REFERENCES peminjaman(id) ON DELETE CASCADE,
    CONSTRAINT fk_detail_barang     FOREIGN KEY (id_barang)     REFERENCES barang(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- -------------------------------------------------------------
-- 7. TABEL SHOP_PRODUCTS (terpisah dari barang inventaris)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS shop_products (
    id              BIGINT         NOT NULL AUTO_INCREMENT,
    id_user         BIGINT         NOT NULL,
    nama_produk     VARCHAR(150)   NOT NULL,
    deskripsi       TEXT,
    harga           DECIMAL(15,2)  NOT NULL DEFAULT 0.00,
    stok            INT            NOT NULL DEFAULT 0,
    gambar_url      TEXT,
    kategori        VARCHAR(100),
    tags            TEXT,
    created_at      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_shop_product_user
        FOREIGN KEY (id_user) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =============================================================
-- 8. AKUN AWAL
-- =============================================================
-- Password di-hash menggunakan BCrypt(strength=10).
-- JANGAN ubah kolom password secara manual tanpa generate hash
-- ulang via aplikasi atau script di bawah.
--
-- Password plaintext:
--   admin@lab.com   -> admin123
--   petugas@lab.com -> petugas123
--
-- Untuk generate hash baru (jika ingin ganti password), jalankan
-- endpoint register atau gunakan query verifikasi di bagian 9.
-- =============================================================
-- =============================================================
-- Password di-hash menggunakan BCrypt(strength=10).
-- JANGAN ubah kolom password secara manual tanpa generate hash
-- ulang via aplikasi atau script di bawah.
--
-- Password plaintext:
--   admin@lab.com   -> admin123
--   petugas@lab.com -> petugas123
--
-- Untuk generate hash baru (jika ingin ganti password), jalankan
-- endpoint register atau gunakan query verifikasi di bagian 8.
-- =============================================================
INSERT INTO users (nama, email, password, role)
SELECT 'Admin Laboratorium', 'admin@lab.com',
       '$2a$10$0bRjJx5eeViiIKt1sx6j3uBdD97gytnkKDwdGfa1NGFS2xE3VRG1a',
       'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@lab.com');

INSERT INTO users (nama, email, password, role)
SELECT 'Petugas Laboratorium', 'petugas@lab.com',
       '$2a$10$gOxSqwv8qLRlP4.cp7FlqOwtyeMXbRNPuBWLh1KRk6lK2nJppx3Ce',
       'PETUGAS'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'petugas@lab.com');


-- =============================================================
-- 9. QUERY VERIFIKASI
-- =============================================================
-- Jalankan query-query berikut untuk memastikan setup sudah benar.

-- 8a. Cek semua user yang sudah terdaftar
SELECT
    id,
    nama,
    email,
    role,
    -- Password harus diawali $2a$ atau $2b$ (BCrypt)
    CASE
        WHEN password LIKE '$2a$%' OR password LIKE '$2b$%'
        THEN 'OK - BCrypt hash valid'
        ELSE 'ERROR - bukan BCrypt hash!'
    END AS status_password,
    LEFT(password, 7) AS prefix_hash,   -- Harus: $2a$10$
    created_at
FROM users
ORDER BY id;


-- 8b. Cek tabel yang sudah dibuat
SELECT
    TABLE_NAME    AS tabel,
    TABLE_ROWS    AS estimasi_baris,
    CREATE_TIME   AS dibuat
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'inventori_lab'
ORDER BY TABLE_NAME;


-- 8c. Verifikasi format BCrypt pada semua user
-- BCrypt valid: diawali $2a$10$ atau $2b$10$, panjang 60 karakter
SELECT
    email,
    role,
    CHAR_LENGTH(password)           AS panjang_hash,   -- Harus 60
    LEFT(password, 7)               AS prefix,         -- Harus $2a$10$
    CASE
        WHEN CHAR_LENGTH(password) = 60
         AND (password LIKE '$2a$10$%' OR password LIKE '$2b$10$%')
        THEN '✓ Valid'
        ELSE '✗ Tidak valid'
    END AS validasi_bcrypt
FROM users;


-- 7. Tabel shop_orders (pesanan dari Shop)
CREATE TABLE IF NOT EXISTS shop_orders (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    id_user      BIGINT       NOT NULL,
    id_product   BIGINT       NOT NULL,
    quantity     INT          NOT NULL DEFAULT 1,
    total_harga  DECIMAL(15,2) NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'MENUNGGU',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME     NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_shop_orders_user
        FOREIGN KEY (id_user) REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_shop_orders_product
        FOREIGN KEY (id_product) REFERENCES shop_products (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 8d. Cek foreign key constraints aktif
SELECT
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'inventori_lab'
  AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME;
