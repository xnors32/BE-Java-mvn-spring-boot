package com.inventorilab.config;

import com.inventorilab.entity.*;
import com.inventorilab.enums.*;
import com.inventorilab.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BarangRepository barangRepository;
    private final KategoriRepository kategoriRepository;
    private final PeminjamanRepository peminjamanRepository;
    private final DetailPeminjamanRepository detailPeminjamanRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded, skipping...");
            return;
        }
        log.info("Seeding database...");

        seedKategori();
        seedUsers();
        seedBarang();
        seedPeminjaman();
        seedDetailPeminjaman();

        log.info("Database seeding complete!");
    }

    private void seedKategori() {
        kategoriRepository.save(Kategori.builder().namaKategori("Alat Pembantu").deskripsi("alat untuk membantu pekerjaan laboratorium").build());
        kategoriRepository.save(Kategori.builder().namaKategori("Alat Utama").deskripsi("alat utama yang di perlukaan saat penggunaan ruanv laboratorium").build());
        kategoriRepository.save(Kategori.builder().namaKategori("bahan kimia larutan").deskripsi("tidak bahaya").build());
    }

    private void seedUsers() {
        userRepository.save(User.builder().nama("alif").email("alifkhasan7@gmail.com").password("$2a$10$WzrN128L1tFSjUm4yDuqMeawExUV9H.b53xj9j3os41JXmZCnI77a").role(Role.ADMIN).createdAt(LocalDateTime.parse("2026-06-02T08:20:01")).build());
        userRepository.save(User.builder().nama("ngalimunghofur").email("fs3650835@gmail.com").password("$2a$10$WxDewcyI4uDQ2QXWZh1ree/sPRza/W4JrXVEv.w60/7748wkVHjqW").role(Role.MAHASISWA).createdAt(LocalDateTime.parse("2026-06-02T08:23:23")).build());
        userRepository.save(User.builder().nama("dino").email("dino@gmail.com").password("$2a$10$ScAG2J/7kAo0yVBc7mZo5Oa2YDMdhjc6nOlmb1I9WWvgylT11kEm.").role(Role.MAHASISWA).createdAt(LocalDateTime.parse("2026-06-02T09:05:01")).build());
        userRepository.save(User.builder().nama("Lulu").email("lulu@email.com").password("$2a$10$b5NmP8coBGVnVTlGuUYwbOBgNFKC1cgyAWQ1mhhjpdOmRPKsyrAT2").role(Role.MAHASISWA).createdAt(LocalDateTime.parse("2026-06-02T09:22:41")).build());
        userRepository.save(User.builder().nama("indah").email("indah@email.com").password("$2a$10$iEKt2v1R/boWoIEOBUjgkupLB.EcFgNha1BD9WxTalLl9cX7b8.Qm").role(Role.MAHASISWA).createdAt(LocalDateTime.parse("2026-06-02T09:22:42")).build());
        userRepository.save(User.builder().nama("indah2").email("indah2@etmin.com").password("$2a$10$csNRc0O3Y.vLwGTsUJ.V5OlTLNXmwnIcmMdqMMg99YPRJeQ3wdjwG").role(Role.ADMIN).createdAt(LocalDateTime.parse("2026-06-02T09:30:19")).build());
        userRepository.save(User.builder().nama("banu prabowo").email("banu@gmail.com").password("$2a$10$FA6LWukAjIuEkfY.IULAfec4LrLLuDizcYrG5.nYfWe.FHTTnMxhO").role(Role.MAHASISWA).createdAt(LocalDateTime.parse("2026-06-03T01:39:29")).build());
        userRepository.save(User.builder().nama("admin").email("admin@lab").password("$2a$10$itPKAZYFhZ9f4GIAqJ5VNOsGPRCH98rjjT8M/P3ECSznMrMUoNMoy").role(Role.ADMIN).createdAt(LocalDateTime.parse("2026-06-04T09:50:48")).build());
        userRepository.save(User.builder().nama("dani").email("dani@dn.com").password("$2a$10$6M6leuIkevxDEDdLlZmvCObHl.cJ/Ktp7bQhz3Uune8BfAEkWTSPS").role(Role.ADMIN).createdAt(LocalDateTime.parse("2026-06-06T13:03:11")).build());
        userRepository.save(User.builder().nama("adin").email("adin@ad").password("$2a$10$3ylmAzxaI1HTFigv5ml5xeaqrJQenEZj8RJtG/KT.ixadM.skC69q").role(Role.PETUGAS).createdAt(LocalDateTime.parse("2026-06-06T13:07:13")).build());
        userRepository.save(User.builder().nama("banu").email("banu@bn.com").password("$2a$10$EUQpES1lTQKMmNwJLH3Lyu38FDN54H0bWXaJuOltpeXlT.qjnCcHC").role(Role.MAHASISWA).createdAt(LocalDateTime.parse("2026-06-06T13:08:35")).build());
        userRepository.save(User.builder().nama("yani").email("yani@gmail.com").password("$2a$10$NYDTotDUTbg.1MifbYB1z.KsHLydTZJgQYDGOSKcRDWmJWAHVooEi").role(Role.ADMIN).createdAt(LocalDateTime.parse("2026-06-08T01:42:39")).build());
        userRepository.save(User.builder().nama("zaza").email("zaza@gmail.com").password("$2a$10$8ZVxgKcWpmgS50pGAyVyuuy.DGXynieDxnsd9TQ6S2us/bHAoUWji").role(Role.MAHASISWA).createdAt(LocalDateTime.parse("2026-06-08T06:32:16")).build());
        userRepository.save(User.builder().nama("deni").email("deni@dn.com").password("$2a$10$W5r09617AO7R2NqYirVexO3k0r9avVs3/pkQCPcZ.3D4.SSU2ZJsu").role(Role.PETUGAS).createdAt(LocalDateTime.parse("2026-06-08T06:33:49")).build());
        userRepository.save(User.builder().nama("prabowo").email("prabowo@admin").password("$2a$10$I/ohMpTHjIDUJziuzq11XOjNYY8wzLrFXL8WjewjQB40YjISXeiTK").role(Role.ADMIN).createdAt(LocalDateTime.parse("2026-06-09T02:06:50")).build());
        userRepository.save(User.builder().nama("johan").email("johan@jh").password("$2a$10$k8qEbzCfyFd26q5QstWXFONBXA9.1BTtVKXtAp944d2/z7WraaCdS").role(Role.ADMIN).createdAt(LocalDateTime.parse("2026-06-09T07:14:21")).build());
    }

    private void seedBarang() {
        Kategori alatPembantu = kategoriRepository.findAll().get(0);
        Kategori alatUtama = kategoriRepository.findAll().get(1);
        Kategori bahanKimia = kategoriRepository.findAll().get(2);

        barangRepository.save(Barang.builder().kategori(alatPembantu).namaBarang("buku").kodeBarang("034").jumlahTotal(23).jumlahTersedia(16).kondisi(KondisiBarang.BAIK).lokasi("Rak 3").harga(new BigDecimal("5000000")).build());
        barangRepository.save(Barang.builder().kategori(alatPembantu).namaBarang("penggaris").kodeBarang("09").jumlahTotal(9).jumlahTersedia(8).kondisi(KondisiBarang.BAIK).lokasi("Rak 5").harga(new BigDecimal("30000000")).build());
        barangRepository.save(Barang.builder().kategori(alatPembantu).namaBarang("Penghapus").kodeBarang("027").jumlahTotal(15).jumlahTersedia(15).kondisi(KondisiBarang.BAIK).lokasi("Rak 1").harga(new BigDecimal("1500000")).build());
        barangRepository.save(Barang.builder().kategori(alatUtama).namaBarang("uranium").kodeBarang("032").jumlahTotal(20).jumlahTersedia(20).kondisi(KondisiBarang.BAIK).lokasi("di bawah bumi").harga(new BigDecimal("100000000000")).build());
        barangRepository.save(Barang.builder().kategori(alatPembantu).namaBarang("atom").kodeBarang("45").jumlahTotal(30).jumlahTersedia(30).kondisi(KondisiBarang.BAIK).lokasi("brankas").harga(new BigDecimal("5000000")).build());
        barangRepository.save(Barang.builder().kategori(alatUtama).namaBarang("daemon core").kodeBarang("384").jumlahTotal(1).jumlahTersedia(0).kondisi(KondisiBarang.RUSAK_RINGAN).lokasi("cernubil").harga(new BigDecimal("13000000000")).build());
        barangRepository.save(Barang.builder().kategori(bahanKimia).namaBarang("NaCl").kodeBarang("4a97").jumlahTotal(1).jumlahTersedia(1).kondisi(KondisiBarang.BAIK).lokasi("rak no.69").harga(new BigDecimal("100000")).build());
    }

    private void seedPeminjaman() {
        User dino = userRepository.findByEmail("dino@gmail.com").orElseThrow();
        User alif = userRepository.findByEmail("alifkhasan7@gmail.com").orElseThrow();
        User ngalimunghofur = userRepository.findByEmail("fs3650835@gmail.com").orElseThrow();
        User lulu = userRepository.findByEmail("lulu@email.com").orElseThrow();
        User indah = userRepository.findByEmail("indah@email.com").orElseThrow();
        User banu = userRepository.findByEmail("banu@bn.com").orElseThrow();
        User dani = userRepository.findByEmail("dani@dn.com").orElseThrow();
        User zaza = userRepository.findByEmail("zaza@gmail.com").orElseThrow();
        User deni = userRepository.findByEmail("deni@dn.com").orElseThrow();

        peminjamanRepository.save(Peminjaman.builder().peminjam(dino).petugas(alif).tglPinjam(LocalDate.parse("2026-06-02")).tglKembali(LocalDate.parse("2026-07-23")).status(StatusPeminjaman.DIKEMBALIKAN).catatan("pinjam dulu seratus").build());
        peminjamanRepository.save(Peminjaman.builder().peminjam(dino).petugas(alif).tglPinjam(LocalDate.parse("2026-06-02")).tglKembali(LocalDate.parse("2026-06-09")).status(StatusPeminjaman.DITOLAK).catatan("wow").build());
        peminjamanRepository.save(Peminjaman.builder().peminjam(dino).petugas(alif).tglPinjam(LocalDate.parse("2026-06-02")).tglKembali(LocalDate.parse("2026-06-09")).status(StatusPeminjaman.DIKEMBALIKAN).catatan("sat").build());
        peminjamanRepository.save(Peminjaman.builder().peminjam(lulu).petugas(alif).tglPinjam(LocalDate.parse("2026-06-02")).tglKembali(LocalDate.parse("2026-06-09")).status(StatusPeminjaman.DISETUJUI).catatan("Pinjem yh min, buku 5 juta nya").build());
        peminjamanRepository.save(Peminjaman.builder().peminjam(indah).petugas(alif).tglPinjam(LocalDate.parse("2026-06-02")).tglKembali(LocalDate.parse("2026-06-09")).status(StatusPeminjaman.DISETUJUI).catatan("pinjam penggaris 30jt nya yh").build());
        peminjamanRepository.save(Peminjaman.builder().peminjam(ngalimunghofur).petugas(alif).tglPinjam(LocalDate.parse("2026-06-02")).tglKembali(LocalDate.parse("2026-06-09")).status(StatusPeminjaman.DISETUJUI).catatan("hai").build());
        peminjamanRepository.save(Peminjaman.builder().peminjam(banu).petugas(dani).tglPinjam(LocalDate.parse("2026-06-06")).tglKembali(LocalDate.parse("2026-06-13")).status(StatusPeminjaman.DIKEMBALIKAN).catatan("ya").build());
        peminjamanRepository.save(Peminjaman.builder().peminjam(zaza).petugas(deni).tglPinjam(LocalDate.parse("2026-06-08")).tglKembali(LocalDate.parse("2026-06-15")).status(StatusPeminjaman.DISETUJUI).catatan("wkwkwkkw").build());
        peminjamanRepository.save(Peminjaman.builder().peminjam(ngalimunghofur).petugas(null).tglPinjam(LocalDate.parse("2026-06-09")).tglKembali(LocalDate.parse("2026-06-16")).status(StatusPeminjaman.MENUNGGU).catatan(null).build());
    }

    private void seedDetailPeminjaman() {
        Barang buku = barangRepository.findByKodeBarang("034").orElseThrow();
        Barang penggaris = barangRepository.findByKodeBarang("09").orElseThrow();
        Barang penghapus = barangRepository.findByKodeBarang("027").orElseThrow();
        Barang daemonCore = barangRepository.findByKodeBarang("384").orElseThrow();

        java.util.List<Peminjaman> allPeminjaman = peminjamanRepository.findAll();
        Peminjaman p1 = allPeminjaman.get(0);
        Peminjaman p2 = allPeminjaman.get(1);
        Peminjaman p3 = allPeminjaman.get(2);
        Peminjaman p4 = allPeminjaman.get(3);
        Peminjaman p5 = allPeminjaman.get(4);
        Peminjaman p6 = allPeminjaman.get(5);
        Peminjaman p7 = allPeminjaman.get(6);
        Peminjaman p8 = allPeminjaman.get(7);
        Peminjaman p9 = allPeminjaman.get(8);

        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p1).barang(buku).jumlah(1).kondisiKembali(KondisiBarang.BAIK).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p2).barang(buku).jumlah(1).kondisiKembali(null).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p3).barang(buku).jumlah(1).kondisiKembali(KondisiBarang.BAIK).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p4).barang(buku).jumlah(1).kondisiKembali(null).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p5).barang(penggaris).jumlah(1).kondisiKembali(null).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p6).barang(buku).jumlah(1).kondisiKembali(null).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p7).barang(buku).jumlah(10).kondisiKembali(null).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p7).barang(penghapus).jumlah(14).kondisiKembali(KondisiBarang.BAIK).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p8).barang(buku).jumlah(5).kondisiKembali(null).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p8).barang(daemonCore).jumlah(1).kondisiKembali(null).build());
        detailPeminjamanRepository.save(DetailPeminjaman.builder().peminjaman(p9).barang(buku).jumlah(1).kondisiKembali(null).build());
    }
}
