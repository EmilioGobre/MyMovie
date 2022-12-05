package com.example.mymovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import java.util.List;


public class Home extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    List<peliculas> peliculasList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = preferences.edit();
        recyclerView = (RecyclerView) findViewById(R.id.rvListaPeliculas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        peliculasList = new ArrayList<>();
        MostarDatos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

private void MostarDatos(){
    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
    String url = WebService.RAIZ + WebService.MOSTRARPELIS;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject pelicula = array.getJSONObject(i);
                            peliculasList.add(new peliculas(
                                    pelicula.getString("name"),
                                    pelicula.getString("horario"),
                                    pelicula.getInt("tickets_disponibles"),
                                    pelicula.getDouble("precio"),
                                    pelicula.getString("clasificacion"),
                                    pelicula.getString("imagen")
                            ));
                        }
                        adaptador_peliculas myadaptador = new adaptador_peliculas(getApplicationContext(),peliculasList);
                        recyclerView.setAdapter(myadaptador);

                    }
                    catch (JSONException e){
                        Toast.makeText(getApplicationContext(), "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), "ERROR: "+error.getMessage(), Toast.LENGTH_LONG).show();
        }
    });
    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
}

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Intent intenthome = new Intent(Home.this, Home.class);
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