package com.chinmay.pojo;

public final class LoginData {
    public LoginData() {
    }

    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public LoginData setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginData setPassword(String password) {
        this.password = password;
        return this;
    }
}
