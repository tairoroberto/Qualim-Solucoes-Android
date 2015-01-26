package br.com.tairoroberto.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.tairoroberto.qualimsolucoes.Despesas;
import br.com.tairoroberto.qualimsolucoes.Evento;
import br.com.tairoroberto.qualimsolucoes.R;

/**
 * Created by tairo on 18/01/15.
 */
public class AdapterListEventos extends BaseAdapter {

    private Context context;
    private List<Evento> list;

    public AdapterListEventos(Context context, List<Evento> list) {
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
        TextView txtTitulo;
        TextView txtDataEvento;
        TextView txtDescricao;
        TextView txtLocal;
        TextView txtHoraEntradaEvento;
        TextView txtHoraSaidaEvento;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder layout = new ViewHolder();
        // instacia do objeto carro
        Evento evento = list.get(position);

        if (convertView == null) {
            // infla o layput para ser usado
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_listview_eventos, null);

            // linka a despesa da lista com o layput do xml
            layout.txtTitulo = (TextView) convertView.findViewById(R.id.txtTitulo);
            layout.txtDataEvento = (TextView) convertView.findViewById(R.id.txtDataEvento);
            layout.txtDescricao = (TextView) convertView.findViewById(R.id.txtDescricao);
            layout.txtLocal = (TextView) convertView.findViewById(R.id.txtLocal);
            layout.txtHoraEntradaEvento = (TextView) convertView.findViewById(R.id.txtHorarioEntradaEvento);
            layout.txtHoraSaidaEvento = (TextView) convertView.findViewById(R.id.txtHorarioSaidaEvento);

            convertView.setTag(layout);
        }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            String horaEntrada[] = evento.getStart().split(" ");
            String horaEntradaAux[] = horaEntrada[0].split("-");

            String horaSaida[] = evento.getEnd().split(" ");
            String horaSaidaAux[] = horaSaida[0].split("-");

            holder.txtTitulo.setText(evento.getTitle());
            holder.txtDataEvento.setText(horaEntradaAux[2]+"/"+horaEntradaAux[1]+"/"+horaEntradaAux[0]);
            holder.txtDescricao.setText(evento.getDescription());
            holder.txtLocal.setText(evento.getLocation());
            holder.txtHoraEntradaEvento.setText(horaEntrada[1]);
            holder.txtHoraSaidaEvento.setText(horaSaida[1]);
        return convertView;
    }
}
