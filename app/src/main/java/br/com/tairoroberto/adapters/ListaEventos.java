package br.com.tairoroberto.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import br.com.tairoroberto.qualimsolucoes.Despesas;
import br.com.tairoroberto.qualimsolucoes.Evento;

public class ListaEventos implements Parcelable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4123640736L;
	public List<Evento> eventos;
	public static final String key = "eventos";

	public ListaEventos(List<Evento> eventos) {
		// TODO Auto-generated constructor stub
		this.eventos = eventos;
	}

    public ListaEventos(Parcel parcel) {
        this.eventos =  new ArrayList<Evento>();
        parcel.readList(this.eventos,Evento.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(eventos);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<ListaEventos> CREATOR = new Creator<ListaEventos>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public ListaEventos createFromParcel(Parcel source) {
            return new ListaEventos(source);
        }

        @Override
        public ListaEventos[] newArray(int size) {
            return new ListaEventos[size];
        }
    };
}
