package kuis2pbo;


import java.io.Serializable;

public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;
    private Pelanggan pelanggan;
    private Produk produk;
    private int jumlah;
    private double total;

    public Transaksi(Pelanggan pelanggan, Produk produk, int jumlah) {
        this.pelanggan = pelanggan;
        this.produk = produk;
        this.jumlah = jumlah;
        this.total = produk.hitungHarga(jumlah);
    }

    public Pelanggan getPelanggan() { return pelanggan; }
    public Produk getProduk() { return produk; }
    public int getJumlah() { return jumlah; }
    public double getTotal() { return total; }
}