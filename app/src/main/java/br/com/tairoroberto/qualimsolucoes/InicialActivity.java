package br.com.tairoroberto.qualimsolucoes;

import android.app.Activity;
import android.app.Dialog;
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

import br.com.tairoroberto.util.HttpConnection;

/**
 * Created by tairo on 24/11/14.
 */
public class InicialActivity extends Activity {

    private EditText edtUsuarioLogin, edtSenhaLogin;
    private Button btnLoginFragment,btnSairFragment, btnLogin_inicial,btnCadastro_inicial;
    private CheckBox ckRememberFragment;
    private ViewFlipper img1,img2,img3,img4,img5,img6;
    private Usuario usuario;
    private String answer;
    private boolean verifyResposta = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        setContentView(R.layout.activity_principal);
        usuario = new Usuario();

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
                sendLogin(edtUsuarioLogin.getText().toString(),edtSenhaLogin.getText().toString());
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

    public boolean sendLogin(final String email,final String password){

        final String url = "http://www.nowsolucoes.com.br/qualim/public/login-android";

        new Thread(){
            public void run(){
                answer = HttpConnection.getSetDataWeb(email,password, url);

                runOnUiThread(new Runnable(){
                    public void run(){
                        try{
                            //String resposta[] = answer.split(",");
                           // Toast.makeText(InicialActivity.this, answer, Toast.LENGTH_SHORT).show();
                            Log.i("Script","Resposta: "+answer);

                            if (answer != null){
                                verifyResposta = true;

                                if (!answer.equals("Login incorreto")){
                                    Intent intent = new Intent();
                                    intent.setClass(InicialActivity.this, PrincipalActivity.class);

                                    overridePendingTransition(R.anim.push_down_enter, R.anim.push_down_exit);
                                    startActivity(intent);
                                    InicialActivity.this.finish();
                                }else{
                                    Toast.makeText(InicialActivity.this, "Login ou Senha inválido", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        catch(NumberFormatException e){ e.printStackTrace(); }
                    }
                });
            }
        }.start();
        return verifyResposta;
    }


}
