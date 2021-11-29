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

public class MenuInicio extends AppCompatActivity {
    //Declaracion de variables
    FirebaseAuth auth;
    FirebaseUser user;
    AppCompatButton cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_inicio);

        //Instanciacion de las variables declaradas
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        cerrar = findViewById(R.id.cerrarSesion);

        //cuando se presione el boton cerrar sesion
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });

    }
    //El metodo onStart verifica que la aplicacion esta abierta y ejecuta el metodo
    //que comprueba si el usuario ya inicio sesion o no
    @Override
    protected void onStart(){
        UsuarioenLinea();
        super.onStart();
    }

    //Este metodo comprueba si el usuario esta en linea
    private void UsuarioenLinea(){
        if(user != null){
            Toast.makeText(getApplicationContext(), "De nuevo en linea", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    //Metodo para cerrar sesion
    private void cerrarSesion(){
        auth.signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}