package com.db.Vistas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.db.Controlador.VisitasController;
import com.db.Modelos.Constants;
import com.db.Modelos.VisitaSesion;
import com.db.Modelos.Visitas;
import com.db.R;

import java.util.ArrayList;

public class BuscarActivity extends AppCompatActivity {

    Button b_ir_buscar;
    EditText e_nic, e_medidor, e_direccion;
    RadioButton rb_pendientes, rb_realizados;
    VisitasController vis = null;
    boolean realizados = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        setTitle("Busqueda de visita");
        e_nic = findViewById(R.id.e_nic);
        e_medidor = findViewById(R.id.e_medidor);
        e_direccion = findViewById(R.id.e_direccion);
        rb_pendientes = findViewById(R.id.rb_pendientes);
        rb_realizados = findViewById(R.id.rb_realizados);
        b_ir_buscar = findViewById(R.id.b_ir_buscar);
        rb_realizados.setChecked(true);
        rb_pendientes.setChecked(false);

        b_ir_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!e_nic.getText().toString().equals("") || !e_medidor.getText().toString().equals("") || !e_direccion.getText().toString().equals("")) {
                    Intent intentar = new Intent(BuscarActivity.this, VisitasActivity.class);
                    intentar.putExtra(Constants.EXTRA_NIC, e_nic.getText().toString());
                    intentar.putExtra(Constants.EXTRA_MEDIDOR, e_medidor.getText().toString());
                    intentar.putExtra(Constants.EXTRA_DIRECCION, e_direccion.getText().toString());
                    intentar.putExtra(Constants.EXTRA_REALIZADO, realizados);
                    startActivityForResult(intentar, Constants.VISITA_REQUEST_CODE);
                } else {
                    Toast.makeText(BuscarActivity.this, "Debe ingresar la visita a buscar", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rb_pendientes:
                if (checked)
                    realizados = false;
                break;
            case R.id.rb_realizados:
                if (checked)
                    realizados = true;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.VISITA_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }
}
