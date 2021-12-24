package com.tarea.proyectoappinventor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.Size;
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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.number.NumberFormatter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

    //Strings
    String CORREO;
    String APODO;
    String FECHA;
    String IMAGEN;
    String PASSWORD;

    //Editar datos
    String passMessage = "Contraseña actualizada, asegurate de iniciar sesion con tu nueva contraseña";
    TextView EditarApodo;
    AppCompatButton CambiarPass;
    Dialog CambiarApodo;
    Dialog CargandoImagen;
    Dialog CambiarPassword;

    //Para ver las contrase;as
    boolean show1 = false;
    boolean show2 = false;
    boolean show3 = false;

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
        FotoDePerfil = findViewById(R.id.FotoDePerfil);
        almacenamientoReferencia = FirebaseStorage.getInstance().getReference();
        PermisosDeAlmacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        CambiarPass = findViewById(R.id.CambiarPassword);

        //Editar datos
        CambiarApodo = new Dialog(Perfil.this);
        CambiarPassword = new Dialog(Perfil.this);
        CargandoImagen = new Dialog(Perfil.this, android.R.style.Theme_Black_NoTitleBar);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firebasedata = FirebaseDatabase.getInstance();
        JUGADORES = firebasedata.getReference("OvniWallop Users");

        //Se consultan los datos del jugador
        ConsultarDatos();

        //Configuracion del icono de loading de la imagen
        CargandoImagen.setContentView(R.layout.cargando_imagen);
        RelativeLayout relativeLayout = CargandoImagen.findViewById(R.id.CargandoImagenIcono);
        relativeLayout.setClickable(false);
        CargandoImagen.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
                perfil = "Imagen";
                AcualizarImagenDePerfil();
            }
        });

        //Cuando se presione cambiar password
        CambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CambiarPassword.setContentView(R.layout.cambiar_password);

                //Campos de texto
                EditText ActualPass = CambiarPassword.findViewById(R.id.ActualPassField);
                EditText NewPass = CambiarPassword.findViewById(R.id.NewPass1);
                EditText NewPassConfirm = CambiarPassword.findViewById(R.id.NewPass2);

                //Ver el password
                AppCompatButton ShowCurrent = CambiarPassword.findViewById(R.id.ShowCurrentPass);
                AppCompatButton ShowPass1 = CambiarPassword.findViewById(R.id.ShowNewPass1);
                AppCompatButton ShowPass2 = CambiarPassword.findViewById(R.id.ShowNewPass2);
                
                //Confirmar o descartar los cambios (Botones)
                AppCompatButton Confirmar = CambiarPassword.findViewById(R.id.AceptarNuevoPass);
                AppCompatButton Cancelar = CambiarPassword.findViewById(R.id.CancelarNuevoPass);
                

                //Para ver la password actual
                ShowCurrent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show1 = !show1;
                        if(show1){
                            ShowCurrent.setBackgroundResource(R.drawable.hidden);
                            ActualPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            ActualPass.setSelection(ActualPass.length());
                        }else{
                            ShowCurrent.setBackgroundResource(R.drawable.view_dos);
                            ActualPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            ActualPass.setSelection(ActualPass.length());
                        }
                    }
                });


                //Para ver el pass1
                ShowPass1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show2 = !show2;
                        if(show2){
                            ShowPass1.setBackgroundResource(R.drawable.hidden);
                            NewPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            NewPass.setSelection(NewPass.length());
                        }else{
                            ShowPass1.setBackgroundResource(R.drawable.view_dos);
                            NewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            NewPass.setSelection(NewPass.length());
                        }
                    }
                });

                //Para ver el pass2
                ShowPass2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show3 = !show3;
                        if(show3){
                            ShowPass2.setBackgroundResource(R.drawable.hidden);
                            NewPassConfirm.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            NewPassConfirm.setSelection(NewPassConfirm.length());
                        }else{
                            ShowPass2.setBackgroundResource(R.drawable.view_dos);
                            NewPassConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            NewPassConfirm.setSelection(NewPassConfirm.length());
                        }
                    }
                });


                /* Cuando se acepten o se cancelen los cambios del password*/

                //Cuando se presione aceptar
                Confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Strings para los valores de los campos de texto
                        String CurrentPassword = ActualPass.getText().toString();
                        String NewPass1 = NewPass.getText().toString();
                        String NewPass2 = NewPassConfirm.getText().toString();

                        if(CurrentPassword.isEmpty() || NewPass1.isEmpty() || NewPass2.isEmpty()){
                             if(CurrentPassword.isEmpty()){
                                 ActualPass.setError("Minimo 6 caracteres");
                             }
                             if(NewPass1.isEmpty()){
                                 NewPass.setError("Minimo 6 caracteres");
                             }
                             if(NewPass2.isEmpty()){
                                 NewPassConfirm.setError("Minimo 6 caracteres");
                             }
                        }
                        if(CurrentPassword.equals(PASSWORD)) {
                            if (!(NewPass1.length() >= 6) || !(NewPass2.length() >= 6)) {
                                if (NewPass.length() < 6) {
                                    NewPass.setError("Minimo 6 caracteres");
                                }

                                if (NewPassConfirm.length() < 6) {
                                    NewPassConfirm.setError("Minimo 6 caracteres");
                                }
                            }else{
                                if (NewPass1.equals(NewPass2)) {
                                    //Ontiene el string de la confirmacion del password
                                    String NEWPASSWORD = NewPassConfirm.getText().toString().trim();

                                    //Para cambiar la contrase;a
                                    AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), CurrentPassword);

                                    user.reauthenticate(authCredential)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    user.updatePassword(NEWPASSWORD)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    String value = NEWPASSWORD;
                                                                    HashMap<String, Object> result = new HashMap<>();
                                                                    result.put("Contraseña", value);
                                                                    JUGADORES.child(user.getUid()).updateChildren(result)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    Toast.makeText(getApplicationContext(), ""+passMessage, Toast.LENGTH_SHORT).show();
                                                                                    auth.signOut();
                                                                                    startActivity(new Intent(Perfil.this, Login.class));
                                                                                    finish();
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    NewPass.setError("No coinciden");
                                    NewPassConfirm.setError("No coinciden");
                                }
                            }
                            }else {
                            ActualPass.setError("Incorrecto");
                        }
                    }
                });


                //Cuando se presione cancelar
                Cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show1 = false;
                        show2 = false;
                        show3 = false;
                        CambiarPassword.dismiss();
                    }
                });

                //Muestra el menu de cambiar password
                CambiarPassword.show();
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
                    FECHA = ""+ds.child("SeUnio").getValue();
                    IMAGEN = ""+ds.child("Imagen").getValue();
                    PASSWORD = ""+ds.child("Contraseña").getValue();

                    //Se ponen los valores en el campo de texto
                    Correo.setText(CORREO);
                    Apodo.setText(APODO);
                    Fecha.setText(FECHA);
                    if(!IMAGEN.equals("")) {
                        Picasso.get().load(IMAGEN).into(FotoDePerfil);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
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
        //Inicia la animacion loading para indicar que la imagen se esta cargando
        CargandoImagen.show();

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
                                                CargandoImagen.dismiss();
                                                Toast.makeText(getApplicationContext(), "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        CargandoImagen.dismiss();
                                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                CargandoImagen.dismiss();
                                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                            }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                CargandoImagen.dismiss();
                Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
