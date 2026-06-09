CREATE TABLE IF NOT EXISTS kategori (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nama_kategori TEXT NOT NULL,
  deskripsi TEXT
);

CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nama TEXT NOT NULL,
  email TEXT NOT NULL UNIQUE,
  password TEXT NOT NULL,
  role TEXT NOT NULL CHECK(role IN ('ADMIN','PETUGAS','MAHASISWA')),
  created_at TEXT NOT NULL DEFAULT (datetime('now'))
);

CREATE TABLE IF NOT EXISTS barang (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  id_kategori INTEGER NOT NULL,
  nama_barang TEXT NOT NULL,
  kode_barang TEXT NOT NULL UNIQUE,
  jumlah_total INTEGER NOT NULL DEFAULT 0,
  jumlah_tersedia INTEGER NOT NULL DEFAULT 0,
  kondisi TEXT NOT NULL CHECK(kondisi IN ('BAIK','RUSAK_RINGAN','RUSAK_BERAT')),
  lokasi TEXT NOT NULL,
  harga REAL NOT NULL DEFAULT 0.00,
  FOREIGN KEY (id_kategori) REFERENCES kategori(id)
);

CREATE INDEX IF NOT EXISTS idx_barang_kategori ON barang(id_kategori);

CREATE TABLE IF NOT EXISTS peminjaman (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  id_peminjam INTEGER NOT NULL,
  id_petugas INTEGER,
  tgl_pinjam TEXT NOT NULL,
  tgl_kembali TEXT NOT NULL,
  status TEXT NOT NULL CHECK(status IN ('MENUNGGU','DISETUJUI','DITOLAK','DIKEMBALIKAN')),
  catatan TEXT,
  FOREIGN KEY (id_peminjam) REFERENCES users(id),
  FOREIGN KEY (id_petugas) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS idx_peminjaman_peminjam ON peminjaman(id_peminjam);
CREATE INDEX IF NOT EXISTS idx_peminjaman_petugas ON peminjaman(id_petugas);

CREATE TABLE IF NOT EXISTS detail_peminjaman (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  id_peminjaman INTEGER NOT NULL,
  id_barang INTEGER NOT NULL,
  jumlah INTEGER NOT NULL,
  kondisi_kembali TEXT CHECK(kondisi_kembali IN ('BAIK','RUSAK_RINGAN','RUSAK_BERAT')),
  FOREIGN KEY (id_peminjaman) REFERENCES peminjaman(id),
  FOREIGN KEY (id_barang) REFERENCES barang(id)
);

CREATE INDEX IF NOT EXISTS idx_detail_peminjaman_peminjaman ON detail_peminjaman(id_peminjaman);
CREATE INDEX IF NOT EXISTS idx_detail_peminjaman_barang ON detail_peminjaman(id_barang);

INSERT OR IGNORE INTO kategori (id, nama_kategori, deskripsi) VALUES (1, 'Alat Pembantu', 'alat untuk membantu pekerjaan laboratorium');
INSERT OR IGNORE INTO kategori (id, nama_kategori, deskripsi) VALUES (2, 'Alat Utama', 'alat utama yang di perlukaan saat penggunaan ruanv laboratorium');
INSERT OR IGNORE INTO kategori (id, nama_kategori, deskripsi) VALUES (4, 'bahan kimia larutan', 'tidak bahaya');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (2, 'alif', 'alifkhasan7@gmail.com', '$2a$10$WzrN128L1tFSjUm4yDuqMeawExUV9H.b53xj9j3os41JXmZCnI77a', 'ADMIN', '2026-06-02T08:20:01.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (3, 'ngalimunghofur', 'fs3650835@gmail.com', '$2a$10$WxDewcyI4uDQ2QXWZh1ree/sPRza/W4JrXVEv.w60/7748wkVHjqW', 'MAHASISWA', '2026-06-02T08:23:23.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (5, 'dino', 'dino@gmail.com', '$2a$10$ScAG2J/7kAo0yVBc7mZo5Oa2YDMdhjc6nOlmb1I9WWvgylT11kEm.', 'MAHASISWA', '2026-06-02T09:05:01.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (6, 'Lulu', 'lulu@email.com', '$2a$10$b5NmP8coBGVnVTlGuUYwbOBgNFKC1cgyAWQ1mhhjpdOmRPKsyrAT2', 'MAHASISWA', '2026-06-02T09:22:41.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (7, 'indah', 'indah@email.com', '$2a$10$iEKt2v1R/boWoIEOBUjgkupLB.EcFgNha1BD9WxTalLl9cX7b8.Qm', 'MAHASISWA', '2026-06-02T09:22:42.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (9, 'indah2', 'indah2@etmin.com', '$2a$10$csNRc0O3Y.vLwGTsUJ.V5OlTLNXmwnIcmMdqMMg99YPRJeQ3wdjwG', 'ADMIN', '2026-06-02T09:30:19.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (10, 'banu prabowo', 'banu@gmail.com', '$2a$10$FA6LWukAjIuEkfY.IULAfec4LrLLuDizcYrG5.nYfWe.FHTTnMxhO', 'MAHASISWA', '2026-06-03T01:39:29.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (11, 'admin', 'admin@lab', '$2a$10$itPKAZYFhZ9f4GIAqJ5VNOsGPRCH98rjjT8M/P3ECSznMrMUoNMoy', 'ADMIN', '2026-06-04T09:50:48.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (13, 'dani', 'dani@dn.com', '$2a$10$6M6leuIkevxDEDdLlZmvCObHl.cJ/Ktp7bQhz3Uune8BfAEkWTSPS', 'ADMIN', '2026-06-06T13:03:11.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (14, 'adin', 'adin@ad', '$2a$10$3ylmAzxaI1HTFigv5ml5xeaqrJQenEZj8RJtG/KT.ixadM.skC69q', 'PETUGAS', '2026-06-06T13:07:13.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (15, 'banu', 'banu@bn.com', '$2a$10$EUQpES1lTQKMmNwJLH3Lyu38FDN54H0bWXaJuOltpeXlT.qjnCcHC', 'MAHASISWA', '2026-06-06T13:08:35.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (16, 'yani', 'yani@gmail.com', '$2a$10$NYDTotDUTbg.1MifbYB1z.KsHLydTZJgQYDGOSKcRDWmJWAHVooEi', 'ADMIN', '2026-06-08T01:42:39.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (17, 'zaza', 'zaza@gmail.com', '$2a$10$8ZVxgKcWpmgS50pGAyVyuuy.DGXynieDxnsd9TQ6S2us/bHAoUWji', 'MAHASISWA', '2026-06-08T06:32:16.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (18, 'deni', 'deni@dn.com', '$2a$10$W5r09617AO7R2NqYirVexO3k0r9avVs3/pkQCPcZ.3D4.SSU2ZJsu', 'PETUGAS', '2026-06-08T06:33:49.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (19, 'prabowo', 'prabowo@admin', '$2a$10$I/ohMpTHjIDUJziuzq11XOjNYY8wzLrFXL8WjewjQB40YjISXeiTK', 'ADMIN', '2026-06-09T02:06:50.000Z');
INSERT OR IGNORE INTO users (id, nama, email, password, role, created_at) VALUES (20, 'johan', 'johan@jh', '$2a$10$k8qEbzCfyFd26q5QstWXFONBXA9.1BTtVKXtAp944d2/z7WraaCdS', 'ADMIN', '2026-06-09T07:14:21.000Z');
INSERT OR IGNORE INTO barang (id, id_kategori, nama_barang, kode_barang, jumlah_total, jumlah_tersedia, kondisi, lokasi, harga) VALUES (1, 1, 'buku', '034', 23, 16, 'BAIK', 'Rak 3', 5000000);
INSERT OR IGNORE INTO barang (id, id_kategori, nama_barang, kode_barang, jumlah_total, jumlah_tersedia, kondisi, lokasi, harga) VALUES (2, 1, 'penggaris', '09', 9, 8, 'BAIK', 'Rak 5', 30000000);
INSERT OR IGNORE INTO barang (id, id_kategori, nama_barang, kode_barang, jumlah_total, jumlah_tersedia, kondisi, lokasi, harga) VALUES (3, 1, 'Penghapus', '027', 15, 15, 'BAIK', 'Rak 1', 1500000);
INSERT OR IGNORE INTO barang (id, id_kategori, nama_barang, kode_barang, jumlah_total, jumlah_tersedia, kondisi, lokasi, harga) VALUES (4, 2, 'uranium', '032', 20, 20, 'BAIK', 'di bawah bumi', 100000000000);
INSERT OR IGNORE INTO barang (id, id_kategori, nama_barang, kode_barang, jumlah_total, jumlah_tersedia, kondisi, lokasi, harga) VALUES (5, 1, 'atom', '45', 30, 30, 'BAIK', 'brankas', 5000000);
INSERT OR IGNORE INTO barang (id, id_kategori, nama_barang, kode_barang, jumlah_total, jumlah_tersedia, kondisi, lokasi, harga) VALUES (6, 2, 'daemon core', '384', 1, 0, 'RUSAK_RINGAN', 'cernubil', 13000000000);
INSERT OR IGNORE INTO barang (id, id_kategori, nama_barang, kode_barang, jumlah_total, jumlah_tersedia, kondisi, lokasi, harga) VALUES (8, 4, 'NaCl', '4a97', 1, 1, 'BAIK', 'rak no.69', 100000);
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (1, 5, 2, '2026-06-01T17:00:00.000Z', '2026-07-22T17:00:00.000Z', 'DIKEMBALIKAN', 'pinjam dulu seratus');
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (2, 5, 2, '2026-06-01T17:00:00.000Z', '2026-06-08T17:00:00.000Z', 'DITOLAK', 'wow');
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (3, 5, 2, '2026-06-01T17:00:00.000Z', '2026-06-08T17:00:00.000Z', 'DIKEMBALIKAN', 'sat');
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (4, 6, 2, '2026-06-01T17:00:00.000Z', '2026-06-08T17:00:00.000Z', 'DISETUJUI', 'Pinjem yh min, buku 5 juta nya');
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (5, 7, 2, '2026-06-01T17:00:00.000Z', '2026-06-08T17:00:00.000Z', 'DISETUJUI', 'pinjam penggaris 30jt nya yh');
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (6, 3, 2, '2026-06-01T17:00:00.000Z', '2026-06-08T17:00:00.000Z', 'DISETUJUI', 'hai');
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (7, 15, 13, '2026-06-05T17:00:00.000Z', '2026-06-12T17:00:00.000Z', 'DIKEMBALIKAN', 'ya');
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (8, 17, 18, '2026-06-07T17:00:00.000Z', '2026-06-14T17:00:00.000Z', 'DISETUJUI', 'wkwkwkkw');
INSERT OR IGNORE INTO peminjaman (id, id_peminjam, id_petugas, tgl_pinjam, tgl_kembali, status, catatan) VALUES (9, 3, NULL, '2026-06-08T17:00:00.000Z', '2026-06-15T17:00:00.000Z', 'MENUNGGU', NULL);
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (1, 1, 1, 1, 'BAIK');
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (2, 2, 1, 1, NULL);
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (3, 3, 1, 1, 'BAIK');
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (4, 4, 1, 1, NULL);
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (5, 5, 2, 1, NULL);
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (6, 6, 1, 1, NULL);
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (7, 7, 1, 10, NULL);
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (8, 7, 3, 14, 'BAIK');
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (9, 8, 1, 5, NULL);
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (10, 8, 6, 1, NULL);
INSERT OR IGNORE INTO detail_peminjaman (id, id_peminjaman, id_barang, jumlah, kondisi_kembali) VALUES (11, 9, 1, 1, NULL);
