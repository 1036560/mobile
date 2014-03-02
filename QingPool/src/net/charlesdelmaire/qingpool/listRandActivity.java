package net.charlesdelmaire.qingpool;

import java.net.URI;
import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class listRandActivity extends ListActivity implements OnClickListener {
	private final String TAG = this.getClass().getSimpleName();

	// Version hébergée :
	private final static String WEB_SERVICE_URL = "charlesdelmaire1992.appspot.com";

	private HttpClient m_ClientHttp = new DefaultHttpClient();
	private ProgressDialog m_ProgressDialog;
	ArrayList<String> m_Personnes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listrand);

		View btnClick = findViewById(R.id.btnRege);
		btnClick.setOnClickListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Chargement asynchrone de la liste des personnes.
		new DownloadPersonListTask().execute((Void) null);
	}

	private class DownloadPersonListTask extends
			AsyncTask<Void, Void, ArrayList<String>> {
		Exception m_Exp;

		@Override
		protected void onPreExecute() {
			if (m_ProgressDialog == null) {
				m_ProgressDialog = new ProgressDialog(listRandActivity.this);
				m_ProgressDialog.setTitle(R.string.pd_title);
				m_ProgressDialog.setMessage(listRandActivity.this
						.getString(R.string.pd_content));
				m_ProgressDialog.setCancelable(false);
				m_ProgressDialog.setIndeterminate(true);
			}
			m_ProgressDialog.show();
		}

		@Override
		protected ArrayList<String> doInBackground(Void... unused) {
			ArrayList<String> liste = null;

			try {

				URI uri = new URI("http", WEB_SERVICE_URL, "/aleatoire", null,
						null);
				HttpGet getMethod = new HttpGet(uri);

				String body = m_ClientHttp.execute(getMethod,
						new BasicResponseHandler());
				Log.i(TAG, "Reçu : " + body);

				liste = JsonParser.parseListePersonne(body);
			} catch (Exception e) {
				m_Exp = e;
			}
			return liste;
		}

		@Override
		protected void onPostExecute(ArrayList<String> p_Personnes) {
			if (m_ProgressDialog != null) {
				m_ProgressDialog.dismiss();
			}

			if (m_Exp == null && p_Personnes != null) {
				m_Personnes.clear();
				m_Personnes.addAll(p_Personnes);
				listRandActivity.this.setListAdapter(new ArrayAdapter<String>(
						listRandActivity.this,
						android.R.layout.simple_list_item_1, m_Personnes));

			} else {
				Log.e(TAG, "Error while fetching", m_Exp);
				Toast.makeText(listRandActivity.this,
						getString(R.string.comm_error), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.btnRege) {
			new DownloadPersonListTask().execute((Void) null);
		}
	}

}
