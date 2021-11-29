package com.tarea.proyectoappinventor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuInicio extends AppCompatActivity {
    //Declaracion de variables
    FirebaseAuth auth;
    FirebaseUser user;
    AppCompatButton opciones;
    AppCompatButton Jugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_menu_inicio);

        //Instanciacion de las variables declaradas
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        opciones = findViewById(R.id.MasOpciones);
        Jugar = findViewById(R.id.Jugar);

        //cuando se presione el boton Mas Opciones
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MenuOpciones.class);
                startActivity(intent);
            }
        });

        //Inicia actividad del juego
        Jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Jugar", Toast.LENGTH_SHORT).show();
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

}