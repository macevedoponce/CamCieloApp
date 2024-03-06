package com.acevedo.caminoalcielo.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acevedo.caminoalcielo.Login.LoginActivity;
import com.acevedo.caminoalcielo.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    TextView txtDerechos, txtYear;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);

        //animacion
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.des_top);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.des_bot);

        txtDerechos = findViewById(R.id.txtDerechos);
        txtYear = findViewById(R.id.txtYear);
        imgLogo = findViewById(R.id.imgLogo);

        txtYear.setText(year+"");


        txtDerechos.setAnimation(animacion1);
        txtYear.setAnimation(animacion2);
        imgLogo.setAnimation(animacion2);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        },1900);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}