package net.charlesdelmaire.qingpool;

import java.io.InputStream;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ProfilJoueActivity extends Activity implements OnClickListener {
	private Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Appel du layout
		setContentView(R.layout.profiljoue);
		
		//Boutons, variable et OnClickListener
		getActionBar().setHomeButtonEnabled(true);
		View btnClick = findViewById(R.id.btnRetPart);		
		TextView textView = (TextView) findViewById(R.id.nomJoueur);
		TextView textView1 = (TextView) findViewById(R.id.score);
		TextView textView2 = (TextView) findViewById(R.id.equipe);
		btnClick.setOnClickListener(this);		
		Bundle b = getIntent().getExtras();
		textView.setText(textView.getText() + " " + b.getString("nom"));
		textView1.setText(textView1.getText() + " " + b.getString("point") + " Pts");
		textView2.setText(textView2.getText() + " " + b.getString("equipe"));
		
		//Google analytics tracker
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Profil Joueur");
		tracker.send(hitParameters);

		//Téléchargement de l'image des joueurs
		new DownloadImageTask((ImageView) findViewById(R.id.imgJoueur))
				.execute("http://3.cdn.nhle.com/photos/mugs/"
						+ b.getString("idJoueur") + ".jpg");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imageView1) {
			Intent intent = null;
			intent = new Intent(this, principaleActivity.class);
			intent.putExtras(b);
			this.startActivity(intent);
		}
		else
		{
			this.finish();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
	
	//Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//Retour à la page principale
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
	
	//Fonction de téléchargement
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;

		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

}
