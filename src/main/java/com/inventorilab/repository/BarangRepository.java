package com.inventorilab.repository;

import com.inventorilab.entity.Barang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BarangRepository extends JpaRepository<Barang, Long> {
    Optional<Barang> findByKodeBarang(String kodeBarang);
    boolean existsByKodeBarang(String kodeBarang);
    boolean existsByKodeBarangAndIdNot(String kodeBarang, Long id);
    List<Barang> findByHargaGreaterThanAndJumlahTersediaGreaterThan(BigDecimal harga, Integer jumlahTersedia);
}
