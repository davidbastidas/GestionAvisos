package com.db.Controlador;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.db.Database.SQLite;
import com.db.Modelos.Constants;
import com.db.Modelos.Visitas;

import java.util.ArrayList;

public class VisitasController {

	int tamanoConsulta = 0;
	long lastInsert;
	public long getLastInsert() {
		return lastInsert;
	}

	public int getTamanoConsulta() {
		return tamanoConsulta;
	}

	public synchronized void insertar(Visitas visita, Activity activity) {
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			ContentValues registro = new ContentValues();
			registro.put("id", visita.getId());
			registro.put("tipo_visita", visita.getTipoVisita());
			registro.put("municipio", visita.getMunicipio());
			registro.put("localidad", visita.getLocalidad());
			registro.put("barrio", visita.getBarrio());
			registro.put("cliente", visita.getCliente());
			registro.put("deuda", visita.getDeuda());
			registro.put("facturas", visita.getFacturas());
			registro.put("nic", visita.getNic());
			registro.put("nis", visita.getNis());
			registro.put("medidor", visita.getMedidor());
			registro.put("tarifa", visita.getTarifa());
			registro.put("fecha_limite_compromiso", visita.getFechaLimiteCompromiso());
			registro.put("resultado", visita.getResultado());
			registro.put("anomalia", visita.getAnomalia());
			registro.put("entidad_recaudo", visita.getEntidadRecaudo());
			registro.put("fecha_pago", visita.getFechaPago());
			registro.put("fecha_compromiso", visita.getFechaCompromiso());
			registro.put("persona_contacto", visita.getPersonaContacto());
			registro.put("cedula", visita.getCedula());
			registro.put("titular_pago", visita.getTitularPago());
			registro.put("telefono", visita.getTelefono());
			registro.put("email", visita.getEmail());
			registro.put("observacion_rapida", visita.getObservacionRapida());
			registro.put("observacion_analisis", visita.getObservacionAnalisis());
			registro.put("lectura", visita.getLectura());
			registro.put("latitud", visita.getLatitud());
			registro.put("longitud", visita.getLongitud());
			registro.put("orden", 0);
			registro.put("foto", visita.getFoto());
			registro.put("fecha_realizado", visita.getFechaRealizado());
			registro.put("gestor_asignado_id", visita.getGestorAsignadoId());
			registro.put("gestor_realiza_id", 0);
			registro.put("estado", 0);
			registro.put("last_insert", 0);
			lastInsert = db.insert(Constants.TABLA_VISITAS, null, registro);
		}
	}
	public synchronized int actualizar(ContentValues registro, String where, Activity activity){
		int actualizados = 0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
	        actualizados = db.update(Constants.TABLA_VISITAS, registro, where, null);
		}
		return actualizados;
	}
	public synchronized int eliminar(String where, Activity activity){
		int registros=0;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();

		if (db != null) {
			registros=db.delete(Constants.TABLA_VISITAS, where, null);
		}
		return registros;
	}
	public synchronized void eliminarTodo(Activity activity){
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		if (db != null) {
			db.execSQL("DELETE FROM " + Constants.TABLA_VISITAS);
		}
	}
	public synchronized ArrayList<Visitas> consultar(int pagina, int limite, String condicion, Activity activity){
		Visitas dataSet;
		ArrayList<Visitas> visitas = new ArrayList<Visitas>();
		Cursor c = null, countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String limit = "";
		if(limite != 0){
			limit = " LIMIT " + pagina + "," + limite;
		}
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE " + condicion;
		}
		c = db.rawQuery("SELECT * FROM " + Constants.TABLA_VISITAS + " " + where+" ORDER BY id DESC "+limit, null);
		countCursor = db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_VISITAS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		if (c.moveToFirst()) {
			do {
				dataSet = new Visitas();
				dataSet.setId(c.getLong(0));
				dataSet.setTipoVisita(c.getString(1));
				dataSet.setMunicipio(c.getString(2));
				dataSet.setLocalidad(c.getString(3));
				dataSet.setBarrio(c.getString(4));
				dataSet.setCliente(c.getString(5));
				dataSet.setDeuda(c.getLong(6));
				dataSet.setFacturas(c.getLong(7));
				dataSet.setNic(c.getLong(8));
				dataSet.setNis(c.getLong(9));
				dataSet.setMedidor(c.getString(10));
				dataSet.setTarifa(c.getString(11));
				dataSet.setFechaLimiteCompromiso(c.getString(12));
				dataSet.setResultado(c.getLong(13));
				dataSet.setAnomalia(c.getLong(14));
				dataSet.setEntidadRecaudo(c.getLong(15));
				dataSet.setFechaPago(c.getString(16));
				dataSet.setFechaCompromiso(c.getString(17));
				dataSet.setPersonaContacto(c.getString(18));
				dataSet.setCedula(c.getString(19));
				dataSet.setTitularPago(c.getString(20));
				dataSet.setTelefono(c.getString(21));
				dataSet.setEmail(c.getString(22));
				dataSet.setObservacionRapida(c.getString(23));
				dataSet.setObservacionAnalisis(c.getString(24));
				dataSet.setLectura(c.getString(25));
				dataSet.setLatitud(c.getString(26));
				dataSet.setLongitud(c.getString(27));
				dataSet.setOrden(c.getLong(28));
				dataSet.setFoto(c.getString(29));
				dataSet.setFechaRealizado(c.getString(30));
				dataSet.setGestorAsignadoId(c.getLong(31));
				dataSet.setGestorRealizaId(c.getLong(32));
				dataSet.setEstado(c.getLong(33));
				dataSet.setLastInsert(c.getLong(34));
				visitas.add(dataSet);
			} while (c.moveToNext());
		}
		c.close();
		countCursor.close();
		return visitas;
	}
	public synchronized int count(String condicion, Activity activity){
		Cursor countCursor = null;
		SQLite usdbh = SQLite.getInstance(activity);
		SQLiteDatabase db = usdbh.getMyWritableDatabase();
		String where = "";
		if(!condicion.equals("")){
			where = " WHERE "+condicion;
		}
		countCursor=db.rawQuery("SELECT count(id) FROM " + Constants.TABLA_VISITAS + " " + where, null);
		if (countCursor.moveToFirst()) {
			do {
				tamanoConsulta = countCursor.getInt(0);
			} while (countCursor.moveToNext());
		}
		countCursor.close();
		return tamanoConsulta;
	}
}
