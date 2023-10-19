package com.abayhq.browniesnfriends.settergetter;

public class setgetMenu {
    String nama, deskripsi, harga;
    Boolean visibility;
    Integer qty, img;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public setgetMenu(Integer img, String nama, String des, String harga){
        this.nama = nama;
        this.deskripsi = des;
        this.harga = harga;
        this.qty = 0;
        this.img = img;
        this.visibility = false;
    }
}
