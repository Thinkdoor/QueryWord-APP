package com.example.administrator.queryword.model;

/**
 * Created by Administrator on 2019/7/25.
 */

public class Basic {
    String [] explains;

    public String[] getExplains() {
        return explains;
    }

    public String getStrings(String [] strings){
        String str = "";
        for (String s:strings
             ) {
            str += s+"\n";
        }
        return str;
    }
}
