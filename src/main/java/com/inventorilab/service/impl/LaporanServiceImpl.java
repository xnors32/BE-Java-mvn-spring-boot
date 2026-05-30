package com.inventorilab.service.impl;

import com.inventorilab.dto.response.LaporanPeminjamanResponse;
import com.inventorilab.dto.response.LaporanStokResponse;
import com.inventorilab.entity.Barang;
import com.inventorilab.enums.KondisiBarang;
import com.inventorilab.enums.StatusPeminjaman;
import com.inventorilab.repository.BarangRepository;
import com.inventorilab.repository.KategoriRepository;
import com.inventorilab.repository.PeminjamanRepository;
import com.inventorilab.service.interfaces.LaporanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LaporanServiceImpl implements LaporanService {

    private final KategoriRepository kategoriRepository;
    private final BarangRepository barangRepository;
    private final PeminjamanRepository peminjamanRepository;

    @Override
    @Transactional(readOnly = true)
    public LaporanStokResponse getStokLaporan() {
        long totalKategori = kategoriRepository.count();
        List<Barang> allBarang = barangRepository.findAll();
        
        long totalBarangUnit = allBarang.size();
        long totalStokSistem = allBarang.stream().mapToLong(Barang::getJumlahTotal).sum();
        long totalStokTersedia = allBarang.stream().mapToLong(Barang::getJumlahTersedia).sum();
        long totalStokDipinjam = totalStokSistem - totalStokTersedia;
        
        long barangBaik = allBarang.stream().filter(b -> b.getKondisi() == KondisiBarang.BAIK).count();
        long barangRusakRingan = allBarang.stream().filter(b -> b.getKondisi() == KondisiBarang.RUSAK_RINGAN).count();
        long barangRusakBerat = allBarang.stream().filter(b -> b.getKondisi() == KondisiBarang.RUSAK_BERAT).count();

        return LaporanStokResponse.builder()
                .totalKategori(totalKategori)
                .totalBarangUnit(totalBarangUnit)
                .totalStokSistem(totalStokSistem)
                .totalStokTersedia(totalStokTersedia)
                .totalStokDipinjam(totalStokDipinjam)
                .barangKondisiBaik(barangBaik)
                .barangKondisiRusakRingan(barangRusakRingan)
                .barangKondisiRusakBerat(barangRusakBerat)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public LaporanPeminjamanResponse getPeminjamanLaporan() {
        long totalPeminjaman = peminjamanRepository.count();
        long menunggu = peminjamanRepository.countByStatus(StatusPeminjaman.MENUNGGU);
        long disetujui = peminjamanRepository.countByStatus(StatusPeminjaman.DISETUJUI);
        long ditolak = peminjamanRepository.countByStatus(StatusPeminjaman.DITOLAK);
        long dikembalikan = peminjamanRepository.countByStatus(StatusPeminjaman.DIKEMBALIKAN);

        return LaporanPeminjamanResponse.builder()
                .totalPeminjaman(totalPeminjaman)
                .statusMenunggu(menunggu)
                .statusDisetujui(disetujui)
                .statusDitolak(ditolak)
                .statusDikembalikan(dikembalikan)
                .build();
    }
}
