package kuis2pbo;


public class ProdukReguler extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukReguler(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public double hargaSatuan() { return 150000; }
}