package com.example.mymovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.controladores.WebService;
import com.example.mymovie.controladores.adaptador_peliculas;
import com.example.mymovie.modelos.peliculas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

public class modify extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    TextView tvNombre;
    EditText etNombreBuscar,etTickets,etHorario,etPrecio,etClasificacion;
    Button btnBuscar,btnModificar,btnBorrar;

    String url = WebService.RAIZ + WebService.MODIFICARPELIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        toolbar = findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = preferences.edit();

        tvNombre = findViewById(R.id.tvnombre_mod);
        etNombreBuscar = findViewById(R.id.etnombre_mod);
        etHorario = findViewById(R.id.ethorario_mod);
        etTickets = findViewById(R.id.etticket_mod);
        etPrecio = findViewById(R.id.etprecio_mod);
        etClasificacion = findViewById(R.id.etclasificacion_mod);
        btnBuscar = findViewById(R.id.btnbuscar_mod);
        btnModificar = findViewById(R.id.button_mod);
        btnBorrar = findViewById(R.id.button_del);

        btnBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){BuscarPeli();}
        });

        btnModificar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){ModificarPeli();}
        });

        btnBorrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){BorrarPeli();}
        });
    }

    public void BuscarPeli(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = WebService.RAIZ + WebService.BUSCARPELI+ "?name=" +etNombreBuscar.getText();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response){
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Toast.makeText(getApplicationContext(), "Pelicula Seleccionada "+etNombreBuscar.getText(), Toast.LENGTH_LONG).show();
                        jsonObject = response.getJSONObject(i);
                        tvNombre.setText(jsonObject.getString("name"));
                        etHorario.setText(jsonObject.getString("horario"));
                        etTickets.setText(jsonObject.getString("tickets_disponibles"));
                        etPrecio.setText(jsonObject.getString("precio"));
                        etClasificacion.setText(jsonObject.getString("clasificacion"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "ERROR: No se encontro la pelicula "+etNombreBuscar.getText(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), "ERROR: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonArrayRequest);
    }

        public void ModificarPeli(){
        final ProgressDialog loading = ProgressDialog.show(this,"Modificando Registro...","Espere por favor");

            StringRequest stringRequest=new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            loading.dismiss();
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            LimpiarCampos();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError{
                    String name=tvNombre.getText().toString().trim();
                    String horario=etHorario.getText().toString();
                    double tickets=Double.parseDouble(etTickets.getText().toString());
                    double precio=Double.parseDouble(etPrecio.getText().toString());
                    String clasificacion=etClasificacion.getText().toString();

                    Map<String,String> params=new Hashtable<>();
                    params.put("name",name);
                    params.put("horario",horario);
                    params.put("tickets_disponibles",String.valueOf(tickets));
                    params.put("precio",String.valueOf(precio));
                    params.put("clasificacion",clasificacion);

                    return params;
                }
            };
            RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

        public void BorrarPeli(){
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = WebService.RAIZ + WebService.BORRARPELIS+ "?name=" +etNombreBuscar.getText();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            LimpiarCampos();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "ERROR: "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError{
                    String name=tvNombre.getText().toString().trim();


                    Map<String,String> params=new Hashtable<>();
                    params.put("name",name);

                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent intenthome = new Intent(this, Home.class);
                startActivity(intenthome);
                break;
            case R.id.add:
                Intent intentadd = new Intent(this, add.class);
                startActivity(intentadd);
                break;
            case R.id.modify:
                Intent intentmodify = new Intent(this, modify.class);
                startActivity(intentmodify);
                break;
            case R.id.logout:
                editor.putString("email", "");
                editor.putString("password", "");
                editor.commit();
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.info:
                Intent intentinfo = new Intent(this, Fragments.class);
                startActivity(intentinfo);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void LimpiarCampos(){
        etNombreBuscar.setText("");
        tvNombre.setText("Nombre de la pelicula");
        etHorario.setText("");
        etTickets.setText("");
        etPrecio.setText("");
        etClasificacion.setText("");
    }
}