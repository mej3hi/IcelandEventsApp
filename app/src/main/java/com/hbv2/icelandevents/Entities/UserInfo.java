package com.hbv2.icelandevents.Entities;

/**
 * Created by Martin on 5.2.2017.
 */

public class UserInfo {


    private String username;
    private String password;
    public static boolean login;
    private static String loginUsername;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        UserInfo.login = login;
    }

    public static String getLoginUsername() {
        return loginUsername;
    }

    public static void setLoginUsername(String loginUsername) {
        UserInfo.loginUsername = loginUsername;
    }
}
