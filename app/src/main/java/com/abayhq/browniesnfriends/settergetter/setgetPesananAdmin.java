package com.abayhq.browniesnfriends.settergetter;

public class setgetPesananAdmin {
    String no_nota;
    String nama;
    String grand_total;

    public setgetPesananAdmin(String no_nota, String nama, String grand_total) {
        this.no_nota = no_nota;
        this.nama = nama;
        this.grand_total = grand_total;
    }

    public String getNo_nota() {
        return no_nota;
    }

    public void setNo_nota(String no_nota) {
        this.no_nota = no_nota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }
}
