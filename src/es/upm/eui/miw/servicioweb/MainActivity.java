package es.upm.eui.miw.servicioweb;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static int CONSULTA = 1;
	private static int INSERCION = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void consultar(View v) {
		String dni = ((EditText)findViewById(R.id.editText1)).getText().toString();
		new consultaBD().execute(dni);
	}
	
	public void insertar(View v) {
		String dni = ((EditText)findViewById(R.id.editText1)).getText().toString();
		if (dni.equals("")){
			Toast.makeText(getBaseContext(), getString(R.string.error_insercion), Toast.LENGTH_SHORT).show();
		} else {
			new insercionBD().execute(dni);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String mensaje = "Operación desconocida";
		if (requestCode == CONSULTA) {
			if (resultCode == RESULT_CANCELED) {
				mensaje = "Consulta finalizada";
			}
		}
		Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_SHORT).show();
	}

	private class consultaBD extends AsyncTask<String, Void, String> {

		private ProgressDialog pDialog;
		private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw37/fichas";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
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
				String url = URL;
				if (!arg0[0].equals("")) {
					url = URL +"/"+ arg0[0];
				}
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
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
				if (numreg == 0) {
					Toast.makeText(getBaseContext(), R.string.registroNoEncontrado, Toast.LENGTH_SHORT).show();
				} else if (numreg > 0){
					Intent nextScreen = new Intent(MainActivity.this, ConsultaActivity.class);
					nextScreen.putExtra("registros", param);
					startActivityForResult(nextScreen, CONSULTA);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//Toast.makeText(getBaseContext(), param, Toast.LENGTH_SHORT).show();
		}
	}
	
	private class insercionBD extends AsyncTask<String, Void, String> {

		private ProgressDialog pDialog;
		private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw37/fichas";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
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
				String url = URL +"/"+ arg0[0];
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
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
				if (numreg > 0) {
					Toast.makeText(getBaseContext(), R.string.registroExistente, Toast.LENGTH_SHORT).show();
				} else if (numreg == 0){
					Intent nextScreen = new Intent(MainActivity.this, InsertarActivity.class);
					nextScreen.putExtra("registro", param);
					startActivityForResult(nextScreen, INSERCION);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//Toast.makeText(getBaseContext(), param, Toast.LENGTH_SHORT).show();
		}
	}
}
