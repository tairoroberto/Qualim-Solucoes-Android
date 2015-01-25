package br.com.tairoroberto.qualimsolucoes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.tairoroberto.adapters.AdapterExpListview;
import br.com.tairoroberto.adapters.CalendarAdapter;
import br.com.tairoroberto.util.HttpConnection;
import br.com.tairoroberto.util.Utility;

import static br.com.tairoroberto.util.ValidaHora.validate;

public class CronogramaActivity extends ActionBarActivity{


    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList_left;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mTelasTitles;
    private String answer;
    private SearchView searchView;
    private UsuarioLogado usuarioLogado;
    private CalendarView calendar;

    /******************************************************************/
    /**        inicio da Variáveis do calendario                     */
    /****************************************************************/

    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> titulo;
    ArrayList<String> descricao;
    ArrayList<String> horaInicio;
    ArrayList<String> horaSaida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Coloca um efeito antes de mostrar a tela principal
        overridePendingTransition(R.anim.push_left_enter,R.anim.push_right_exit);
        setContentView(R.layout.activity_cronograma);

        // mostra o logo do app na actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Cronograma");

        //Verifica se foi enviado acão de sair
        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){
                usuarioLogado = bundle.getParcelable("usuarioLogado");
            }
        }

        /****************************************************************************************/
        /**                     ligacção das variaveis do java com as do Xml                   */
        /**************************************************************************************/
        //calendar = (CalendarView) findViewById(R.id.calendarView);
        Locale.setDefault(Locale.US);

        rLayout = (LinearLayout) findViewById(R.id.layoutEventos);
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new CalendarAdapter(this, month);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        handler = new Handler();
        handler.post(calendarUpdater);

        final TextView title = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // removing the previous view if added
                if (((LinearLayout) rLayout).getChildCount() > 0) {
                    ((LinearLayout) rLayout).removeAllViews();
                }

                date = new ArrayList<String>();
                titulo = new ArrayList<String>();
                descricao = new ArrayList<String>();
                horaInicio = new ArrayList<String>();
                horaSaida = new ArrayList<String>();

                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                for (int i = 0; i < Utility.startDates.size(); i++) {
                    if (Utility.startDates.get(i).equals(selectedGridDate)) {
                        titulo.add(Utility.nameOfEvent.get(i));
                        descricao.add(Utility.descriptions.get(i));
                        date.add(Utility.startDates.get(i));
                        horaInicio.add(Utility.startHour.get(i));
                        horaSaida.add(Utility.endHour.get(i));
                    }
                }

                String dateAux[];
                if (descricao.size() > 0) {
                    for (int i = 0; i < descricao.size(); i++) {
                        //Views to add on layout
                        TextView txtTitleEvent = new TextView(CronogramaActivity.this);
                        TextView txtDescEvent = new TextView(CronogramaActivity.this);
                        TextView txtStartEndHour = new TextView(CronogramaActivity.this);
                        View view = new View(CronogramaActivity.this);
                        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
                        view.setBackgroundColor(Color.BLACK);

                        //design views to show
                        txtTitleEvent.setTextSize(16);
                        txtTitleEvent.setTextColor(Color.BLACK);
                        txtDescEvent.setTextSize(14);
                        txtStartEndHour.setTextSize(14);
                        txtStartEndHour.setTextColor(Color.RED);

                        //Convert date
                        dateAux = date.get(i).split("-");

                        // set some properties of rowTextView or something
                        txtTitleEvent.setText(titulo.get(i));
                        txtDescEvent.setText(descricao.get(i));
                        txtStartEndHour.setText("Inicio: " + horaInicio.get(i) + "   Fim: " + horaSaida.get(i));

                        // add the textview to the linearlayout
                        rLayout.addView(txtTitleEvent);
                        rLayout.addView(txtDescEvent);
                        rLayout.addView(txtStartEndHour);
                        rLayout.addView(view);
                    }

                }

                descricao = null;
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //Converte data
                final String data[] = parent.getAdapter().getItem(position).toString().split("-");

                //Dialogo to show cronogram
                final Dialog dialog = new Dialog(CronogramaActivity.this);
                /** inflando o layout pra criação do DialogFragment*/
                dialog.setContentView(R.layout.fragment_salvar_cronograma);

                /*************************************************************************/
                /**                Cria os componentes no DialogFrafment 				*/
                /***********************************************************************/
                final EditText edtTituloCronograma = (EditText) dialog.findViewById(R.id.edtTituloCronograma);
                final EditText edtLocalCronograma = (EditText) dialog.findViewById(R.id.edtLocalCronograma);
                final EditText edtDescricaoCronograma = (EditText) dialog.findViewById(R.id.edtDescricaoCronograma);
                final EditText edtHoraEntradaCronograma = (EditText) dialog.findViewById(R.id.edtHoraEntradaCronograma);
                final EditText edtHoraSaidaCronograma = (EditText) dialog.findViewById(R.id.edtHoraSaidaCronograma);
                Button btnSalvarCronograma = (Button) dialog.findViewById(R.id.btnSalvarCronograma);
                Button btnSairCronograma = (Button) dialog.findViewById(R.id.btnSairCronograma);

                /*************************************************************************/
                /**                    Mostra o DialogFrafment 						*/
                /***********************************************************************/
                dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                dialog.setTitle("Criar cronograma");
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        refreshCalendar();
                    }
                });

                /*************************************************************************/
                /**                    Método para fazer login 						*/
                /***********************************************************************/
                btnSalvarCronograma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                         if (edtLocalCronograma.getText().toString().equals("")){
                            Toast.makeText(CronogramaActivity.this,"Insira o Local",Toast.LENGTH_SHORT).show();
                             edtLocalCronograma.requestFocus();

                        }else if (edtDescricaoCronograma.getText().toString().equals("")){
                             Toast.makeText(CronogramaActivity.this,"Insira uma descrição",Toast.LENGTH_SHORT).show();
                             edtDescricaoCronograma.requestFocus();

                         }else if (edtHoraEntradaCronograma.getText().toString().equals("")){
                             Toast.makeText(CronogramaActivity.this,"Insira a Hora de entrada",Toast.LENGTH_SHORT).show();
                             edtHoraEntradaCronograma.requestFocus();

                         }else if (!validate(edtHoraEntradaCronograma.getText().toString())){
                            Toast.makeText(CronogramaActivity.this,"Formato do horário de entrada incorreto",Toast.LENGTH_SHORT).show();
                            edtHoraEntradaCronograma.requestFocus();

                        }else if (edtHoraSaidaCronograma.getText().toString().equals("")){
                            Toast.makeText(CronogramaActivity.this,"Insira a Hora de saída",Toast.LENGTH_SHORT).show();
                             edtHoraSaidaCronograma.requestFocus();

                        }else if (!validate(edtHoraSaidaCronograma.getText().toString())){
                            Toast.makeText(CronogramaActivity.this,"Formato do horário de saída incorreto",Toast.LENGTH_SHORT).show();
                             edtHoraSaidaCronograma.requestFocus();

                        }else {

                             String HoraMinEntrada[] = edtHoraEntradaCronograma.getText().toString().split(":");
                             String HoraMinSaida[] = edtHoraSaidaCronograma.getText().toString().split(":");

                             Calendar beginTime = Calendar.getInstance();
                             beginTime.set(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2]),
                                                    Integer.parseInt(HoraMinEntrada[0]), Integer.parseInt(HoraMinEntrada[1]));

                             Calendar endTime = Calendar.getInstance();
                             endTime.set(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2]),
                                                    Integer.parseInt(HoraMinSaida[0]), Integer.parseInt(HoraMinSaida[1]));

                             Intent intent = new Intent(Intent.ACTION_INSERT)
                                     .setData(CalendarContract.Events.CONTENT_URI)
                                     .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                                     .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                                     .putExtra(CalendarContract.Events.TITLE, edtTituloCronograma.getText().toString())
                                     .putExtra(CalendarContract.Events.DESCRIPTION, edtDescricaoCronograma.getText().toString())
                                     .putExtra(CalendarContract.Events.EVENT_LOCATION, edtLocalCronograma.getText().toString())
                                     .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);



                             ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
                             valores.add(new BasicNameValuePair("title", edtTituloCronograma.getText().toString()));
                             valores.add(new BasicNameValuePair("description", edtDescricaoCronograma.getText().toString()));
                             valores.add(new BasicNameValuePair("start", Utility.getHour(beginTime.getTimeInMillis())));
                             valores.add(new BasicNameValuePair("end", Utility.getHour(endTime.getTimeInMillis())));
                             valores.add(new BasicNameValuePair("nutricionista_id", usuarioLogado.getId()+""));

                             //Send data to server
                             storeEvent(valores);
                             startActivity(intent);
                             dialog.dismiss();
                         }
                    }
                });



                /*************************************************************************/
                /**                         Método para  sair 						    */
                /***********************************************************************/
                btnSairCronograma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CronogramaActivity.this,"Sair ",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });


                return false;
            }
        });

        /*************************************************************************************/
        /**  fim da implementação do calendario*/
        /***********************************************************************************/





        /****************************************************************************************/
        /**                     Implementação do ExpadableListView                             */
        /**************************************************************************************/
        //mTitle = mDrawerTitle = getTitle();
        //Pega um array de String para colocar no drawer

        //Coloca os resources nas variáveis
        mTelasTitles = getResources().getStringArray(R.array.telas_array);

        //Linka o DrawerLayout do java com o xml
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Linka o ListView do java com o xml
        mDrawerList_left = (ExpandableListView) findViewById(R.id.left_drawer);

        //  Configura uma sombra personalizada quando o Drawer é aberto
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //Configura a lista do Drawer com os items do array e seta o evento de clique da lista
        //configura a lista da direita e esqueda do Drawer
        mDrawerList_left.setAdapter(new AdapterExpListview(this));

        mDrawerList_left.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Verifify whacth screen go
                if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Tela Inicial"){
                    selectItemLeft(0);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar tarefas"){
                    selectItemLeft(1);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar cronograma"){
                    selectItemLeft(2);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Cadastrar visitas técnicas"){
                    selectItemLeft(3);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar visitas técnicas"){
                    selectItemLeft(4);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Cadastrar auditórias"){
                    selectItemLeft(5);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar auditórias"){
                    selectItemLeft(6);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Cadastrar check list"){
                    selectItemLeft(7);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar check list"){
                    selectItemLeft(8);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Insirir despesa"){
                    selectItemLeft(9);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Ver despesas"){
                    selectItemLeft(10);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Mudar foto"){
                    selectItemLeft(11);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Foto de assinatura"){
                    selectItemLeft(12);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Trocar senha"){
                    selectItemLeft(13);
                }
                return false;
            }
        });

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                CronogramaActivity.this,    /* Classe que chama a activity Activity */
                mDrawerLayout,         /* Layout que será mostrado DrawerLayout  */
                R.drawable.ic_drawer,  /* Icone que aparecera na ActionBar */
                R.string.drawer_open){ /* Descrição */

            // Método chamado quando o fragment é fechado
            public void onDrawerClosed(View view) {
                //Toast.makeText(EventosActivity.this, "Teste Drawer close",Toast.LENGTH_SHORT).show();
            }

            // Método chamado quando o fragment é aberto
            public void onDrawerOpened(View drawerView) {
                //Toast.makeText(EventosActivity.this, "Teste Drawer open",Toast.LENGTH_SHORT).show();
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
           // selectItemLeft(0);
        }

    }

    /****************************************************************************************/
    /**                               Implementação do Menu                                */
    /**************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cronograma, menu);

        //Implementa o SearchView
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchFiltro());
        return super.onCreateOptionsMenu(menu);

    }

    /****************************************************************************************/
    /**                    Implementação da clase do searchView                            */
    /**************************************************************************************/
    private class SearchFiltro implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextChange(String query) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {

            //Cria uma intent para manipular o websearch para o item selecionado
            //Envia conteudo do searchView capturado para uma intent para fazer uma busca na internet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY,query);

            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(CronogramaActivity.this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Se o drawer for aberto, esconde os items de ações  relatados para o cotainer
        boolean drawerLeftOpen = mDrawerLayout.isDrawerOpen(mDrawerList_left);
        menu.findItem(R.id.action_exit).setVisible(!drawerLeftOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /****************************************************************************************/
    /**                      Implementação da selecção do menu                             */
    /**************************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Manipula as ações dos botões
        switch(item.getItemId()) {
            case R.id.action_exit:
                Intent intent = new Intent(this,PrincipalActivity.class);
                //tira todas as atividades da pilha e vai para a home
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("sair",true);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * @param position
     */
    /****************************************************************************************/
    /**             Implementação do seleção da lista de menus lateral                     */
    /**************************************************************************************/
    private void selectItemLeft(int position) {

        //Atualiza o item selecionado e titulo, depois fecha o Drawer
        mDrawerList_left.setItemChecked(position, true);
        //setTitle(mDrawerList_left.getExpandableListAdapter().getGroup(position).toString());
        mDrawerLayout.closeDrawer(mDrawerList_left);

        if (position == 0) {

            Intent intent = new Intent(CronogramaActivity.this,PrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        } else if (position == 1) {
            Intent intent = new Intent(CronogramaActivity.this,TarefasVisualizarActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 2) {
            /*Intent intent = new Intent(CronogramaActivity.this,CronogramaActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);*/

        } else if (position == 3) {
            Intent intent = new Intent(CronogramaActivity.this,RelatoriosCadastVisitaTecActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 4) {
            Intent intent = new Intent(CronogramaActivity.this,RelatoriosVisualVisitaTecActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 5) {
            Intent intent = new Intent(CronogramaActivity.this,RelatoriosCadastAuditoriaActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 6) {
            Intent intent = new Intent(CronogramaActivity.this,RelatoriosVisualAuditoriaActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 7) {
            Intent intent = new Intent(CronogramaActivity.this,RelatoriosCadastCheckListActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 8) {
            Intent intent = new Intent(CronogramaActivity.this,RelatoriosVisualCheckListActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 9) {
            Intent intent = new Intent(CronogramaActivity.this,PrestacaoContasInserirActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 10) {
            Intent intent = new Intent(CronogramaActivity.this,PrestacaoContasVerActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 11) {
            Intent intent = new Intent(CronogramaActivity.this,PerfilAlterarFotoActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 12) {
            Intent intent = new Intent(CronogramaActivity.this,PerfilAlterarAssinaturaActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }else if (position == 13) {
            Intent intent = new Intent(CronogramaActivity.this,PerfilAlterarSenhaActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);
        }

    }



    /****************************************************************************************/
    /**                          Muda o titulo da ActionBar                                */
    /**************************************************************************************/
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Syncroniza o toggle state depois  que o onRestoreInstanceState tiver ocorrido.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /*******************************************************************************************/
    /**                  Method to make logout in system                                      */
    /*****************************************************************************************/
    public void sendLogout(){
        final ProgressDialog progress = new ProgressDialog(CronogramaActivity.this);
        progress.setMessage("Desconectando...");
        progress.show();

        final String url = "http://www.nowsolucoes.com.br/qualim/public/logout-android";

        new Thread(){
            public void run(){

                ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
                valores.add(new BasicNameValuePair("acao", "desconectar"));

                answer = HttpConnection.getSetDataWeb(url, valores);

                runOnUiThread(new Runnable(){
                    public void run(){
                        try{
                            if (answer != null){
                                if (!answer.equals("Ainda conectado")){

                                    Intent intent = new Intent(CronogramaActivity.this,PrincipalActivity.class);
                                    //tira todas as atividades da pilha e vai para a home
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("sair",true);
                                    startActivity(intent);
                                    progress.dismiss();

                                }else{
                                    progress.dismiss();
                                    Toast.makeText(CronogramaActivity.this, "Ainda conectado..!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        catch(NumberFormatException e){ e.printStackTrace(); }
                    }
                });
            }
        }.start();
    }



    /****************************************************************************************/
    /**                      metodo que gerencia o calendario                             */
    /**************************************************************************************/
    protected void setNextMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    protected void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {
        TextView title = (TextView) findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        handler.post(calendarUpdater); // generate some calendar items

        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
        rLayout.removeAllViews();
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();

            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
            event = Utility.readCalendarEvent(CronogramaActivity.this);
            Log.d("=====Event====", event.toString());
            Log.d("=====Date ARRAY====", Utility.startDates.toString());

            for (int i = 0; i < Utility.startDates.size(); i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add(Utility.startDates.get(i).toString());
            }
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
        }
    };


    /*******************************************************************************************/
    /**                  Method to make insert expense in system                              */
    /*****************************************************************************************/
    public void storeEvent(final ArrayList<NameValuePair> valores ){
        final ProgressDialog progress = new ProgressDialog(CronogramaActivity.this);
        progress.setMessage("Cadastrando...");
        progress.show();

        final String url = "http://www.nowsolucoes.com.br/qualim/public/store-events-android";

        new Thread(){
            public void run(){

                answer = HttpConnection.getSetDataWeb(url, valores);

                runOnUiThread(new Runnable(){
                    public void run(){
                        try{
                            if (answer != null){
                                if (answer.equals("Saved")){
                                    progress.dismiss();
                                    Toast.makeText(CronogramaActivity.this, "Evento cadastrado com sucesso..!!!", Toast.LENGTH_LONG).show();

                                }else{
                                    progress.dismiss();
                                    Toast.makeText(CronogramaActivity.this, "Evento não cadastrado..!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        catch(NumberFormatException e){ e.printStackTrace(); }
                    }
                });
            }
        }.start();
    }


}
