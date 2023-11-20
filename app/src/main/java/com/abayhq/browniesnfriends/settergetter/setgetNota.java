package com.abayhq.browniesnfriends.settergetter;

public class setgetNota {
    String nama, harga;
    Integer qty, total;

    public setgetNota(String nama, String harga, Integer qty, Integer total) {
        this.nama = nama;
        this.harga = harga;
        this.qty = qty;
        this.total = total;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
