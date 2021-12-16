package com.tarea.proyectoappinventor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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
        int PuntajeInt = usuarioList.get(i).getPuntaje();
        int PocisionINT = usuarioList.get(i).getPuntaje();

        //Se convierte el puntaje y la pocision en String
        String PUNTAJE = String.valueOf(PuntajeInt);
        String POCISION = String.valueOf(PocisionINT);

        //Se colocan los datos del jugador
        holder.NombreJugador.setText(NOMBRE);
        holder.PuntajeJugador.setText(PUNTAJE);
        holder.NumeroJugador.setText("#3");

        //Imagen del Jugador
        try{
            Picasso.get().load(IMAGEN).into(holder.ImagenJugador);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        //Variables
        CircleImageView ImagenJugador;
        TextView NombreJugador;
        TextView PuntajeJugador;
        TextView NumeroJugador;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //Incializando Variables
            ImagenJugador = itemView.findViewById(R.id.ImagenJugador);
            NombreJugador = itemView.findViewById(R.id.NombreDelJugador);
            PuntajeJugador = itemView.findViewById(R.id.PuntajeDeJugador);
            NumeroJugador = itemView.findViewById(R.id.NumeroDeJugador);

        }
    }
}
