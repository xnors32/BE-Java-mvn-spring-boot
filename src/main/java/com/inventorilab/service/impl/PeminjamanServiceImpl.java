package com.inventorilab.service.impl;

import com.inventorilab.dto.request.DetailPeminjamanRequest;
import com.inventorilab.dto.request.PeminjamanRequest;
import com.inventorilab.dto.request.PengembalianDetailRequest;
import com.inventorilab.dto.request.PengembalianRequest;
import com.inventorilab.dto.response.PeminjamanResponse;
import com.inventorilab.entity.Barang;
import com.inventorilab.entity.DetailPeminjaman;
import com.inventorilab.entity.Peminjaman;
import com.inventorilab.entity.User;
import com.inventorilab.enums.KondisiBarang;
import com.inventorilab.enums.Role;
import com.inventorilab.enums.StatusPeminjaman;
import com.inventorilab.exception.BadRequestException;
import com.inventorilab.exception.ResourceNotFoundException;
import com.inventorilab.mapper.PeminjamanMapper;
import com.inventorilab.repository.BarangRepository;
import com.inventorilab.repository.DetailPeminjamanRepository;
import com.inventorilab.repository.PeminjamanRepository;
import com.inventorilab.repository.UserRepository;
import com.inventorilab.service.PusherService;
import com.inventorilab.service.interfaces.PeminjamanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PeminjamanServiceImpl implements PeminjamanService {

    private final PeminjamanRepository peminjamanRepository;
    private final DetailPeminjamanRepository detailPeminjamanRepository;
    private final UserRepository userRepository;
    private final BarangRepository barangRepository;
    private final PusherService pusherService;

    @Override
    @Transactional
    public PeminjamanResponse create(PeminjamanRequest request, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan!"));

        if (request.getTglKembali().isBefore(request.getTglPinjam())) {
            throw new BadRequestException("Tanggal kembali tidak boleh sebelum tanggal pinjam!");
        }

        // Build Peminjaman
        Peminjaman peminjaman = Peminjaman.builder()
                .peminjam(user)
                .tglPinjam(request.getTglPinjam())
                .tglKembali(request.getTglKembali())
                .status(StatusPeminjaman.MENUNGGU)
                .catatan(request.getCatatan())
                .details(new ArrayList<>())
                .build();

        // Process details
        for (DetailPeminjamanRequest detailReq : request.getDetails()) {
            Barang barang = barangRepository.findById(detailReq.getIdBarang())
                    .orElseThrow(() -> new ResourceNotFoundException("Barang dengan ID " + detailReq.getIdBarang() + " tidak ditemukan!"));

            // Validate available stock before requesting
            if (detailReq.getJumlah() > barang.getJumlahTersedia()) {
                throw new BadRequestException("Jumlah pinjam barang '" + barang.getNamaBarang() + "' (" + detailReq.getJumlah() +
                        ") melebihi stok tersedia (" + barang.getJumlahTersedia() + ")!");
            }

            DetailPeminjaman detail = DetailPeminjaman.builder()
                    .peminjaman(peminjaman)
                    .barang(barang)
                    .jumlah(detailReq.getJumlah())
                    .build();

            peminjaman.getDetails().add(detail);
        }

        Peminjaman savedPeminjaman = peminjamanRepository.save(peminjaman);

        pusherService.trigger(
                "notifications",
                "peminjaman-new",
                Map.of(
                        "peminjamanId", savedPeminjaman.getId(),
                        "message", "Peminjaman baru #" + savedPeminjaman.getId() + " dari " + user.getNama()
                )
        );

        return PeminjamanMapper.toResponse(savedPeminjaman);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PeminjamanResponse> getAll(Pageable pageable, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan!"));

        Page<Peminjaman> peminjamanPage;
        if (user.getRole() == Role.MAHASISWA) {
            // Mahasiswa only gets their own loans
            peminjamanPage = peminjamanRepository.findByPeminjam(user, pageable);
        } else {
            // Admin and Petugas get all loans
            peminjamanPage = peminjamanRepository.findAll(pageable);
        }

        return peminjamanPage.map(PeminjamanMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public PeminjamanResponse getById(Long id, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan!"));

        Peminjaman peminjaman = peminjamanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaksi peminjaman tidak ditemukan!"));

        if (user.getRole() == Role.MAHASISWA && !peminjaman.getPeminjam().getId().equals(user.getId())) {
            throw new BadRequestException("Anda tidak berhak melihat transaksi ini!");
        }

        return PeminjamanMapper.toResponse(peminjaman);
    }

    @Override
    @Transactional
    public PeminjamanResponse approve(Long id, String petugasEmail) {
        User petugas = userRepository.findByEmail(petugasEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Petugas tidak ditemukan!"));

        Peminjaman peminjaman = peminjamanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaksi peminjaman dengan ID " + id + " tidak ditemukan!"));

        if (peminjaman.getStatus() != StatusPeminjaman.MENUNGGU) {
            throw new BadRequestException("Transaksi peminjaman tidak dapat disetujui karena berstatus: " + peminjaman.getStatus());
        }

        // Deduct stocks
        for (DetailPeminjaman detail : peminjaman.getDetails()) {
            Barang barang = detail.getBarang();
            if (detail.getJumlah() > barang.getJumlahTersedia()) {
                throw new BadRequestException("Stok tidak mencukupi untuk barang '" + barang.getNamaBarang() + "'! " +
                        "(Dibutuhkan: " + detail.getJumlah() + ", Tersedia: " + barang.getJumlahTersedia() + ")");
            }
            barang.setJumlahTersedia(barang.getJumlahTersedia() - detail.getJumlah());
            barangRepository.save(barang);
        }

        peminjaman.setStatus(StatusPeminjaman.DISETUJUI);
        peminjaman.setPetugas(petugas);

        Peminjaman updatedPeminjaman = peminjamanRepository.save(peminjaman);

        pusherService.trigger(
                "notifications",
                "peminjaman-updated",
                Map.of(
                        "peminjamanId", updatedPeminjaman.getId(),
                        "status", updatedPeminjaman.getStatus().name(),
                        "message", "Peminjaman #" + updatedPeminjaman.getId() + " telah disetujui",
                        "userId", updatedPeminjaman.getPeminjam().getId()
                )
        );

        return PeminjamanMapper.toResponse(updatedPeminjaman);
    }

    @Override
    @Transactional
    public PeminjamanResponse reject(Long id, String petugasEmail) {
        User petugas = userRepository.findByEmail(petugasEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Petugas tidak ditemukan!"));

        Peminjaman peminjaman = peminjamanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaksi peminjaman dengan ID " + id + " tidak ditemukan!"));

        if (peminjaman.getStatus() != StatusPeminjaman.MENUNGGU) {
            throw new BadRequestException("Transaksi peminjaman tidak dapat ditolak karena berstatus: " + peminjaman.getStatus());
        }

        peminjaman.setStatus(StatusPeminjaman.DITOLAK);
        peminjaman.setPetugas(petugas);

        Peminjaman updatedPeminjaman = peminjamanRepository.save(peminjaman);

        pusherService.trigger(
                "notifications",
                "peminjaman-updated",
                Map.of(
                        "peminjamanId", updatedPeminjaman.getId(),
                        "status", updatedPeminjaman.getStatus().name(),
                        "message", "Peminjaman #" + updatedPeminjaman.getId() + " telah ditolak",
                        "userId", updatedPeminjaman.getPeminjam().getId()
                )
        );

        return PeminjamanMapper.toResponse(updatedPeminjaman);
    }

    @Override
    @Transactional
    public PeminjamanResponse kembalikan(Long id, PengembalianRequest request, String petugasEmail) {
        User petugas = userRepository.findByEmail(petugasEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Petugas tidak ditemukan!"));

        Peminjaman peminjaman = peminjamanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaksi peminjaman dengan ID " + id + " tidak ditemukan!"));

        if (peminjaman.getStatus() != StatusPeminjaman.DISETUJUI) {
            throw new BadRequestException("Transaksi peminjaman tidak dapat dikembalikan karena berstatus: " + peminjaman.getStatus());
        }

        // Map conditions from request payload
        Map<Long, KondisiBarang> kondisiMap = new HashMap<>();
        if (request != null && request.getDetails() != null) {
            for (PengembalianDetailRequest detailReq : request.getDetails()) {
                kondisiMap.put(detailReq.getIdDetail(), detailReq.getKondisiKembali());
            }
        }

        // Restore stocks & save conditions
        for (DetailPeminjaman detail : peminjaman.getDetails()) {
            Barang barang = detail.getBarang();
            barang.setJumlahTersedia(barang.getJumlahTersedia() + detail.getJumlah());
            barangRepository.save(barang);

            // Assign return condition
            KondisiBarang kondisi = kondisiMap.getOrDefault(detail.getId(), KondisiBarang.BAIK);
            detail.setKondisiKembali(kondisi);
            detailPeminjamanRepository.save(detail);
        }

        peminjaman.setStatus(StatusPeminjaman.DIKEMBALIKAN);
        peminjaman.setPetugas(petugas);

        Peminjaman updatedPeminjaman = peminjamanRepository.save(peminjaman);

        pusherService.trigger(
                "notifications",
                "peminjaman-updated",
                Map.of(
                        "peminjamanId", updatedPeminjaman.getId(),
                        "status", updatedPeminjaman.getStatus().name(),
                        "message", "Peminjaman #" + updatedPeminjaman.getId() + " telah dikembalikan",
                        "userId", updatedPeminjaman.getPeminjam().getId()
                )
        );

        return PeminjamanMapper.toResponse(updatedPeminjaman);
    }
}
