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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class delete extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        toolbar = findViewById(R.id.bar);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = preferences.edit();
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