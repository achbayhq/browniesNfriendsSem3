package com.abayhq.browniesnfriends.respons;

import com.abayhq.browniesnfriends.settergetter.setgetPesananAdmin;

import java.util.List;

public class pesananAdminRespons {
    private int code;
    private  String status;
    private List<setgetPesananAdmin> transaksi_list;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<setgetPesananAdmin> getTransaksi_list() {
        return transaksi_list;
    }
}
