package com.tarea.proyectoappinventor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.awt.font.NumericShaper;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder>{
    //Contexto
    private Context context;
    private List <Usuario> usuarioList;

    //Constructor de la clase
    public Adaptador(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
    }


    //Infla el dise;o del jugador
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        //Se obtienen los datos del modelo de informacion
        String IMAGEN = usuarioList.get(i).getImagen();
        String NOMBRE = usuarioList.get(i).getApodo();
        String CORREO = usuarioList.get(i).getCorreo();
        String UID = usuarioList.get(i).getUid();
        String SEUNIO = usuarioList.get(i).getSeUnio();
        int PuntajeInt = usuarioList.get(i).getPuntuacion();

        //Se convierte el puntaje y la pocision en String
        String PUNTAJE = String.valueOf(PuntajeInt);

        //Se colocan los datos del jugador
        holder.NombreJugador.setText(NOMBRE);
        holder.PuntajeJugador.setText(PUNTAJE);

        //Imagen del Jugador
        try{
            Picasso.get().load(IMAGEN).into(holder.ImagenJugador);
        }catch (Exception e){
            Picasso.get().load(R.drawable.default_image_profile_dos).into(holder.ImagenJugador);
        }

        //Cuando se haga click en un jugador
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InfoJugador.class);
                //Envia a la otra actividad la informacion del jugador
                intent.putExtra("Imagen", IMAGEN);
                intent.putExtra("Apodo", NOMBRE);
                intent.putExtra("SeUnio", SEUNIO);
                intent.putExtra("Correo", CORREO);
                intent.putExtra("UID", UID);
                intent.putExtra("PUNTUACION", PUNTAJE);

                //Inicia la actividad
                context.startActivity(intent);

                //Muestra el nombre completo del jugador seleccionado
                Toast.makeText(context, ""+NOMBRE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        //Variables
        CircleImageView ImagenJugador;
        TextView NombreJugador;
        TextView PuntajeJugador;
        LinearLayout linearLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //Incializando Variables
            ImagenJugador = itemView.findViewById(R.id.ImagenJugador);
            NombreJugador = itemView.findViewById(R.id.NombreDelJugador);
            PuntajeJugador = itemView.findViewById(R.id.PuntajeDeJugador);
            linearLayout = itemView.findViewById(R.id.LinearLayoutGlobal);
        }
    }

}
