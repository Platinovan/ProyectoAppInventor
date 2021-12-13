package com.tarea.proyectoappinventor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.HashSet;
import de.hdodenhof.circleimageview.CircleImageView;

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
    Dialog CambiarApodo;

    //Strings
    String CORREO;
    String APODO;
    String FECHA;
    String IMAGEN;

    //Editar datos
    TextView EditarApodo;

    //Firebase storage para la imagen de perfil
    CircleImageView FotoDePerfil;
    private StorageReference almacenamientoReferencia;
    private String rutaImagenFirebase = "FotosDePerfil/*";

    //Permisos
    private static final int CODIGO_SOLICITUD_DE_ALMACENAMIENTO = 200;
    private static final int CODIGO_PARA_SELECCION_IMAGEN = 300;

    //Arreglos
    private String[] PermisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfil;

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
        EditarApodo = findViewById(R.id.EditarApodo);
        CambiarApodo = new Dialog(Perfil.this);
        FotoDePerfil = findViewById(R.id.FotoDePerfil);
        almacenamientoReferencia = FirebaseStorage.getInstance().getReference();
        PermisosDeAlmacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebasedata = FirebaseDatabase.getInstance();
        JUGADORES = firebasedata.getReference("OvniWallop Users");

        //Se consultan los datos del jugador
        ConsultarDatos();

        //Cuando se presione editar apodo
        EditarApodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CambiarApodo.setContentView(R.layout.prueba_cambiar_apodo);

                //Variables del 'Nuevo apodo View'
                EditText NuevoApodo = CambiarApodo.findViewById(R.id.NuevoApodoField);
                AppCompatButton BorrarTexto = CambiarApodo.findViewById(R.id.ClearTextApodo);
                AppCompatButton AceptarCambio = CambiarApodo.findViewById(R.id.AceptarNuevoApodo);
                AppCompatButton CancelarCambio = CambiarApodo.findViewById(R.id.CancelarNuevoApodo);

                //Cuando se presione el icono de borrar texto
                BorrarTexto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NuevoApodo.setText("");
                    }
                });

                //Cuando se presione el boton de aceptar cambios
                AceptarCambio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newApodo = NuevoApodo.getText().toString().trim();
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("Apodo", newApodo);
                        JUGADORES.child(user.getUid()).updateChildren(result)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Apodo Actualizado", Toast.LENGTH_SHORT).show();
                                        CambiarApodo.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                //Cuando se presione el boton de cancelar cambios
                CancelarCambio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CambiarApodo.dismiss();
                        Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });

                CambiarApodo.show();
            }
        });

        //Cuando se haga click en la imagen para cambiarla
        FotoDePerfil.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Cambiar Imagen", Toast.LENGTH_SHORT).show();
                perfil = "Imagen";
                AcualizarImagenDePerfil();
            }
        });
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
                    IMAGEN = ""+ds.child("Imagen").getValue();

                    //Se ponen los valores en el campo de texto
                    Correo.setText(CORREO);
                    Apodo.setText(APODO);
                    Fecha.setText(FECHA);
                    Picasso.get().load(IMAGEN).into(FotoDePerfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Metodo para actualizar la imagen de perfil
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AcualizarImagenDePerfil(){
        if(!ComprobarPermisosDeAlmacenamiento()){
            //Si el permiso es denegado de almacenamiento
            SolicitarPermisoAlmacenamiento();
        }else{
            //Si se permitio el permiso de almacenamiento
            ElegirImagen();
        }
    }

    //Comprobar los permisos de almacenamiento
    private boolean ComprobarPermisosDeAlmacenamiento(){
        boolean resultado = ContextCompat.checkSelfPermission(Perfil.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return resultado;
    }

    //Se llama cuando el jugador presiona permitir o denegar
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CODIGO_SOLICITUD_DE_ALMACENAMIENTO:{
                //Seleccion de la imagen
                if(grantResults.length > 0){
                    boolean EscrituraDeAlmacenamientoAceptado = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(EscrituraDeAlmacenamientoAceptado){
                        //El permiso fue habilitado
                        ElegirImagen();
                    }else{
                        Toast.makeText(getApplicationContext(), "Permiso Denegado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //Se llama cuando el jugador ha elegido la imagen de la galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            //La imagen se obtiene la ruta
            if(requestCode == CODIGO_PARA_SELECCION_IMAGEN){
                imagen_uri = data.getData();
                SubirFoto(imagen_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //Solicitar permisos de almacinamiento
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void SolicitarPermisoAlmacenamiento(){
        requestPermissions(PermisosDeAlmacenamiento, CODIGO_SOLICITUD_DE_ALMACENAMIENTO);
    }

    //Elegir imagen desde la galeria
    private void ElegirImagen(){
        Intent AbrirGaleria = new Intent(Intent.ACTION_PICK);
        AbrirGaleria.setType("image/*");
        startActivityForResult(AbrirGaleria, CODIGO_PARA_SELECCION_IMAGEN);
    }

    //Subir la imagen al storage de firebase
    private void SubirFoto(Uri imagen_uri){
        String nombreImagen = rutaImagenFirebase + "" + perfil + user.getUid();
        StorageReference storageReference = almacenamientoReferencia.child(nombreImagen);
        storageReference.putFile(imagen_uri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while(!uriTask.isSuccessful());
                        Uri download = uriTask.getResult();

                            if(uriTask.isSuccessful()){
                                HashMap<String, Object> resultado = new HashMap<>();
                                resultado.put(perfil, download.toString());
                                JUGADORES.child(user.getUid()).updateChildren(resultado)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
