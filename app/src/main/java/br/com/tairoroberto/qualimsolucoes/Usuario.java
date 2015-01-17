package br.com.tairoroberto.qualimsolucoes;

import java.io.Serializable;

/**
 * Created by tairo on 16/01/15.
 */
public class Usuario implements Serializable{

    private long id;
    private String name;
    private String email;
    private String password;
    private boolean remember;
/*    private String password_confirmation;
    private String address;
    private String number;
    private String district;
    private String city;
    private String postal_code;
    private String telephone;
    private String celphone;
    private String email;
    private String type;
    private int num_ticket;
    private String photo;
    private String signature;
    private String remember_token;*/

    public Usuario() {

    }

    public Usuario(long id, String name, String email, String password, boolean remember) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.remember = remember;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public boolean getRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
