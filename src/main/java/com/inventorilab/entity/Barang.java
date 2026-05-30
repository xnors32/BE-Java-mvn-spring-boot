package com.inventorilab.entity;

import com.inventorilab.enums.KondisiBarang;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "barang")
public class Barang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kategori", nullable = false)
    private Kategori kategori;

    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @Column(name = "kode_barang", nullable = false, unique = true)
    private String kodeBarang;

    @Column(name = "jumlah_total", nullable = false)
    private Integer jumlahTotal;

    @Column(name = "jumlah_tersedia", nullable = false)
    private Integer jumlahTersedia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KondisiBarang kondisi;

    @Column(nullable = false)
    private String lokasi;

    @Column(name = "harga", nullable = false, precision = 15, scale = 2)
    private BigDecimal harga;
}
