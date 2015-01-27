package br.com.tairoroberto.qualimsolucoes;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import br.com.tairoroberto.util.HttpConnection;

/**
 * Created by tairo on 24/11/14.
 */
public class InicialActivity extends Activity {

    private EditText edtUsuarioLogin, edtSenhaLogin;
    private Button btnLoginFragment,btnSairFragment, btnLogin_inicial,btnCadastro_inicial;
    private CheckBox ckRememberFragment;
    private ViewFlipper img1,img2,img3,img4,img5,img6;
    private UsuarioLogado usuarioLogado;
    private String answer;
    ProgressDialog progress;
    private static final String PREF_NAME = "InicialActivity";
    private Dialog dialog;
    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        setContentView(R.layout.activity_principal);
        usuarioLogado = new UsuarioLogado();

        chamaLogin();
    }


    public void chamaLogin() {
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        /*************************************************************************/
        /**  Mostrar um DialogFrafment para ser inserido o endereço so website  */
        /***********************************************************************/

        dialog = new Dialog(InicialActivity.this);

        /** inflando o layout pra criação do DialogFragment*/
        dialog.setContentView(R.layout.fragment_login);

        /*************************************************************************/
        /**                Cria os componentes no DialogFrafment 				*/
        /***********************************************************************/

        edtUsuarioLogin = (EditText) dialog.findViewById(R.id.edtUsuarioLogin);
        edtSenhaLogin = (EditText) dialog.findViewById(R.id.edtSenhaLogin);
        ckRememberFragment = (CheckBox) dialog.findViewById(R.id.chLembrarFragment);
        btnLoginFragment = (Button) dialog.findViewById(R.id.btnLoginFragment);
        btnSairFragment = (Button) dialog.findViewById(R.id.btnSairFragment);


        /*************************************************************************/
        /**                    Mostra o DialogFrafment 						*/
        /***********************************************************************/
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.setTitle(R.string.txtMensagemLogin);
        dialog.show();

        /** In this mode, we can acess this sharedpreference in all Activities and Fragments*/
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        password = sharedPreferences.getString("password","");
        String passwordRegex[] = password.split("!");

        edtUsuarioLogin.setText(email);
        edtSenhaLogin.setText(passwordRegex[0]);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                InicialActivity.this.finish();
            }
        });

        /*************************************************************************/
        /**                    Método para fazer login 						*/
        /***********************************************************************/
        btnLoginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Enviaa requisição para o servidor
                ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
                valores.add(new BasicNameValuePair("email", edtUsuarioLogin.getText().toString()));
                valores.add(new BasicNameValuePair("password", edtSenhaLogin.getText().toString()));

                //verify if saved password an email
                if (ckRememberFragment.isChecked()){
                    //store login and password in sharedPreferences
                    /** In this mode, we can acess this sharedpreference in all Activities and Fragments*/
                    SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email",edtUsuarioLogin.getText().toString());
                    editor.putString("password", edtSenhaLogin.getText().toString() + "!1_2Dgg3+3wfat@887S!.*_09@!bheff&675#vfojjdl^");
                    editor.commit();

                }else{
                    //store login and password in sharedPreferences
                    /** In this mode, we can acess this sharedpreference in all Activities and Fragments*/
                    SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                }

                SendLogin sendLogin = new SendLogin(InicialActivity.this);
                sendLogin.execute(valores);
            }
        });

        /*************************************************************************/
        /**                    Método para fazer sair 						*/
        /***********************************************************************/
        btnSairFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    /*******************************************************************************************/
    /**                   Class to make insert event in system                               */
    /*****************************************************************************************/
    private class SendLogin extends AsyncTask<ArrayList<NameValuePair>,Void,String> {
        Context context;
        private ProgressDialog progress;

        public SendLogin(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Carregando...");
            progress.show();
        }

        @Override
        protected String doInBackground(ArrayList<NameValuePair>... params) {
            final String url = "http://www.nowsolucoes.com.br/qualim/public/login-android";
            answer = HttpConnection.getSetDataWeb(url, params[0]);
            return answer;
        }

        @Override
        protected void onPostExecute(String answer) {
            super.onPostExecute(answer);
            //Log.i("Script","Resposta do servidor: "+answer);
            if (!answer.equals("Login incorreto")){
                progress.dismiss();
                //Log.i("Script","Resposta: "+answer);

                String resposta[] = answer.split(",");
                usuarioLogado.setId(Long.parseLong(resposta[0]));
                usuarioLogado.setName(resposta[1]);
                usuarioLogado.setEmail(resposta[2]);

                Intent intent = new Intent();
                intent.setClass(InicialActivity.this, PrincipalActivity.class);
                intent.putExtra("usuarioLogado",usuarioLogado);
                overridePendingTransition(R.anim.push_down_enter, R.anim.push_down_exit);

                startActivity(intent);
                dialog.dismiss();
                InicialActivity.this.finish();

            }else{
                progress.dismiss();
                Toast.makeText(InicialActivity.this, "Login ou Senha inválido", Toast.LENGTH_SHORT).show();
                Log.i("Script","Resposta servidor: "+answer);
            }
        }
    }
}
