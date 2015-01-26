package br.com.tairoroberto.qualimsolucoes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tairo on 25/01/15.
 */
public class Evento implements Parcelable{
    private long id;
    private String title;
    private String description;
    private String location;
    private String start;
    private String end;
    private long nutricionista_id;

    public Evento() {
    }

    public Evento(long id, String title, String description, String location,
                    String start, String end, long nutricionista_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.nutricionista_id = nutricionista_id;
    }

    public Evento(Parcel parcel) {
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.description = parcel.readString();
        this.location = parcel.readString();
        this.start = parcel.readString();
        this.end = parcel.readString();
        this.nutricionista_id = parcel.readLong();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public long getNutricionista_id() {
        return nutricionista_id;
    }

    public void setNutricionista_id(long nutricionista_id) {
        this.nutricionista_id = nutricionista_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeLong(nutricionista_id);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<Evento> CREATOR = new Creator<Evento>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public Evento createFromParcel(Parcel source) {
            return new Evento(source);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };
}
