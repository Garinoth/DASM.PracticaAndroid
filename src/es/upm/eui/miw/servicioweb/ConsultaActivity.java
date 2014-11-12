package es.upm.eui.miw.servicioweb;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import es.upm.eui.miw.servicioweb.models.Registro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ConsultaActivity extends Activity {
	
	private ArrayList<Registro> registros;
	private int registroActual = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta);
		
		Bundle extras = getIntent().getExtras();
		try {
			JSONArray res = new JSONArray(extras.get("registros").toString());
			this.registros = new ArrayList<Registro>();
			Registro aux;
			for (int i = 1; i <= res.getJSONObject(0).getInt("NUMREG"); i++) {
				aux = new Registro(res.getJSONObject(i));
				this.registros.add(aux);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		this.mostrarActual();
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
	
	private void mostrarActual() {
		TextView actual = (TextView)findViewById(R.id.textView3);
		actual.setText("Registro "+(this.registroActual +1)+" de "+this.registros.size());
		
		EditText dni = (EditText)findViewById(R.id.EditText05);
		EditText nombre = (EditText)findViewById(R.id.EditText04);
		EditText apellidos = (EditText)findViewById(R.id.EditText03);
		EditText direccion = (EditText)findViewById(R.id.EditText02);
		EditText telefono = (EditText)findViewById(R.id.EditText01);
		EditText equipo = (EditText)findViewById(R.id.editText1);
		dni.setText(this.registros.get(registroActual).getDni());
		nombre.setText(this.registros.get(registroActual).getNombre());
		apellidos.setText(this.registros.get(registroActual).getApellidos());
		direccion.setText(this.registros.get(registroActual).getDireccion());
		telefono.setText(this.registros.get(registroActual).getTelefono());
		equipo.setText(this.registros.get(registroActual).getEquipo());
	}
	
	public void previous(View v) {
		if (this.registroActual > 0) {
			this.registroActual--;
		}
		this.mostrarActual();
	}
	
	public void next(View v) {
		if (this.registroActual < this.registros.size()-1) {
			this.registroActual++;
		}
		this.mostrarActual();
	}
	
	public void first(View v) {
		this.registroActual = 0;
		this.mostrarActual();
	}
	
	public void last(View v) {
		this.registroActual = this.registros.size()-1;
		this.mostrarActual();
	}
}
