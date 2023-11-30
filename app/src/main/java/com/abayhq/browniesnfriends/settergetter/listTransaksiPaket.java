package com.abayhq.browniesnfriends.settergetter;

public class listTransaksiPaket {
    String identitasPaket, idPaket;
    int qty, total;

    public listTransaksiPaket(String identitasPaket, String idPaket, int qty, int total) {
        this.identitasPaket = identitasPaket;
        this.idPaket = idPaket;
        this.qty = qty;
        this.total = total;
    }

    public String getIdentitasPaket() {
        return identitasPaket;
    }

    public void setIdentitasPaket(String identitasPaket) {
        this.identitasPaket = identitasPaket;
    }

    public String getIdPaket() {
        return idPaket;
    }

    public void setIdPaket(String idPaket) {
        this.idPaket = idPaket;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
