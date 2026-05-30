package com.inventorilab.entity;

import com.inventorilab.enums.StatusPeminjaman;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "peminjaman")
public class Peminjaman {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_peminjam", nullable = false)
    private User peminjam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_petugas")
    private User petugas;

    @Column(name = "tgl_pinjam", nullable = false)
    private LocalDate tglPinjam;

    @Column(name = "tgl_kembali", nullable = false)
    private LocalDate tglKembali;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPeminjaman status;

    @Column(columnDefinition = "TEXT")
    private String catatan;

    @Builder.Default
    @OneToMany(mappedBy = "peminjaman", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailPeminjaman> details = new ArrayList<>();
}
