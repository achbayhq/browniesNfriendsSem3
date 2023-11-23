package com.abayhq.browniesnfriends.settergetter;

public class listBarangNota {
    String nama_barang;
    String qty;
    String harga_jual;
    String total;
    String id_barang;
    String image_barang;
    String deskripsi;

    public listBarangNota(String nama_barang, String qty, String harga_jual, String total) {
        this.nama_barang = nama_barang;
        this.qty = qty;
        this.harga_jual = harga_jual;
        this.total = total;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public void setHarga_jual(String harga_jual) {
        this.harga_jual = harga_jual;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getImage_barang() {
        return image_barang;
    }

    public void setImage_barang(String image_barang) {
        this.image_barang = image_barang;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
