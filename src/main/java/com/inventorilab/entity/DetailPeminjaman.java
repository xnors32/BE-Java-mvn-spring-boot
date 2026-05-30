package com.inventorilab.entity;

import com.inventorilab.enums.KondisiBarang;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detail_peminjaman")
public class DetailPeminjaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_peminjaman", nullable = false)
    private Peminjaman peminjaman;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_barang", nullable = false)
    private Barang barang;

    @Column(nullable = false)
    private Integer jumlah;

    @Enumerated(EnumType.STRING)
    @Column(name = "kondisi_kembali")
    private KondisiBarang kondisiKembali;
}
