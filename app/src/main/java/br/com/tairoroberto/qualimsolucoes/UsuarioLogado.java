package br.com.tairoroberto.qualimsolucoes;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tairo on 16/01/15.
 */
public class UsuarioLogado implements Parcelable {

    private long id;
    private String name;
    private String email;

    public UsuarioLogado() {

    }

    public UsuarioLogado(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UsuarioLogado(Parcel parcel) {
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.email = parcel.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<UsuarioLogado> CREATOR = new Creator<UsuarioLogado>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public UsuarioLogado createFromParcel(Parcel source) {
            return new UsuarioLogado(source);
        }

        @Override
        public UsuarioLogado[] newArray(int size) {
            return new UsuarioLogado[size];
        }
    };
}
