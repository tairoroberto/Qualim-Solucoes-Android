package br.com.tairoroberto.qualimsolucoes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

/**
 * Created by tairo on 24/11/14.
 */
public class InicialActivity extends Activity {

    private EditText edtUsuarioLogin, edtSenhaLogin;
    private Button btnLoginFragment,btnSairFragment, btnLogin_inicial,btnCadastro_inicial;
    private ViewFlipper img1,img2,img3,img4,img5,img6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        setContentView(R.layout.activity_inicial);

        chamaLogin();
    }


    public void chamaLogin() {
        overridePendingTransition(R.anim.slide_up, R.anim.rotate_in_enter);
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
        btnLoginFragment = (Button) dialog.findViewById(R.id.btnLoginFragment);
        btnSairFragment = (Button) dialog.findViewById(R.id.btnSairFragment);


        /*************************************************************************/
        /**                    Mostra o DialogFrafment 						*/
        /***********************************************************************/
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.setTitle(R.string.txtMensagemLogin);
        dialog.show();

        /*************************************************************************/
        /**                    Método para fazer login 						*/
        /***********************************************************************/
        btnLoginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InicialActivity.this, PrincipalActivity.class);

                overridePendingTransition(R.anim.push_down_enter, R.anim.push_down_exit);
                startActivity(intent);
                dialog.dismiss();
                InicialActivity.this.finish();
            }

        });

        /*************************************************************************/
        /**                    Método para fazer sair 						*/
        /***********************************************************************/
        btnSairFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                InicialActivity.this.finish();
            }
        });

    }


}
