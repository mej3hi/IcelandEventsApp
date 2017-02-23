package com.hbv2.icelandevents.Entities;

/**
 * Created by Martin on 22.2.2017.
 */

public class User {

    private Long id;
    private String username;
    private String password;
    private String passwordConfirm;
    private String resetpasswordtoken;
    private String resetpasswordexpires;
    private String name;
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getResetpasswordtoken() {
        return resetpasswordtoken;
    }

    public void setResetpasswordtoken(String resetpasswordtoken) {
        this.resetpasswordtoken = resetpasswordtoken;
    }

    public String getResetpasswordexpires() {
        return resetpasswordexpires;
    }

    public void setResetpasswordexpires(String resetpasswordexpires) {
        this.resetpasswordexpires = resetpasswordexpires;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
