package br.com.tairoroberto.qualimsolucoes;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import br.com.tairoroberto.adapters.AdapterExpListview;
import br.com.tairoroberto.util.HttpConnection;

public class RelatoriosCadastCheckListActivity extends ActionBarActivity{


    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerList_left;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mTelasTitles;
    private String answer;
    private SearchView searchView;
    UsuarioLogado usuarioLogado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Coloca um efeito antes de mostrar a tela principal
        overridePendingTransition(R.anim.push_up_enter,R.anim.zoom_out_exit);
        setContentView(R.layout.activity_relatorios_cadastra_checklist);

        // mostra o logo do app na actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Cadastrar check list");

        //Verifica se foi enviado acão de sair
        Intent intent = getIntent();
        if (intent != null){

            Bundle bundle = intent.getExtras();
            if (bundle != null){
                usuarioLogado = bundle.getParcelable("usuarioLogado");
            }
        }



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
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Cadastrar cronograma"){
                    selectItemLeft(2);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar cronograma"){
                    selectItemLeft(3);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Cadastrar visitas técnicas"){
                    selectItemLeft(4);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar visitas técnicas"){
                    selectItemLeft(5);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Cadastrar auditórias"){
                    selectItemLeft(6);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar auditórias"){
                    selectItemLeft(7);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Cadastrar check list"){
                    selectItemLeft(8);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Visualizar check list"){
                    selectItemLeft(9);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Insirir despesa"){
                    selectItemLeft(10);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Ver despesas"){
                    selectItemLeft(11);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Mudar foto"){
                    selectItemLeft(12);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Foto de assinatura"){
                    selectItemLeft(13);
                }else if (parent.getExpandableListAdapter().getChild(groupPosition,childPosition).toString() == "Trocar senha"){
                    selectItemLeft(14);
                }
                return false;
            }
        });

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                RelatoriosCadastCheckListActivity.this,    /* Classe que chama a activity Activity */
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
        inflater.inflate(R.menu.menu_relatorios, menu);

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
                Toast.makeText(RelatoriosCadastCheckListActivity.this, R.string.app_not_available, Toast.LENGTH_LONG).show();
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

            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,PrincipalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        } else if (position == 1) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,TarefasVisualizarActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 2) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,CronogramaInserirActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 3) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,CronogramaVerActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        } else if (position == 4) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,RelatoriosCadastVisitaTecActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 5) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,RelatoriosVisualVisitaTecActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 6) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,RelatoriosCadastAuditoriaActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 7) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,RelatoriosVisualAuditoriaActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 8) {
            /*Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,RelatoriosCadastCheckListActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);*/

        }else if (position == 9) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,RelatoriosVisualCheckListActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 10) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,PrestacaoContasInserirActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 11) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,PrestacaoContasVerActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 12) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,PerfilAlterarFotoActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 13) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,PerfilAlterarAssinaturaActivity.class);
            intent.putExtra("usuarioLogado",usuarioLogado);
            startActivity(intent);

        }else if (position == 14) {
            Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,PerfilAlterarSenhaActivity.class);
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
        final ProgressDialog progress = new ProgressDialog(RelatoriosCadastCheckListActivity.this);
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

                                    Intent intent = new Intent(RelatoriosCadastCheckListActivity.this,PrincipalActivity.class);
                                    //tira todas as atividades da pilha e vai para a home
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("sair",true);
                                    startActivity(intent);
                                    progress.dismiss();

                                }else{
                                    progress.dismiss();
                                    Toast.makeText(RelatoriosCadastCheckListActivity.this, "Ainda conectado..!!!", Toast.LENGTH_SHORT).show();
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

