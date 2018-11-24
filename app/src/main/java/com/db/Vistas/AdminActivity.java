package com.db.Vistas;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.db.Modelos.Constants;
import com.db.Modelos.SesionSingleton;
import com.db.R;

public class AdminActivity extends AppCompatActivity {

    EditText e_ip, e_ruta_web;
    Button b_borrar_datos, b_guardar_settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        final SharedPreferences preferencias = getSharedPreferences(Constants.CONFIGURACION,
                Context.MODE_PRIVATE);
        final SesionSingleton sesion = SesionSingleton.getInstance();
        final SharedPreferences.Editor editor = preferencias.edit();

        e_ip = findViewById(R.id.e_ip);
        e_ruta_web = findViewById(R.id.e_ruta_web);
        b_borrar_datos = findViewById(R.id.b_borrar_datos);
        b_guardar_settings = findViewById(R.id.b_guardar_settings);

        e_ip.setText(preferencias.getString(Constants.IP, ""));
        e_ruta_web.setText(preferencias.getString(Constants.RUTAWEB, ""));

        b_borrar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        b_guardar_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!preferencias.getString(Constants.IP, "").equals(e_ip.getText().toString())){
                    editor.putString(Constants.IP, e_ip.getText().toString());
                }
                if(!preferencias.getString(Constants.RUTAWEB, "").equals(e_ruta_web.getText().toString())){
                    editor.putString(Constants.RUTAWEB, e_ruta_web.getText().toString());
                }
                editor.commit();
                sesion.setIp(e_ip.getText().toString());
                sesion.setRuta(e_ruta_web.getText().toString());

                finish();
            }
        });
    }
}
