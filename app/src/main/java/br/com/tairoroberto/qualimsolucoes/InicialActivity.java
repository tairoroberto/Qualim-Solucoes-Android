package br.com.tairoroberto.qualimsolucoes;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

        final Dialog dialog = new Dialog(InicialActivity.this);

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
                sendLogin(edtUsuarioLogin.getText().toString(),edtSenhaLogin.getText().toString(),dialog);
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

    public void sendLogin(final String email,final String password, final Dialog dialog){
        progress = new ProgressDialog(InicialActivity.this);
        progress.setMessage("Conectando...");
        progress.show();

        final String url = "http://www.nowsolucoes.com.br/qualim/public/login-android";

        new Thread(){
            public void run(){

                ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
                valores.add(new BasicNameValuePair("email", email));
                valores.add(new BasicNameValuePair("password", password));

                answer = HttpConnection.getSetDataWeb(url, valores);

                runOnUiThread(new Runnable(){
                    public void run(){
                        try{
                            if (answer != null){
                                if (!answer.equals("Login incorreto")){

                                    progress.dismiss();
                                    //Log.i("Script","Resposta: "+answer);

                                    String resposta[] = answer.split(",");
                                    usuarioLogado.setId(Long.parseLong(resposta[0]));
                                    usuarioLogado.setName(resposta[1]);
                                    usuarioLogado.setEmail(resposta[2]);

                                    /*//ArrayList de Usuarios logados para ser enviado
                                    ArrayList<UsuarioLogado> usuarioLogados = new ArrayList<UsuarioLogado>();
                                    usuarioLogados.add(usuarioLogado);*/

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
