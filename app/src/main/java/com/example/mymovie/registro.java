package com.example.mymovie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class registro extends AppCompatActivity {
EditText t_nombre, t_email, t_contrasena;
Button b_registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        /*Aqui llama los edit text de la actividad registro*/
        t_nombre = (EditText) findViewById(R.id.txtnombreregistro);
        t_email = (EditText) findViewById(R.id.txtemailregistro);
        t_contrasena = (EditText) findViewById(R.id.txtpasswordregistro);
        /*Aqui llama el button de la actividad registro*/
        b_registrar = (Button) findViewById(R.id.buttonregistro);

        b_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*manda a llamar la funcion insertarDatos*/
                insertarDatos();
            }
        });
    }

    private void insertarDatos() {
        /*final es porque sera una constante*/
        /*Va a agarrar los datos y los hara de tipo string*/
        final String nombre = t_nombre.getText().toString().trim();
        final String email = t_email.getText().toString().trim();
        final String password = t_contrasena.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando...");

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            /*Si alguno de los campos esta vacio, mandara un mensaje*/
            progressDialog.dismiss();
            Toast.makeText(this, "Te faltan uno o varios campos", Toast.LENGTH_SHORT).show();
        } else {
            /*Si no esta vacio, mandara un mensaje de que se registro*/
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.100.107/VSC%20PHP/Programacion%20Movil/Login/insertar.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    /*Si esta correcto, mandara un mensaje de que se insertaron los datos*/
                    if (response.equalsIgnoreCase("Datos Insertados")) {
                        Toast.makeText(registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent intent = new Intent(registro.this, login.class);
                        startActivity(intent);
                    } else {
                        /*Si no, mandara un mensaje de que esta mal y el error de volley*/
                        Toast.makeText(registro.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Toast.makeText(registro.this, "Registro Fallido", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    /*Aqui mandara el error de volley por si hay mas*/
                    Toast.makeText(registro.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }){
                @NonNull
                @Override
                /*envia los datos a la base de datos, va a agarrar un dato string y va a mandar un dato string*/
                protected Map<String, String> getParams() throws AuthFailureError {
                    /*Map nos permite almacenar pares clave/valor, cada valor sera identificado con una clave y cada clave solo tendra un valor*/
                    Map<String,String> params = new HashMap<>();
                    /*la clave 'nombre' tendra el valor de nombre*/
                    params.put("nombre", nombre);
                    /*la clave 'email' tendra el valor de email*/
                    params.put("email", email);
                    /*la clave 'contrasena' tendra el valor de contrasena*/
                    params.put("password",password);
                    return params;
                }
            };
            /*Esto es para que mande todos los datos*/
            RequestQueue requestQueue = Volley.newRequestQueue(registro.this);
            requestQueue.add(request);
        }
    }

    @Override
    /*cuando le demos a la tecla de atras va a finalizar el proceso*/
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void login(View view){
        /*Ir a la actividad de login*/
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
}