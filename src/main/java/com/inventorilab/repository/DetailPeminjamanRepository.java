package com.inventorilab.repository;

import com.inventorilab.entity.DetailPeminjaman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailPeminjamanRepository extends JpaRepository<DetailPeminjaman, Long> {
}
