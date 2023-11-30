package com.abayhq.browniesnfriends.respons;

import com.abayhq.browniesnfriends.settergetter.dataPaket;

import java.util.List;

public class dataPaketRespons {
    private int code;
    private  String status;
    private List<dataPaket> paket_list;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<dataPaket> getPaket_list() {
        return paket_list;
    }
}
