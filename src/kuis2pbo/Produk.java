package kuis2pbo;


import java.io.Serializable;

public abstract class Produk implements Serializable {
    private static final long serialVersionUID = 1L;
    private String kode;
    private String nama;
    private int stok;

    public Produk(String kode, String nama, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.stok = stok;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public int getStok() { return stok; }

    public void kurangiStok(int jumlah) throws StokTidakCukupException {
        if (jumlah > stok) {
            throw new StokTidakCukupException("Stok " + nama + " tidak cukup (sisa "
                    + stok + ", diminta " + jumlah + ")");
        }
        stok -= jumlah;
    }

    public abstract double hargaSatuan();

    public double hitungHarga(int jumlah) {
        return hargaSatuan() * jumlah;
    }
}