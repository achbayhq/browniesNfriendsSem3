package com.abayhq.browniesnfriends.settergetter;

public class setgetRincianBeli {
    String nama, harga, img, jenis;
    Integer qty;

    public setgetRincianBeli(String img, String nama, String harga, Integer qty, String jenis){
        this.nama = nama;
        this.harga = harga;
        this.qty = qty;
        this.img = img;
        this.jenis = jenis;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
}
