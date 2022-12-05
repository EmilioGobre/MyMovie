package com.example.mymovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.controladores.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class modify extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    TextView tvNombre;
    EditText etNombreBuscar,etTickets,etHorario,etPrecio,etClasificacion;
    Button btnBuscar,btnModificar;

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
        etHorario = findViewById(R.id.ethorario_add);
        etTickets = findViewById(R.id.etticket_mod);
        etPrecio = findViewById(R.id.etprecio_mod);
        etClasificacion = findViewById(R.id.etclasificacion_mod);
        btnBuscar = findViewById(R.id.btnbuscar_mod);
        btnModificar = findViewById(R.id.button_mod);

        btnBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){BuscarPeli();}
        });

        btnModificar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){ModificarPeli();}
        });
    }

    public void BuscarPeli(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = WebService.RAIZ + WebService.BUSCARPELI+ "?name=" +etNombreBuscar.getText();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response){
                JSONObject jsonObject = null;
                Toast.makeText(getApplicationContext(), "Pelicula Seleccionada "+etNombreBuscar.getText(), Toast.LENGTH_LONG).show();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        tvNombre.setText(jsonObject.getString("name"));
                        etHorario.setText(jsonObject.getString("horario"));
                        etTickets.setText(jsonObject.getString("tickets_disponibles"));
                        etPrecio.setText(jsonObject.getString("precio"));
                        etClasificacion.setText(jsonObject.getString("clasificacion"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "ERROR: No se encontro la pelicula", Toast.LENGTH_LONG).show();
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


        public void ModificarPeli(){}
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
            case R.id.delete:
                Intent intentdelete = new Intent(this, delete.class);
                startActivity(intentdelete);
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
                Intent intentinfo = new Intent(this, info.class);
                startActivity(intentinfo);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}