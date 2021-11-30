package com.tarea.proyectoappinventor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Variables
    TextView title1, title2; //Variables para cambio de fuente del titulo
    //Botones
    AppCompatButton registro;
    AppCompatButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Inicializacion
        registro = findViewById(R.id.botonRegistro);
        login = findViewById(R.id.botonLogin);

        //Inicia actividad Registro
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityRegistro = new Intent(getApplicationContext(), Registro.class);
                startActivity(activityRegistro);
            }
        });

        //Inicia actividad Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityLogin = new Intent(getApplicationContext(), Login.class);
                startActivity(activityLogin);
            }
        });
    }
}