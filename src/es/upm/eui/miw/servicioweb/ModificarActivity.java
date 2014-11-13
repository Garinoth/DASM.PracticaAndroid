package es.upm.eui.miw.servicioweb;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import es.upm.eui.miw.servicioweb.models.Registro;

public class ModificarActivity extends Activity {
	
	private Registro registro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modificar);
		
		Bundle extras = getIntent().getExtras();
		try {
			JSONArray res = new JSONArray(extras.get("registro").toString());
			this.registro = new Registro(res.getJSONObject(1));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.mostrar();
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
	
	private void mostrar() {		
		EditText dni = (EditText)findViewById(R.id.EditText05);
		EditText nombre = (EditText)findViewById(R.id.EditText04);
		EditText apellidos = (EditText)findViewById(R.id.EditText03);
		EditText direccion = (EditText)findViewById(R.id.EditText02);
		EditText telefono = (EditText)findViewById(R.id.EditText01);
		EditText equipo = (EditText)findViewById(R.id.editText1);
		dni.setText(this.registro.getDni());
		nombre.setText(this.registro.getNombre());
		apellidos.setText(this.registro.getApellidos());
		direccion.setText(this.registro.getDireccion());
		telefono.setText(this.registro.getTelefono());
		equipo.setText(this.registro.getEquipo());
	}
	
	public JSONObject recoger() {
		Registro reg = new Registro();
		EditText dni = (EditText)findViewById(R.id.EditText05);
		EditText nombre = (EditText)findViewById(R.id.EditText04);
		EditText apellidos = (EditText)findViewById(R.id.EditText03);
		EditText direccion = (EditText)findViewById(R.id.EditText02);
		EditText telefono = (EditText)findViewById(R.id.EditText01);
		EditText equipo = (EditText)findViewById(R.id.editText1);
		reg.setDni(dni.getText().toString());
		reg.setNombre(nombre.getText().toString());
		reg.setApellidos(apellidos.getText().toString());
		reg.setDireccion(direccion.getText().toString());
		reg.setTelefono(telefono.getText().toString());
		reg.setEquipo(equipo.getText().toString());
		
		return reg.toJSON();
	}
	
	public void modificar(View v) {
		String json = this.recoger().toString();
		new modificacionBD().execute(json);
	}
	
	private class modificacionBD extends AsyncTask<String, Void, String> {

		private ProgressDialog pDialog;
		private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw37/fichas";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ModificarActivity.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String respuesta = "";
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpPut httpPut = new HttpPut(URL);
				
				StringEntity se = new StringEntity(arg0[0]);
				httpPut.setEntity(se);
				httpPut.setHeader("Accept", "application/json");
	            httpPut.setHeader("Content-type", "application/json");
				
				HttpResponse response = httpclient.execute(httpPut);
				respuesta = EntityUtils.toString(response.getEntity());
				httpclient.close();
			} catch (Exception e) {
				Log.e("ServicioWeb", e.toString());
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(String param) {
			pDialog.dismiss();
			try {
				JSONArray res = new JSONArray(param);
				int numreg = res.getJSONObject(0).getInt("NUMREG");
				if (numreg == 0 || numreg == 1) {
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Intent intent = new Intent();
					setResult(RESULT_CANCELED, intent);
					finish();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//Toast.makeText(getBaseContext(), param, Toast.LENGTH_SHORT).show();
		}
	}
}
