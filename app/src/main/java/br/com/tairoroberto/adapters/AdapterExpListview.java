package br.com.tairoroberto.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import br.com.tairoroberto.qualimsolucoes.R;

/**
 * Created by tairo on 17/01/15.
 */
public class AdapterExpListview extends BaseExpandableListAdapter{

    private Context context;

    public AdapterExpListview(Context context) {
        this.context = context;
    }

    /************************************************************************************
     *	 			 		String con todos os opçõesPai						*
     *************************** ********************************************************/

    public String[] opcoesPai = { "Home", "Tarefas","Cronograma", "Relatórios", "Prestação de contas", "Meu perfil"};


    /************************************************************************************
     *	 				 Array de String com todos as opções    						*
     *************************** ********************************************************/

    public String[][] opcoesFilho = {

            /************************ Opções do Home************************/

            { "Tela Inicial" },

            /************************ Opções das Tarefas************************/

            { "Visualizar tarefas" },

            /************************ Opções do Cronograma************************/

            { "Cadastrar cronograma","Visualizar cronograma" },


            /************************ Opções dos Relatorios************************/

            { "Cadastrar visitas técnicas"/*,"Visualizar visitas técnicas",
                    "Cadastrar auditórias","Visualizar auditórias",
                    "Cadastrar check list","Visualizar check list"*/},//

            /************************ Opções da Prestação de contas************************/

            { "Insirir despesa", "Ver despesas" },

            /************************ Opções do perfil do usuario************************/

            { "Mudar foto", "Foto de assinatura","Trocar senha" }
    };


    @Override
    public int getGroupCount() {
        return opcoesPai.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return opcoesFilho[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return opcoesPai[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return opcoesFilho[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        /************************************************************************************************
         *	Seta os objetos do ViewHolder child com os objtos do Xml em que foi feito o layout de linha	*
         *************************** *********************************************************************/

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.explistview_layout, null);

        ImageView icone = (ImageView) convertView.findViewById(R.id.imgPai);
        //verificação para a imagem
        switch (groupPosition){
            case 0:
                icone.setImageResource(R.drawable.ic_action_storage);
                break;
            case 1:
                icone.setImageResource(R.drawable.ic_action_go_to_today);
                break;
            case 2:
                icone.setImageResource(R.drawable.ic_action_add_to_queue);
                break;
            case 3:
                icone.setImageResource(R.drawable.ic_action_event);
                break;
            case 4:
                icone.setImageResource(R.drawable.ic_action_view_as_list);
                break;
            case 5:
                icone.setImageResource(R.drawable.ic_action_person);
                break;
        }
        TextView txtPai = (TextView) convertView.findViewById(R.id.txtPai);
        txtPai.setText(opcoesPai[groupPosition]);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView texto = new TextView(context);
        texto.setText(opcoesFilho[groupPosition][childPosition]);
        texto.setPadding(60, 20, 20, 20);
        texto.setTextColor(Color.parseColor("#DC143C"));
        texto.setTextSize(16);

        texto.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        return texto;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
