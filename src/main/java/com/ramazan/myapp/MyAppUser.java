package com.ramazan.myapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MyAppUser {

    private Integer id;
    private String name;
    private String email;
    private String password;

    public MyAppUser(){}

    public MyAppUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public MyAppUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public MyAppUser(Integer id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void encodePassword() {
        setPassword(new BCryptPasswordEncoder().encode(getPassword()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
