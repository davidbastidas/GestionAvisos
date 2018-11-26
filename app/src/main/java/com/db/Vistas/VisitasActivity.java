package com.db.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.db.Controlador.VisitasController;
import com.db.Modelos.Constants;
import com.db.Modelos.VisitaSesion;
import com.db.Modelos.Visitas;
import com.db.R;
import com.db.Vistas.Adaptader.AdapterVisitas;

import java.util.ArrayList;

public class VisitasActivity extends AppCompatActivity {

    ImageButton ib_primero, ib_anterior, ib_siguiente, ib_ultimo;
    TextView t_pagina;
    ListView l_visitas;
    VisitasController vis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas);
        setTitle("Lista de Visitas");

        ib_primero = findViewById(R.id.ib_primero);
        ib_anterior = findViewById(R.id.ib_anterior);
        ib_siguiente = findViewById(R.id.ib_siguiente);
        ib_ultimo = findViewById(R.id.ib_ultimo);
        t_pagina = findViewById(R.id.t_pagina);
        l_visitas = findViewById(R.id.l_visitas);

        cargarLista();
        l_visitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                try {
                    Visitas visita = (Visitas) parent.getItemAtPosition(position);
                    VisitaSesion.getInstance().setId(visita.getId());
                    Intent intentar = new Intent(VisitasActivity.this, DetalleActivity.class);
                    intentar.putExtra(Constants.EXTRA_VISITA_ID, visita.getId());
                    startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
                } catch (Exception ex) {
                    Toast.makeText(VisitasActivity.this, "Error en la visita: " + ex, Toast.LENGTH_LONG).show();
                }
            }
        });

        ib_primero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ib_anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ib_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ib_ultimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void cargarLista(){
        vis = new VisitasController();
        ArrayList<Visitas> visitas = vis.consultar(0, 15, "estado=0", this);
        AdapterVisitas adapter = new AdapterVisitas(this, visitas);
        l_visitas.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarLista();
    }
}
