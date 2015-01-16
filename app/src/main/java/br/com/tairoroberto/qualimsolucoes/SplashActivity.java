package br.com.tairoroberto.qualimsolucoes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


public class SplashActivity extends Activity {

    private Thread mSplashThread;
    private boolean mblnClicou = false;
    ImageView imageView;
    /**************************************************************************************/
    /**             Evento chamado quando a activity é executada pela primeira vez			 */
    /**
     * ************************ *******************************************************
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.push_down_enter, R.anim.push_down_exit);
        setContentView(R.layout.activity_splash);

        //pega a imagem para girar
        imageView = (ImageView) findViewById(R.id.imgSplash);

        // Create an animation instance
        Animation an = new RotateAnimation(0.0f, 90.0f, imageView.getPivotX(), imageView.getPivotY());

        // Set the animation's parameters
        an.setDuration(7000);               // duration in ms
        an.setRepeatCount(0);                // -1 = infinite repeated
        an.setRepeatMode(Animation.REVERSE); // reverses each repeat
        an.setFillAfter(true);               // keep rotation after animation

        imageView.setAnimation(an);

        /**************************************************************************************/
        /**            Thead que roda enquanto espera para abrir tela principal do App			 */
        /*************************** ********************************************************/
        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {

                        wait(3700);
                        mblnClicou = true;
                    }
                } catch (InterruptedException ex) {
                }
                /**************************************************************************************/
                /**                        Se for clicado Carrega a Activity Principal					 */
                /*************************** ********************************************************/
                if (mblnClicou) {
                    finish();

                    Intent i = new Intent();
                    i.setClass(SplashActivity.this, InicialActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            }
        };

        mSplashThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        /**********************************************************************************************/
        /**     garante que quando o usuário clicar no botão "Voltar" o sistema deve finalizar a thread  */
        /*************************** ******************************************************************/
        mSplashThread.interrupt();
    }


    /**********************************************************************************************/
    /**     O método abaixo finaliza o comando wait mesmo que ele não tenha terminado sua espera	  */
    /**
     * ************************ *****************************************************************
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (mSplashThread) {
                mblnClicou = true;
                mSplashThread.notifyAll();
            }
        }
        return true;
    }

}
