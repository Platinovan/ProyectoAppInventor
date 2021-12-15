package com.tarea.proyectoappinventor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuOpciones extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    AppCompatButton cerrar;
    AppCompatButton topGlobal;
    AppCompatButton Perfil;
    AppCompatButton Acercade;
    Dialog Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_opciones);

        //Instanciacion de las variables declaradas
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //Icono de carga
       Loading = new Dialog(MenuOpciones.this, android.R.style.Theme_Black_NoTitleBar);
       Loading.setContentView(R.layout.cargando);
       RelativeLayout relativeLayout = Loading.findViewById(R.id.CargandoLayout);

        //Botones
        cerrar = findViewById(R.id.cerrarSesion); //boton cerrar sesion
        topGlobal = findViewById(R.id.TopGlobal); //boton top global
        Perfil = findViewById(R.id.Perfil); //boton editar Perfil
        Acercade = findViewById(R.id.Acerca); //boton Acerda de


        //Top Global
        topGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RankGlobal = new Intent(getApplicationContext(), TopGlobal.class);
                relativeLayout.setClickable(false);
                Loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Loading.show();
                startActivity(RankGlobal);

            }
        });

        //Editar Perfil
        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent perfil = new Intent(getApplicationContext(), Perfil.class);
                relativeLayout.setClickable(false);
                Loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Loading.show();
                new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                        startActivity(perfil);
                   }
                }, 3000);
            }
        });

        //Acerca de
        Acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Acerca de", Toast.LENGTH_SHORT).show();
            }
        });

        //Cerrar sesion
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
                Toast.makeText(getApplicationContext(), "Sesion cerrada", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //Metodo para cerrar sesion
    private void cerrarSesion(){
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //Cuando se regrese desde otra actividad
    @Override
    protected void onResume() {
        super.onResume();
        if(Loading.isShowing()){
            Loading.dismiss();
        }
    }

    //Cuando se reinicie la actividad

    @Override
    protected void onRestart() {
        super.onRestart();
        if(Loading.isShowing()){
            Loading.dismiss();
        }
    }
}