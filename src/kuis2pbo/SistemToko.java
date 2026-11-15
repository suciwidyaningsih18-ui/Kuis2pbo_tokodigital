package kuis2pbo;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SistemToko implements Serializable {
    private static final long serialVersionUID = 1L;

    private String namaToko;
    private HashMap<String, Pelanggan> daftarPelanggan = new LinkedHashMap<>();
    private HashMap<String, Produk> daftarProduk = new LinkedHashMap<>();
    private ArrayList<Transaksi> logTransaksi = new ArrayList<>();

    public SistemToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public void tambahPelanggan(String id, String nama) {
        daftarPelanggan.put(id, new Pelanggan(id, nama));
    }

    public void tambahProduk(String kode, String nama, int stok) {
        Produk p;
        if (nama.contains("Premium")) {
            p = new ProdukPremium(kode, nama, stok);
        } else if (nama.contains("Reguler")) {
            p = new ProdukReguler(kode, nama, stok);
        } else {
            p = new ProdukEkonomis(kode, nama, stok);
        }
        daftarProduk.put(kode, p);
    }

    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        int nomor = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                baris = baris.trim();
                if (baris.isEmpty()) continue;
                nomor++;
                try {
                    prosesBaris(baris);
                } catch (FormatDataException | PelangganTidakDitemukanException
                         | ProdukTidakDitemukanException | StokTidakCukupException e) {
                    System.out.println("[GAGAL] Baris " + nomor + ": " + e.getMessage());
                }
            }
        }
    }

    
    private void prosesBaris(String baris) throws FormatDataException,
            PelangganTidakDitemukanException, ProdukTidakDitemukanException,
            StokTidakCukupException {

        if (baris.endsWith(".")) {
            baris = baris.substring(0, baris.length() - 1);
        }
        String[] bagian = baris.split("\\.");
        if (bagian.length != 3) {
            throw new FormatDataException("Format baris tidak valid: " + baris);
        }

        String idPelanggan = ambilNilai(bagian[0], "ID");
        String kodeProduk = ambilNilai(bagian[1], "KD");
        int jumlah;
        try {
            jumlah = Integer.parseInt(ambilNilai(bagian[2], "JML"));
        } catch (NumberFormatException e) {
            throw new FormatDataException("JML bukan angka: " + baris);
        }

        Pelanggan pel = daftarPelanggan.get(idPelanggan);
        if (pel == null) {
            throw new PelangganTidakDitemukanException("Pelanggan " + idPelanggan + " tidak ditemukan");
        }
        Produk prod = daftarProduk.get(kodeProduk);
        if (prod == null) {
            throw new ProdukTidakDitemukanException("Produk " + kodeProduk + " tidak ditemukan");
        }

        prod.kurangiStok(jumlah);
        Transaksi t = new Transaksi(pel, prod, jumlah);
        logTransaksi.add(t);
        System.out.println("[OK] " + pel.getNama() + " membeli " + jumlah + " x "
                + prod.getNama() + " = Rp" + String.format("%,.0f", t.getTotal()));
    }

    private String ambilNilai(String potongan, String kunciDiharapkan) throws FormatDataException {
        String[] kv = potongan.split(">");
        if (kv.length != 2 || !kv[0].equals(kunciDiharapkan)) {
            throw new FormatDataException("Potongan data tidak valid: " + potongan);
        }
        return kv[1];
    }

    public void simpanLaporanAkhir() {
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("sistem_state.ser"))) {
            oos.writeObject(this);
            System.out.println("[INFO] Status sistem disimpan ke sistem_state.ser");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal serialisasi: " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter("laporan.txt"))) {
            pw.println("=== DAFTAR AKTOR AKTIF - " + namaToko + " ===");
            for (Pelanggan p : daftarPelanggan.values()) {
                pw.println("ID: " + p.getId() + " | Nama: " + p.getNama());
            }
            System.out.println("[INFO] Laporan disimpan ke laporan.txt");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menulis laporan: " + e.getMessage());
        }
    }
}