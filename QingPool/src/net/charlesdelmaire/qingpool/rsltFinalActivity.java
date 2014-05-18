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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class rsltFinalActivity extends Activity {
	
	//Variables
	private Bundle b;
	private List<Participant> lstPart;
	List<Map<String, String>> partList;
	private QingPoolDatasource bd;
	String lesJoueurs;
	SimpleAdapter simpleAdpt;
	private List<String> liste;
	ListView lv;
	private List<JoueurPool> listJoueurs;
	List<String> listJoueur = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Appel du layout
		setContentView(R.layout.rsltfinal);
		
		//Récupération des views
		lv = (ListView) findViewById(R.id.list);
		liste = new ArrayList<String>();
		partList = new ArrayList<Map<String, String>>();
		b = getIntent().getExtras();
		getActionBar().setHomeButtonEnabled(true);
		lstPart = bd.getTousPart(b.getInt("idPoolSelect"));
		
		//Connexion à la BD
		this.bd = new QingPoolDatasource(this);
		this.bd.open();		

		initList();
		
		//Adaptateur
		simpleAdpt = new SimpleAdapter(this, partList,
				android.R.layout.simple_list_item_1,
				new String[] { "nomPart" }, new int[] { android.R.id.text1 });
		lv.setAdapter(simpleAdpt);

		//Google analytics tracker
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_rslt_pool));
		tracker.send(hitParameters);
	}

	//Menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, principaleActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	//Initialisation de laliste
	private void initList() {
		for (int i = 0; i < lstPart.size(); i++) {			
			liste.add(lstPart.get(i).getNomPart());
		}
	}

	//Passage de variables en sortie
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("listJoueur", lesJoueurs);
	}
	
	//Méthode asynchrone
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			return GET(urls[0]);
		}
		
		//Affichage de la liste et triage en ordre croissant de score
		@Override
		protected void onPreExecute() {
			listJoueurs = bd.getTousJoueurs(b.getInt("idPartSelect"),
					b.getInt("idPoolSelect"));
	
			for (int i = 0; i < listJoueurs.size(); i++) {
				listJoueur
						.add(listJoueurs.get(i).nomJoueur.replace(" ", "%20"));
			}
		}
	}
	
	//Méthode GET pour liste des joueurs à partir du Web Service
		public String GET(String url) {
			InputStream inputStream = null;
			String result = "";
			String result2 = "";
			try {
				ArrayList<String> liste = null;

				//Parcours de la liste
				for (Iterator<String> i = listJoueur.iterator(); i.hasNext();) {
					String item = i.next();

					//Création du client HTTP
					HttpClient httpclient = new DefaultHttpClient();

					//Envoie de la requête
					HttpResponse httpResponse = httpclient.execute(new HttpGet(url
							.concat(item)));

					//Réception de la réponse
					inputStream = httpResponse.getEntity().getContent();

					//Conversion en chaîne de caractères
					if (inputStream != null)
						result2 = convertInputStreamToString(inputStream);
					else
						result2 = "Did not work!";

					//Addition des résultats
					liste = JsonParser.unePersonne(result2);
					result += liste.get(0) + ";";
				}
			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}
			return result;
		}

		//Méthode de conversion du InputStream
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
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}
