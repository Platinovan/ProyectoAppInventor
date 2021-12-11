package com.tarea.proyectoappinventor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Perfil extends AppCompatActivity {
    //Firebase variables
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebasedata;
    DatabaseReference JUGADORES;

    //Statement variables
    TextView Correo;
    TextView Apodo;
    TextView Fecha;

    //Strings
    String CORREO;
    String APODO;
    String FECHA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_perfil);

        //Instanciacion de las variables declaradas
        Correo = findViewById(R.id.perfilCorreo);
        Apodo = findViewById(R.id.perfilApodo);
        Fecha = findViewById(R.id.perfilFecha);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebasedata = FirebaseDatabase.getInstance();
        JUGADORES = firebasedata.getReference("OvniWallop Users");

        //Se consultan los datos del jugador
        ConsultarDatos();
    }

    //Consulta los datos de el jugador
    private void ConsultarDatos(){
        Query query = JUGADORES.orderByChild("Correo").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    //Se obtienen los datos de la base de datos
                    CORREO = ""+ds.child("Correo").getValue();
                    APODO = ""+ds.child("Apodo").getValue();
                    FECHA = ""+ds.child("Se unio").getValue();

                    //Se ponen los valores en el campo de texto
                    Correo.setText(CORREO);
                    Apodo.setText(APODO);
                    Fecha.setText(FECHA);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}