package com.nduowang.ymind.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {

    private static ResourceBundle resourceBundle;
    static {

        resourceBundle = ResourceBundle.getBundle("lang/lang", Locale.US);
}

    public static String get(String key){
        return resourceBundle.getString(key);

    }
}
