package com.db.Controlador;


import com.db.Modelos.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GestorConexion {
    public String login(String user, String password){
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", user);
            obj.put("password", password);
            return peticionWeb(Constants.ROUTE_LOGIN, obj);
        } catch (JSONException e) {
            return "" + e;
        }
    }

    public String descargarVisitas(int userId){
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", userId);
            return peticionWeb(Constants.ROUTE_VISITAS, obj);
        } catch (JSONException e) {
            return "" + e;
        }
    }

    public String peticionWeb(String urlWeb, JSONObject obj){
        String respuesta="";
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(urlWeb);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.write(obj.toString().getBytes("UTF-8"));
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuffer response = new StringBuffer();
            while((respuesta = rd.readLine()) != null) {
                response.append(respuesta);
                response.append('\r');
            }
            rd.close();
            respuesta = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        return respuesta;
    }
}
