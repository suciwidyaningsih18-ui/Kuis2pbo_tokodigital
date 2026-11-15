package kuis2pbo;


public class ProdukPremium extends Produk {
    private static final long serialVersionUID = 1L;

    public ProdukPremium(String kode, String nama, int stok) {
        super(kode, nama, stok);
    }

    @Override
    public double hargaSatuan() { return 15000000; }
}