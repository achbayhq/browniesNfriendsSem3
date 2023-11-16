package com.abayhq.browniesnfriends.settergetter;

public class setgetRincianBeli {
    String nama, harga, img;
    Integer qty;

    public setgetRincianBeli(String img, String nama, String harga, Integer qty){
        this.nama = nama;
        this.harga = harga;
        this.qty = qty;
        this.img = img;
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
}
