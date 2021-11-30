package com.tarea.proyectoappinventor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuOpciones extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    AppCompatButton cerrar;
    AppCompatButton topGlobal;
    AppCompatButton Perfil;
    AppCompatButton Acercade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_opciones);

        //Instanciacion de las variables declaradas
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        //Botones
        cerrar = findViewById(R.id.cerrarSesion); //boton cerrar sesion
        topGlobal = findViewById(R.id.TopGlobal); //boton top global
        Perfil = findViewById(R.id.Perfil); //boton editar Perfil
        Acercade = findViewById(R.id.Acerca); //boton Acerda de


        //Top Global
        topGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Top Global", Toast.LENGTH_SHORT).show();
            }
        });

        //Editar Perfil
        Perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Editar Perfil", Toast.LENGTH_SHORT).show();
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
}