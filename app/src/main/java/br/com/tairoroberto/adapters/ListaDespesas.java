package br.com.tairoroberto.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.tairoroberto.qualimsolucoes.Despesas;

public class ListaDespesas implements Parcelable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4123640836L;
	public List<Despesas> despesas;
	public static final String key = "despesas";

	public ListaDespesas(List<Despesas> despesas) {
		// TODO Auto-generated constructor stub
		this.despesas = despesas;
	}

    public ListaDespesas(Parcel parcel) {
        this.despesas =  new ArrayList<Despesas>();
        parcel.readList(this.despesas,Despesas.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(despesas);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<ListaDespesas> CREATOR = new Creator<ListaDespesas>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public ListaDespesas createFromParcel(Parcel source) {
            return new ListaDespesas(source);
        }

        @Override
        public ListaDespesas[] newArray(int size) {
            return new ListaDespesas[size];
        }
    };
}
