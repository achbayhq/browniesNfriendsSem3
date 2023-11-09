package com.abayhq.browniesnfriends.respons;

import com.abayhq.browniesnfriends.settergetter.dataUserLogin;

import java.util.List;

public class userLoginRespons {
    private int code;
    private  String status;
    private List<dataUserLogin> user_list;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<dataUserLogin> getUser_list() {
        return user_list;
    }
}
