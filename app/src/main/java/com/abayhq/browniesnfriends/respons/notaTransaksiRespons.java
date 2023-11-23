package com.abayhq.browniesnfriends.respons;

import com.abayhq.browniesnfriends.settergetter.dataNotaTransaksi;

import java.util.List;

public class notaTransaksiRespons {
    private Integer code;
    private String status;
    private List<dataNotaTransaksi> nota_list;

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<dataNotaTransaksi> getNota_list() {
        return nota_list;
    }
}
