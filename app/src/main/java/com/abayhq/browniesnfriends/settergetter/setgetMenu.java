package com.abayhq.browniesnfriends.settergetter;

public class setgetMenu {
    String id, nama, deskripsi, harga, img;
    Boolean visibility;
    Integer qty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public setgetMenu(String img, String id, String nama, String des, String harga){
        this.id = id;
        this.nama = nama;
        this.deskripsi = des;
        this.harga = harga;
        this.qty = 0;
        this.img = img;
        this.visibility = false;
    }
}
