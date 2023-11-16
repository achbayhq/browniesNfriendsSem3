package com.abayhq.browniesnfriends.settergetter;

public class listTransaksiBarang {
    String id;
    int qty, total;

    public listTransaksiBarang(String id, int qty, int total) {
        this.id = id;
        this.qty = qty;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
