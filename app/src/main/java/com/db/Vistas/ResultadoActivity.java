package com.db.Vistas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.db.R;

public class ResultadoActivity extends AppCompatActivity {

    ImageButton ib_primero, ib_anterior, ib_siguiente, ib_ultimo;
    TextView t_pagina;
    ListView l_visitas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        ib_primero = findViewById(R.id.ib_primero);
        ib_anterior = findViewById(R.id.ib_anterior);
        ib_siguiente = findViewById(R.id.ib_siguiente);
        ib_ultimo = findViewById(R.id.ib_ultimo);
        t_pagina = findViewById(R.id.t_pagina);
        l_visitas = findViewById(R.id.l_visitas);

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
}
