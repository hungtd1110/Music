package com.example.admin.music.utils;

import com.example.admin.music.config.Data;
import com.example.admin.music.config.Restrofit;

/**
 * Created by admin on 1/23/2019.
 */

public class APIUtils {
//    private static final String URL = "http://192.168.1.6/project/androidservice/public/";
    private static final String URL = "http://androidservice1.000webhostapp.com/";

    public static Data getData() {
        return Restrofit.Config(URL).create(Data.class);
    }
}
