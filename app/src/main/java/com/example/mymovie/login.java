package com.example.mymovie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

public class login extends AppCompatActivity {
    EditText t_email, t_password;
    String str_email, str_pass;
    String url = "http://192.168.100.107/VSC%20PHP/Programacion%20Movil/Login/login.php";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        t_email = findViewById(R.id.txtemaillogin);
        t_password = findViewById(R.id.txtpasswordlogin);
        preferences = this.getSharedPreferences("login",Context.MODE_PRIVATE);
        editor = preferences.edit();
        revisarsesion();
    }

    public void login(View view) {
        if (t_email.getText().toString().equals("")) {
            Toast.makeText(this, "ingrese correo", Toast.LENGTH_SHORT).show();
        } else if (t_password.getText().toString().equals("")) {
            Toast.makeText(this, "ingrese contraseña", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Iniciando sesión...");
            progressDialog.show();

            str_email = t_email.getText().toString().trim();
            str_pass = t_password.getText().toString().trim();
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    if (response.equalsIgnoreCase("Login exitoso")) {
                        guardarsesion(str_email, str_pass);
                        startActivity(new Intent(login.this, Home.class));
                    } else {
                        Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", str_email);
                    params.put("password", str_pass);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(login.this);
            requestQueue.add(request);
        }
    }
    /*Validar que los datos esten correctos*/
    private void revisarsesion() {
        String email = preferences.getString("email", "");
        String password = preferences.getString("password", "");
        if (!email.equals("") && !password.equals("")) {
            startActivity(new Intent(login.this, Home.class));
        }
    }
/*metodo para guardar los datos que tenemos en nuestras variables email y password*/
    private void guardarsesion(String email, String password) {
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }

    public void registro(View view) {
        Intent intent = new Intent(this, registro.class);
        startActivity(intent);
    }
}