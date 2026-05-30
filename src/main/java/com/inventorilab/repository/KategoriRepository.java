package com.inventorilab.repository;

import com.inventorilab.entity.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KategoriRepository extends JpaRepository<Kategori, Long> {
    boolean existsByNamaKategori(String namaKategori);
    boolean existsByNamaKategoriAndIdNot(String namaKategori, Long id);
}
