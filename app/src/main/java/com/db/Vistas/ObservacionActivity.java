package com.db.Vistas;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.db.Controlador.GestorConexion;
import com.db.Controlador.ObservacionRapidaController;
import com.db.Controlador.VisitasController;
import com.db.Modelos.Constants;
import com.db.Modelos.ObservacionRapida;
import com.db.Modelos.SesionSingleton;
import com.db.Modelos.VisitaSesion;
import com.db.Modelos.Visitas;
import com.db.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ObservacionActivity extends AppCompatActivity implements DialogoGPS.OnGPSIntent {

    Button b_foto, b_finalizar;
    Spinner s_observacion;
    EditText e_observacion;
    ObservacionRapida obsElegida;
    ProgressDialog progressDialog = null;
    Visitas visitaEnviar = null;

    /** OPCIONES DEL GPS*/
    private ObservacionActivity listenerGps;
    LocationManager locManager;
    private boolean guardadoActivo = true, pasarConPuntoelegido = false;
    LocationListener locListener;
    private int ESTADO_SERVICE = 0;
    private static final int OUT_OF_SERVICE = 0;
    private static final int TEMPORARILY_UNAVAILABLE = 1;
    private static final int AVAILABLE = 2;
    private String LONGITUD = "0.0", LATITUD = "0.0", ACURRACY = "0";
    private String LONGITUD_FINAL = "0.0", LATITUD_FINAL = "0.0", ACURRACY_FINAL = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observacion);
        setTitle("Observacion y Otros");

        listenerGps = this;
        comenzarLocalizacion();
        b_foto = findViewById(R.id.b_foto);
        b_finalizar = findViewById(R.id.b_finalizar);
        s_observacion = findViewById(R.id.s_observacion);
        e_observacion = findViewById(R.id.e_observacion);
        if(VisitaSesion.getInstance().getObservacionAnalisis() != null){
            e_observacion.setText(VisitaSesion.getInstance().getObservacionAnalisis());
        }

        ObservacionRapidaController obs = new ObservacionRapidaController();
        ArrayList<ObservacionRapida> observaciones = obs.consultar(0, 0, "", this);
        ArrayAdapter<ObservacionRapida> obsAdapter = new ArrayAdapter<ObservacionRapida>(this,
                android.R.layout.simple_spinner_item, observaciones);
        obsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s_observacion.setAdapter(obsAdapter);
        if(VisitaSesion.getInstance().getObservacionRapida() != 0){
            ArrayList<ObservacionRapida> obs2 = obs.consultar(0, 0, "id=" + VisitaSesion.getInstance().getObservacionRapida(), this);
            s_observacion.setSelection(getIndex(s_observacion, obs2.get(0).getNombre()));
        }else{
            ObservacionRapida ob = new ObservacionRapida();
            ob.setId(4);//sin observacion en la Base de datos
            s_observacion.setSelection(getIndex(s_observacion, "Sin observación"));
        }

        s_observacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                obsElegida = (ObservacionRapida) parentView.getItemAtPosition(position);
                VisitaSesion.getInstance().setObservacionRapida(obsElegida.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                obsElegida = null;
            }
        });

        b_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        b_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(VisitaSesion.getInstance().getFoto() != null){
                VisitaSesion.getInstance().setObservacionAnalisis(e_observacion.getText().toString());
                validarGuardarGps();
            } else{
                Toast.makeText(ObservacionActivity.this, "Debe tomar una foto para soporte.", Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void guardarVisita() {
        VisitasController vis = new VisitasController();
        int orden = vis.ultimoOrden(this);
        orden = orden + 1;

        VisitaSesion sesion = VisitaSesion.getInstance();
        visitaEnviar = new Visitas();
        visitaEnviar.setId(sesion.getId());
        visitaEnviar.setResultado(sesion.getResultado());
        visitaEnviar.setAnomalia(sesion.getAnomalia());
        visitaEnviar.setEntidadRecaudo(sesion.getEntidadRecaudo());
        visitaEnviar.setFechaPago(sesion.getFechaPago());
        visitaEnviar.setFechaCompromiso(sesion.getFechaCompromiso());
        visitaEnviar.setPersonaContacto(sesion.getPersonaContacto());
        visitaEnviar.setCedula(sesion.getCedula());
        visitaEnviar.setTitularPago(sesion.getTitularPago());
        visitaEnviar.setTelefono(sesion.getTelefono());
        visitaEnviar.setEmail(sesion.getEmail());
        visitaEnviar.setObservacionRapida(sesion.getObservacionRapida());
        visitaEnviar.setLectura(sesion.getLectura());
        visitaEnviar.setObservacionAnalisis(sesion.getObservacionAnalisis());
        visitaEnviar.setLatitud(LATITUD_FINAL);
        visitaEnviar.setLongitud(LONGITUD_FINAL);
        visitaEnviar.setOrden(sesion.getOrden());
        visitaEnviar.setFoto(sesion.getFoto());

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());

        ContentValues registro = new ContentValues();
        registro.put("resultado", visitaEnviar.getResultado());
        registro.put("anomalia", visitaEnviar.getAnomalia());
        registro.put("entidad_recaudo", visitaEnviar.getEntidadRecaudo());
        registro.put("fecha_pago", visitaEnviar.getFechaPago());
        registro.put("fecha_compromiso", visitaEnviar.getFechaCompromiso());
        registro.put("persona_contacto", visitaEnviar.getPersonaContacto());
        registro.put("cedula", visitaEnviar.getCedula());
        registro.put("titular_pago", visitaEnviar.getTitularPago());
        registro.put("telefono", visitaEnviar.getTelefono());
        registro.put("email", visitaEnviar.getEmail());
        registro.put("observacion_rapida", visitaEnviar.getObservacionRapida());
        registro.put("observacion_analisis", visitaEnviar.getObservacionAnalisis());
        registro.put("lectura", visitaEnviar.getLectura());
        registro.put("latitud", visitaEnviar.getLatitud());
        registro.put("longitud", visitaEnviar.getLongitud());
        registro.put("orden", orden);
        registro.put("foto", visitaEnviar.getFoto());
        registro.put("fecha_realizado", date);
        registro.put("estado", 1);
        registro.put("gestor_realiza_id", SesionSingleton.getInstance().getFkId());
        vis.actualizar(registro, "id = " + VisitaSesion.getInstance().getId(), this);

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void validarGuardarGps() {
        if(pasarConPuntoelegido){
            if (guardadoActivo){
                guardadoActivo = false;
                pasarConPuntoelegido = false;
                guardarVisita();
            }
        } else {
            DialogoGPS dgps = new DialogoGPS(listenerGps, LATITUD, LONGITUD, ACURRACY, this);
            dgps.showMyDialog().show();
        }
    }

    private void takePhoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.FOTO_REQUEST_CODE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Constants.FOTO_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK){
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    VisitaSesion.getInstance().setFoto(BitMapToString(bmp));
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        VisitaSesion.getInstance().setObservacionAnalisis(e_observacion.getText().toString());
        if(s_observacion.getSelectedItem() != null){
            VisitaSesion.getInstance().setObservacionRapida(((ObservacionRapida) s_observacion.getSelectedItem()).getId());
        }
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void onStop () {
        if (locListener != null) {
            locManager.removeUpdates(locListener);
            locListener = null;
            locManager = null;
        }
        super.onStop();
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,60, baos);
        byte [] b = baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void comenzarLocalizacion() {
        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    System.out.println("onLocationChanged");
                    LATITUD = String.valueOf(location.getLatitude());
                    LONGITUD = String.valueOf(location.getLongitude());
                    ACURRACY = String.valueOf(location.getAccuracy());
                }

                @Override
                public void onProviderDisabled(String provider) {
                    locManager.removeUpdates(locListener);
                    System.out.println("onProviderDisabled");
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    ESTADO_SERVICE = status;
                    mostrarEstadoGPS();
                }
            };
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            //iniciarTareaGPS30Sec();
        } else {
            ActivarGPS();
        }
    }

    private void mostrarEstadoGPS(){
        if(ESTADO_SERVICE==OUT_OF_SERVICE){
            Toast.makeText(this, "Servicio GPS no disponible", Toast.LENGTH_LONG).show();
            System.out.println("OUT_OF_SERVICE");
        } else if(ESTADO_SERVICE==TEMPORARILY_UNAVAILABLE){
            System.out.println("TEMPORARILY_UNAVAILABLE");
            Toast.makeText(this, "Servicio GPS no disponible", Toast.LENGTH_LONG).show();
        } else if(ESTADO_SERVICE==AVAILABLE){
            System.out.println("AVAILABLE");
            Toast.makeText(this, "GPS Disponible", Toast.LENGTH_LONG).show();
        }
    }

    private void ActivarGPS() {
        Toast.makeText(this, "Por favor active su GPS", Toast.LENGTH_SHORT)
                .show();
        Intent settingsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        this.startActivityForResult(settingsIntent, 0);
    }

    @Override
    public void onGuardarConPuntoElegido(String latitud,String longitud,String acurracy) {
        pasarConPuntoelegido = true;
        LONGITUD_FINAL = longitud;
        LATITUD_FINAL = latitud;
        ACURRACY_FINAL = acurracy;
        validarGuardarGps();
    }

    @Override
    public void onSeguirIntentandoGPS() {
        pasarConPuntoelegido = false;
        validarGuardarGps();
    }
}