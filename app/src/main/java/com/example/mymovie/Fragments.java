package com.example.mymovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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


public class Fragments extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);
        toolbar = findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fragments_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.casa:
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
                break;
            case R.id.Version:
                addFragment(new Version(), false, "1");
                return true;
            case R.id.Dev:
                addFragment(new Develop(), false, "2");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if(addToBackStack){
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.fragments, fragment, tag);
        ft.commitAllowingStateLoss();
    }
}