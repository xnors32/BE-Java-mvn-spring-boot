package com.inventorilab.seeder;

import com.inventorilab.entity.Barang;
import com.inventorilab.entity.Kategori;
import com.inventorilab.entity.User;
import com.inventorilab.enums.KondisiBarang;
import com.inventorilab.enums.Role;
import com.inventorilab.repository.BarangRepository;
import com.inventorilab.repository.KategoriRepository;
import com.inventorilab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final KategoriRepository kategoriRepository;
    private final BarangRepository barangRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedInventory();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .nama("Admin Lab Utama")
                    .email("admin@lab.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();

            User petugas = User.builder()
                    .nama("Petugas Lab Satu")
                    .email("petugas@lab.com")
                    .password(passwordEncoder.encode("petugas123"))
                    .role(Role.PETUGAS)
                    .build();

            User mahasiswa = User.builder()
                    .nama("Mahasiswa Teknik")
                    .email("mahasiswa@lab.com")
                    .password(passwordEncoder.encode("mahasiswa123"))
                    .role(Role.MAHASISWA)
                    .build();

            userRepository.saveAll(Arrays.asList(admin, petugas, mahasiswa));
            System.out.println(">>> DatabaseSeeder: Berhasil menanamkan data pengguna awal (Admin, Petugas, Mahasiswa).");
        }
    }

    private void seedInventory() {
        if (kategoriRepository.count() == 0 && barangRepository.count() == 0) {
            Kategori elektronik = Kategori.builder()
                    .namaKategori("Alat Elektronik")
                    .deskripsi("Peralatan elektronik laboratorium termasuk osiloskop, catu daya, dsb.")
                    .build();

            Kategori alatGelas = Kategori.builder()
                    .namaKategori("Alat Gelas")
                    .deskripsi("Peralatan glassware laboratorium kimia dasar.")
                    .build();

            kategoriRepository.saveAll(Arrays.asList(elektronik, alatGelas));

            Barang osc = Barang.builder()
                    .kategori(elektronik)
                    .namaBarang("Oscilloscope Digital")
                    .kodeBarang("EL-OSC-001")
                    .jumlahTotal(5)
                    .jumlahTersedia(5)
                    .kondisi(KondisiBarang.BAIK)
                    .lokasi("Lemari A1")
                    .build();

            Barang multimeter = Barang.builder()
                    .kategori(elektronik)
                    .namaBarang("Digital Multimeter")
                    .kodeBarang("EL-DMM-002")
                    .jumlahTotal(10)
                    .jumlahTersedia(10)
                    .kondisi(KondisiBarang.BAIK)
                    .lokasi("Lemari A2")
                    .build();

            Barang beaker = Barang.builder()
                    .kategori(alatGelas)
                    .namaBarang("Beaker Glass 250ml")
                    .kodeBarang("GL-BKR-003")
                    .jumlahTotal(25)
                    .jumlahTersedia(25)
                    .kondisi(KondisiBarang.BAIK)
                    .lokasi("Rak B1")
                    .build();

            barangRepository.saveAll(Arrays.asList(osc, multimeter, beaker));
            System.out.println(">>> DatabaseSeeder: Berhasil menanamkan data kategori dan barang awal.");
        }
    }
}
