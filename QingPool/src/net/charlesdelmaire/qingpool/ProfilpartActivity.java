package net.charlesdelmaire.qingpool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ProfilpartActivity extends Activity {

	private ProgressDialog m_ProgressDialog;
	List<Map<String, String>> partList = new ArrayList<Map<String, String>>();
	SimpleAdapter simpleAdpt;
	String lesJoueurs;
	TextView txtScorePart;
	TextView txtNomPart;
	TextView txtEmpty;
	ListView listJoueur;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.profilpart);
		listJoueur = (ListView) findViewById(R.id.list);

		txtScorePart = (TextView) findViewById(R.id.scorePart);
		txtNomPart = (TextView) findViewById(R.id.nomPart);
		txtEmpty = (TextView) findViewById(R.id.empty);

		Bundle b = getIntent().getExtras();
		txtNomPart.setText(txtNomPart.getText() + " " + b.getString("nom"));
		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomJoueur" }, new int[] { android.R.id.text1 });
		listJoueur.setAdapter(simpleAdpt);

		registerForContextMenu(listJoueur);
		// call AsynTask to perform network operation on separate thread
		new HttpAsyncTask()
				.execute("http://charlesdelmaire1992.appspot.com/joueur?nom=");

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	// Fonction appelé Async
	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		String result2 = "";
		try {
			List<String> listJoueur = new ArrayList<String>();
			ArrayList<String> liste = null;

			// Liste des joueurs à aller chercher sur le web service
			listJoueur.add("Sidney%20Crosby");
			listJoueur.add("Claude%20Giroux");

			// Passe les joueurs de la liste
			for (Iterator<String> i = listJoueur.iterator(); i.hasNext();) {
				String item = i.next();

				// create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// make GET request to the given URL
				HttpResponse httpResponse = httpclient.execute(new HttpGet(url
						.concat(item)));

				// receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();

				// convert inputstream to string
				if (inputStream != null)
					result2 = convertInputStreamToString(inputStream);
				else
					result2 = "Did not work!";

				// result += result2;
				liste = JsonParser.unePersonne(result2);
				result += liste.get(0) + ";";

			}

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	// Verifie la connexion internet
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo aInfo = (AdapterContextMenuInfo) menuInfo;

		// We know that each row in the adapter is a Map
		HashMap map = (HashMap) simpleAdpt.getItem(aInfo.position);
		Bundle b = new Bundle();
		Intent intent = new Intent(this, ProfilJoueActivity.class);
		String str[] = lesJoueurs.split(";");

		for (int i = 0; i < str.length; i++) {
			String str1[] = str[i].split("/");

			if (str1[0].equals(map.get("nomJoueur"))) {
				b.putString("nom", str1[0]);
				b.putString("equipe", str1[1]);
				b.putString("point", str1[2]);
			}
		}

		intent.putExtras(b);
		// start the second Activity
		this.startActivity(intent);
	}

	// Gestion de appel au web service
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {

			return GET(urls[0]);
		}

		// Gestion du resultat de appel au web service
		@Override
		protected void onPostExecute(String result) {
			if (m_ProgressDialog != null) {
				m_ProgressDialog.dismiss();
			}
			lesJoueurs = result;
			String str[] = result.split(";");
			int score = 0;
			for (int i = 0; i < str.length; i++) {
				String str1[] = str[i].split("/");
				score += Integer.parseInt(str1[2]);
				partList.add(createJoueur("nomJoueur", str1[0]));
			}
			txtScorePart.setText(txtScorePart.getText() + " "
					+ Integer.toString(score));
			simpleAdpt.notifyDataSetChanged();
			txtEmpty.setText("");

		}

		private HashMap<String, String> createJoueur(String key, String name) {
			HashMap<String, String> Pool = new HashMap<String, String>();
			Pool.put(key, name);
			return Pool;
		}

		@Override
		protected void onPreExecute() {
			// Gestion du spinner
			if (m_ProgressDialog == null) {
				m_ProgressDialog = new ProgressDialog(ProfilpartActivity.this);
				m_ProgressDialog.setTitle(R.string.pd_title);
				m_ProgressDialog.setMessage(ProfilpartActivity.this
						.getString(R.string.pd_content));
				m_ProgressDialog.setCancelable(false);
				m_ProgressDialog.setIndeterminate(true);
			}
			m_ProgressDialog.show();
		}
	}

}
