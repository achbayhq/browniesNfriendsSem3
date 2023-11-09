package com.abayhq.browniesnfriends.settergetter;

public class setgetPesanan {

    String tgl, status, harga;
    int imgStatus;

    public setgetPesanan(String tgl, String status, int imgStatus, String harga) {
        this.tgl = tgl;
        this.status = status;
        this.harga = harga;
        this.imgStatus = imgStatus;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public int getImgStatus() {
        return imgStatus;
    }

    public void setImgStatus(int imgStatus) {
        this.imgStatus = imgStatus;
    }
}
