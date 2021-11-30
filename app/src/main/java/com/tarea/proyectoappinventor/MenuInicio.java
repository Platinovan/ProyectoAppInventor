package com.tarea.proyectoappinventor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MenuInicio extends AppCompatActivity {
    //Declaracion de variables
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebasedata;
    DatabaseReference JUGADORES;
    AppCompatButton opciones;
    AppCompatButton Jugar;
    TextView userField; //campo para nombre de usuario
    TextView puntuacion;

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
        firebasedata = FirebaseDatabase.getInstance();
        JUGADORES = firebasedata.getReference("OvniWallop Users");

        userField = findViewById(R.id.user_field);
        puntuacion = findViewById(R.id.Puntuacion);

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
            ConsultarDatos();
            Toast.makeText(getApplicationContext(), "De nuevo en linea", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void ConsultarDatos(){
        Query query = JUGADORES.orderByChild("Correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String user = ""+ds.child("Apodo").getValue();
                    String score = ""+ds.child("Puntuacion").getValue();
                    userField.setText(user);
                    puntuacion.setText(score);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}