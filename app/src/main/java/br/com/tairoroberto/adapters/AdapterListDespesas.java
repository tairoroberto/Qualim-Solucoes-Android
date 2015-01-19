package br.com.tairoroberto.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.tairoroberto.qualimsolucoes.Despesas;
import br.com.tairoroberto.qualimsolucoes.R;

/**
 * Created by tairo on 18/01/15.
 */
public class AdapterListDespesas extends BaseAdapter {

    private Context context;
    private List<Despesas> list;

    public AdapterListDespesas(Context context, List<Despesas> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /************************************************************************************
     * Criação da classe estática ViewHolder que será uma ponte para a lista *
     * Essa classe é usada para dar mehor desempenho a lista *
     *************************** ********************************************************/

    static class ViewHolder {
        TextView txtCliente;
        TextView txtData;
        TextView txtHorarioEntrada;
        TextView txtHorarioSaida;
        TextView txtValeRefeicao;
        TextView txtDescValetransporte;
        TextView txtValorValeTrasporte;
        TextView txtDescGastosExtras;
        TextView txtValorGastosExtras;
        TextView txtNutricionista_id;
        TextView txtCreated_at;
        TextView txtUpdated_at;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder layout = new ViewHolder();
        // instacia do objeto carro
        Despesas despesa = list.get(position);

        if (convertView == null) {
            // infla o layput para ser usado
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_listview_despesas, null);

            // linka a despesa da lista com o layput do xml
            layout.txtCliente = (TextView) convertView.findViewById(R.id.txtCliente);
            layout.txtData = (TextView) convertView.findViewById(R.id.txtData);
            layout.txtHorarioEntrada = (TextView) convertView.findViewById(R.id.txtHorarioEntrada);
            layout.txtHorarioSaida = (TextView) convertView.findViewById(R.id.txtHorarioSaida);
            layout.txtValeRefeicao = (TextView) convertView.findViewById(R.id.txtValerefeicao);
            layout.txtDescValetransporte = (TextView) convertView.findViewById(R.id.txtDescricaoValeTransp);
            layout.txtValorValeTrasporte = (TextView) convertView.findViewById(R.id.txtValorValeTransporte);
            layout.txtDescGastosExtras = (TextView) convertView.findViewById(R.id.txtDescGastosExtras);
            layout.txtValorGastosExtras = (TextView) convertView.findViewById(R.id.txtGastosExtras);

            convertView.setTag(layout);
        }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            String data[] = despesa.getCreated_at().split(" ");
            String dataAux[] = data[0].split("-");

            holder.txtCliente.setText(despesa.getClient_locale());
            holder.txtData.setText(dataAux[2]+"/"+dataAux[1]+"/"+dataAux[0]);
            holder.txtHorarioEntrada.setText(despesa.getEntry_time());
            holder.txtHorarioSaida.setText(despesa.getDeparture_time());
            holder.txtValeRefeicao.setText(despesa.getMeal_voucher());
            holder.txtDescValetransporte.setText(despesa.getObservation_transport());
            holder.txtValorValeTrasporte.setText(despesa.getTransport_voucher());
            holder.txtDescGastosExtras.setText(despesa.getObservation_extra_expense());
            holder.txtValorGastosExtras.setText(despesa.getExtra_expense());

        return convertView;
    }
}
