package com.hbv2.icelandevents.Entities;

/**
 * Created by Martin on 5.2.2017.
 */

public class UserInfo {


    private static String username;
    private static String password;
    private static boolean login;


    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserInfo.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserInfo.password = password;
    }

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        UserInfo.login = login;
    }
}
