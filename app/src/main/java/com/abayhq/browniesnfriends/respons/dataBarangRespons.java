package com.abayhq.browniesnfriends.respons;

import com.abayhq.browniesnfriends.settergetter.dataBarang;

import java.util.List;

public class dataBarangRespons {
    private int code;
    private  String status;
    private List<dataBarang> barang_list;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<dataBarang> getBarang_list() {
        return barang_list;
    }
}
