package com.db.Modelos;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.File;

public final class Constants {

    private static SesionSingleton sesion = SesionSingleton.getInstance();

    public static final int PERMISOS_REQUEST_CODE = 100;

    public static final String DB_NAME = "db_gestion";
    public static final String TABLA_USUARIOS = "usuarios";
    public static final String TABLA_VISITAS = "visitas";
    public static final String TABLA_RESULTADOS = "resultados";
    public static final String TABLA_ANOMALIAS = "anomalias";
    public static final String TABLA_ENTIDADES_PAGO = "entidades_pago";
    public static final String TABLA_OBSERVACION_RAPIDA = "observacion_rapida";

    /** nombre de la configuracion base*/
    public static final String CONFIGURACION = "configuracion";
    public static final String PASSWORDADMIN = "passwordAdmin";
    public static final String PASSWORDSERVICIOS = "passwordServicios";
    public static final String ESTADODATOS = "estado_datos";
    public static final String ESTADOENVIO = "estado_envio";
    public static final String IP = "ip";
    public static final String RUTAWEB = "ruta";

    /** url*/
    //ip/control/api/public/--ruta de el controlador--/
    private static String URL_BASE = "http://" + sesion.getIp() + File.separator + sesion.getRuta() + File.separator +  "api" + File.separator +  "public" + File.separator;
    public static final String ROUTE_LOGIN = URL_BASE + "site/login";
    public static final String ROUTE_VISITAS = URL_BASE + "site/login";

    public static ProgressDialog dialogIndeterminate(Context context, String mensaje){
        ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Procesando...");
        pd.setMessage(mensaje);
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        return pd;
    }

    /** mensajes estandar*/
    public static final String MSG_FORMATO_NO_VALIDO = "El formato de respuesta no es valido.";
    public static final String MSG_PETICION_RECHAZADA = "Peticion Rechazada.";
    public static final String MSG_LEYENDO_DATOS = "Ocurrio un error leyendo los datos ";
}
