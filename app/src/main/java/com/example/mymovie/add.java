package com.example.mymovie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymovie.controladores.WebService;
import com.example.mymovie.modelos.peliculas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class add extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    EditText etNombre,etHorario,etTickets,etPrecio;
    ImageView ivImagen;
    Button btnGuardar,btnSeleccionar;
    ImageButton btnFecha;
    Spinner spClasificacion;

    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String url= WebService.RAIZ+WebService.INSETARPELIS;

    String KEY_IMAGE = "imagen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        etNombre = findViewById(R.id.etnombre_add);
        etHorario = findViewById(R.id.ethorario_add);
        etTickets = findViewById(R.id.ettickets_add);
        etPrecio = findViewById(R.id.etprecio_add);
        spClasificacion = findViewById(R.id.spclasificacion_add);
        ivImagen = findViewById(R.id.Imagen_add);
        btnGuardar = findViewById(R.id.button_add);
        btnSeleccionar = findViewById(R.id.btnselect_add);
        btnFecha = findViewById(R.id.btnfecha_add);

        toolbar = findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = preferences.edit();

        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {InsertarPelicula();}
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(add.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etHorario.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        String [] clasificacion = {"AA","A","B","B15","C","D"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,clasificacion);
        spClasificacion.setAdapter(adapter);
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }


    public void InsertarPelicula(){
        final ProgressDialog loading = ProgressDialog.show(this,"Subiendo...","Espere por favor...");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                Limpiar();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"ERROR: "+error.getMessage(),Toast.LENGTH_LONG).show();
                Limpiar();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String name = etNombre.getText().toString().trim();
                String horario = etHorario.getText().toString().trim();
                double tickets = Double.parseDouble(etTickets.getText().toString().trim());
                double precio = Double.parseDouble(etPrecio.getText().toString().trim());
                String clasificacion = spClasificacion.getSelectedItem().toString();
                String imagen = getStringImage(bitmap);

                peliculas peli= new peliculas(name,horario,tickets,precio,clasificacion,imagen);

                Map<String,String> params = new HashMap<>();
                params.put("name",peli.getname());
                params.put("horario",peli.gethorario());
                params.put("tickets_disponibles",String.valueOf(peli.gettickets_disponibles()));
                params.put("precio",String.valueOf(peli.getprecio()));
                params.put("clasificacion",peli.getclasificacion());
                params.put(KEY_IMAGE,imagen);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filepath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                ivImagen.setImageBitmap(bitmap);
            }catch (IOException e){
                Toast.makeText(getApplicationContext(), "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
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

    public void Limpiar(){
        etNombre.setText("");
        etHorario.setText("");
        etTickets.setText("");
        etPrecio.setText("");
        ivImagen.setImageResource(R.drawable.no_photo);
    }
}