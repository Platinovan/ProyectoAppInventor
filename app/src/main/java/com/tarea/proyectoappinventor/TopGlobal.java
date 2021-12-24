package com.tarea.proyectoappinventor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TopGlobal extends AppCompatActivity {
    //Variables
    LinearLayoutManager mlayoutManager;
    RecyclerView recicleViewUsuarios;
    Adaptador adaptador;
    List<Usuario> usuarioList;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_top_global);

        //Inicializando variables
        mlayoutManager = new LinearLayoutManager(TopGlobal.this);
        firebaseAuth = FirebaseAuth.getInstance();
        recicleViewUsuarios = findViewById(R.id.recicleViewUsuarios);

        //Los ordena de manera inversa
        mlayoutManager.setReverseLayout(true);
        mlayoutManager.setStackFromEnd(true);
        recicleViewUsuarios.setHasFixedSize(true);
        recicleViewUsuarios.setLayoutManager(mlayoutManager);
        usuarioList = new ArrayList<>();

        //Se obtienen los datos de los usuarios
        ObtenerTodosLosUsuarios();

    }

    public void ObtenerTodosLosUsuarios(){
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("OvniWallop Users");

        ref.orderByChild("Puntuacion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    Usuario usuario = ds.getValue(Usuario.class);
                    usuarioList.add(usuario);
                    adaptador = new Adaptador(TopGlobal.this, usuarioList);
                    recicleViewUsuarios.setAdapter(adaptador);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}