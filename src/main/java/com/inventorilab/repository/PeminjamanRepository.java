package com.inventorilab.repository;

import com.inventorilab.entity.Peminjaman;
import com.inventorilab.entity.User;
import com.inventorilab.enums.StatusPeminjaman;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeminjamanRepository extends JpaRepository<Peminjaman, Long> {
    Page<Peminjaman> findByPeminjam(User peminjam, Pageable pageable);
    long countByStatus(StatusPeminjaman status);
    boolean existsByPeminjam(User peminjam);
    boolean existsByPetugas(User petugas);
}
