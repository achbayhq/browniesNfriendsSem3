package com.abayhq.browniesnfriends.settergetter;

public class listTransaksiDetailPaket {
    String id_paket, id_barang;
    int qty;

    public listTransaksiDetailPaket(String id_paket, String id_barang, int qty) {
        this.id_paket = id_paket;
        this.id_barang = id_barang;
        this.qty = qty;
    }

    public String getId_paket() {
        return id_paket;
    }

    public void setId_paket(String id_paket) {
        this.id_paket = id_paket;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
