package com.abayhq.browniesnfriends.respons;

import com.abayhq.browniesnfriends.settergetter.listBarangNota;

import java.util.List;

public class barangNotaRespons {
    private Integer code;
    private String status;
    private List<listBarangNota> nota_list;

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<listBarangNota> getNota_list() {
        return nota_list;
    }
}
