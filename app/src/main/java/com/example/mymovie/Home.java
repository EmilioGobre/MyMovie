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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.controladores.WebService;
import com.example.mymovie.modelos.peliculas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;


public class Home extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;
    TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = preferences.edit();
        tvResultado = findViewById(R.id.tvResultado);
        MostarDatos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
private void MostarDatos(){
        tvResultado.setText("Bienvenido " + preferences.getString("email", "No hay datos"));
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = WebService.RAIZ + WebService.MOSTRARPELIS;
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray array = new JSONArray(response);
                        if (array.length() > 0) {
                            JSONObject obj = (JSONObject) array.get(0);
                            peliculas peli = new peliculas(
                                    obj.getString("name"),
                                    obj.getString("horario"),
                                    obj.getInt("tickets_disponibles"),
                                    obj.getDouble("precio"),
                                    obj.getString("clasificacion"),
                                    obj.getString("imagen"));
                            Toast.makeText(Home.this, "Nombre de pelicula: "+peli.name, Toast.LENGTH_LONG).show();
                        }
                        //tvResultado.setText("Response is: "+ array.length());
                    }catch (JSONException e){
                        Toast.makeText(Home.this, "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        //e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            tvResultado.setText("ERROR: "+ error.getMessage());
        }
    });
    queue.add(stringRequest);
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