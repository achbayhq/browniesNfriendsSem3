package com.abayhq.browniesnfriends.respons;

import com.abayhq.browniesnfriends.settergetter.listPaketNota;

import java.util.List;

public class paketNotaRespons {
    private Integer code;
    private String status;
    private List<listPaketNota> nota_list;

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<listPaketNota> getNota_list() {
        return nota_list;
    }
}
