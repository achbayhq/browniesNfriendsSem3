package com.abayhq.browniesnfriends.settergetter;

public class setgetPesanan {

    String tanggal_pengambilan, status, grand_total, nota;
    int imgStatus;

    public setgetPesanan(String tanggal_pengambilan, String status, int imgStatus, String grand_total, String nota) {
        this.tanggal_pengambilan = tanggal_pengambilan;
        this.status = status;
        this.grand_total = grand_total;
        this.imgStatus = imgStatus;
        this.nota = nota;
    }

    public String getTanggal_pengambilan() {
        return tanggal_pengambilan;
    }

    public void setTanggal_pengambilan(String tanggal_pengambilan) {
        this.tanggal_pengambilan = tanggal_pengambilan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public int getImgStatus() {
        return imgStatus;
    }

    public void setImgStatus(int imgStatus) {
        this.imgStatus = imgStatus;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
