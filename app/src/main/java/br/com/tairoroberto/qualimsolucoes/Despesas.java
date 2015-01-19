package br.com.tairoroberto.qualimsolucoes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by tairo on 18/01/15.
 */
public class Despesas implements Parcelable {
    private long id;
    private String client_locale;
    private String entry_time;
    private String departure_time;
    private String meal_voucher;
    private String observation_transport;
    private String transport_voucher;
    private String observation_extra_expense;
    private String extra_expense;
    private long nutricionista_id;
    private String created_at;
    private String updated_at;

    public Despesas() {
    }

    public Despesas(long id, String client_locale, String entry_time,
                    String departure_time, String meal_voucher,
                    String observation_transport, String transport_voucher,
                    String observation_extra_expense, String extra_expense,
                    long nutricionista_id, String created_at, String updated_at) {
        this.id = id;
        this.client_locale = client_locale;
        this.entry_time = entry_time;
        this.departure_time = departure_time;
        this.meal_voucher = meal_voucher;
        this.observation_transport = observation_transport;
        this.transport_voucher = transport_voucher;
        this.observation_extra_expense = observation_extra_expense;
        this.extra_expense = extra_expense;
        this.nutricionista_id = nutricionista_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Despesas(Parcel parcel) {
        this.id = parcel.readLong();
        this.client_locale = parcel.readString();
        this.entry_time = parcel.readString();
        this.departure_time = parcel.readString();
        this.meal_voucher = parcel.readString();
        this.observation_transport = parcel.readString();
        this.transport_voucher = parcel.readString();
        this.observation_extra_expense = parcel.readString();
        this.extra_expense = parcel.readString();
        this.nutricionista_id = parcel.readLong();
        this.created_at = parcel.readString();
        this.updated_at = parcel.readString();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClient_locale() {
        return client_locale;
    }

    public void setClient_locale(String client_locale) {
        this.client_locale = client_locale;
    }

    public String getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(String entry_time) {
        this.entry_time = entry_time;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getMeal_voucher() {
        return meal_voucher;
    }

    public void setMeal_voucher(String meal_voucher) {
        this.meal_voucher = meal_voucher;
    }

    public String getObservation_transport() {
        return observation_transport;
    }

    public void setObservation_transport(String observation_transport) {
        this.observation_transport = observation_transport;
    }

    public String getTransport_voucher() {
        return transport_voucher;
    }

    public void setTransport_voucher(String transport_voucher) {
        this.transport_voucher = transport_voucher;
    }

    public String getObservation_extra_expense() {
        return observation_extra_expense;
    }

    public void setObservation_extra_expense(String observation_extra_expense) {
        this.observation_extra_expense = observation_extra_expense;
    }

    public String getExtra_expense() {
        return extra_expense;
    }

    public void setExtra_expense(String extra_expense) {
        this.extra_expense = extra_expense;
    }

    public long getNutricionista_id() {
        return nutricionista_id;
    }

    public void setNutricionista_id(long nutricionista_id) {
        this.nutricionista_id = nutricionista_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(client_locale);
        dest.writeString(entry_time);
        dest.writeString(departure_time);
        dest.writeString(meal_voucher);
        dest.writeString(observation_transport);
        dest.writeString(transport_voucher);
        dest.writeString(observation_extra_expense);
        dest.writeString(extra_expense);
        dest.writeLong(nutricionista_id);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<Despesas> CREATOR = new Creator<Despesas>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public Despesas createFromParcel(Parcel source) {
            return new Despesas(source);
        }

        @Override
        public Despesas[] newArray(int size) {
            return new Despesas[size];
        }
    };
}
