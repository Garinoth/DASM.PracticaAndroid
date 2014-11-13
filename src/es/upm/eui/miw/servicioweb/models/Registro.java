package es.upm.eui.miw.servicioweb.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Registro {
	
	private String dni;
	private String nombre;
	private String apellidos;
	private String direccion;
	private String telefono;
	private String equipo;
	
	public Registro(JSONObject registroJSON) throws JSONException {
		this.dni = registroJSON.getString("DNI");
		this.nombre = registroJSON.getString("Nombre");
		this.apellidos = registroJSON.getString("Apellidos");
		this.direccion = registroJSON.getString("Direccion");
		this.telefono = registroJSON.getString("Telefono");
		this.equipo = registroJSON.getString("Equipo");
	}

	public Registro() {
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEquipo() {
		return equipo;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}

	public JSONObject toJSON() {
		JSONObject res = new JSONObject();
		try {
			res.put("DNI", this.dni);
			res.put("Nombre", this.nombre);
			res.put("Apellidos", this.apellidos);
			res.put("Direccion", this.direccion);
			res.put("Telefono", this.telefono);
			res.put("Equipo", this.equipo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
}
