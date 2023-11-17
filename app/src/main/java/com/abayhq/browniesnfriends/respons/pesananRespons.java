package com.abayhq.browniesnfriends.respons;

import com.abayhq.browniesnfriends.settergetter.setgetPesanan;

import java.util.List;

public class pesananRespons {
    private int code;
    private  String status;
    private List<setgetPesanan> transaksi_list;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<setgetPesanan> getTransaksi_list() {
        return transaksi_list;
    }
}
