package net.charlesdelmaire.qingpool;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusClient;

public class connexionActivity extends Activity implements OnClickListener,
		PlusClient.ConnectionCallbacks, PlusClient.OnConnectionFailedListener,
		PlusClient.OnAccessRevokedListener {

	// Variables
	private QingPoolDatasource bd;
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

	private TextView mSignInStatus;
	private PlusClient mPlusClient;
	private View mSignInButton;
	private View mSignOutButton;
	private int idduPart;
	private ConnectionResult mConnectionResult;
	Bundle b;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Appel du layout
		setContentView(R.layout.connexion);

		// Connexion � la BD
		this.bd = new QingPoolDatasource(this);
		this.bd.open();

		if (bd.verifPart("Demo QingPool") != 0) {

			bd.createPart(new Participant(
					0,
					"Demo QingPool",
					"https://lh6.googleusercontent.com/-JGkWvxm5Ku4/AAAAAAAAAAI/AAAAAAAAAnA/DLk299TROxc/photo.jpg?sz=50",
					"fr"));
			bd.createPart(new Participant(
					1,
					"Charles Delmaire",
					"https://lh6.googleusercontent.com/-JGkWvxm5Ku4/AAAAAAAAAAI/AAAAAAAAAnA/DLk299TROxc/photo.jpg?sz=50",
					"fr"));
			bd.createPart(new Participant(
					2,
					"Olivier Plante",
					"https://lh6.googleusercontent.com/-JGkWvxm5Ku4/AAAAAAAAAAI/AAAAAAAAAnA/DLk299TROxc/photo.jpg?sz=50",
					"fr"));

			bd.createPool(new Pool(0, "test", 0, "test", 10));
			JoueurPool unjoueur = new JoueurPool();
			unjoueur.setIdJoueur1("8471675");
			unjoueur.setIdPart(0);
			unjoueur.setIdPool(0);
			unjoueur.setNomJoueur("Sidney Crosby");
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);

			PartScore score = new PartScore();
			score.setIdPart(0);
			score.setIdPool(0);
			score.setScore(1040);
			bd.createPartScore(score);

			unjoueur.setIdJoueur1("8470612");
			unjoueur.setIdPart(1);
			unjoueur.setIdPool(0);
			unjoueur.setNomJoueur("Ryan Getzlaf");
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);

			score = new PartScore();
			score.setIdPart(1);
			score.setIdPool(0);
			score.setScore(870);
			bd.createPartScore(score);

			unjoueur.setIdJoueur1("8468598");
			unjoueur.setIdPart(2);
			unjoueur.setIdPool(0);
			unjoueur.setNomJoueur("Lubomir Visnovsky");
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);
			bd.createJoueur(unjoueur);

			score = new PartScore();
			score.setIdPart(2);
			score.setIdPool(0);
			score.setScore(110);
			bd.createPartScore(score);
		}

		// Bouton et OnClickListener
		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				MomentUtil.ACTIONS).build();
		mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
		mSignInButton = findViewById(R.id.sign_in_button);
		mSignInButton.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_shape));
		mSignInButton.setOnClickListener(this);

		// Google analytics tracker
		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");
		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME,
				getString(R.string.screen_connexion));
		tracker.send(hitParameters);
		b = getIntent().getExtras();
	}

	@Override
	public void onStart() {
		super.onStart();
		bd.open();
		mPlusClient.connect();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		mPlusClient.disconnect();
		bd.close();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {

			// Connexion � Google
			int available = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(this);
			if (available != ConnectionResult.SUCCESS) {
				showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
				return;
			}

			try {
				mSignInStatus.setText(getString(R.string.signing_in_status));
				mConnectionResult.startResolutionForResult(this,
						REQUEST_CODE_SIGN_IN);
			} catch (IntentSender.SendIntentException e) {
				mPlusClient.connect();
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		// Messages pour la connexion
		if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
			return super.onCreateDialog(id);
		}

		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (available == ConnectionResult.SUCCESS) {
			return null;
		}
		if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
			return GooglePlayServicesUtil.getErrorDialog(available, this,
					REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
		}
		return new AlertDialog.Builder(this)
				.setMessage(R.string.plus_generic_error).setCancelable(true)
				.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Messages interactifs
		if (requestCode == REQUEST_CODE_SIGN_IN
				|| requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
			if (resultCode == RESULT_OK && !mPlusClient.isConnected()
					&& !mPlusClient.isConnecting()) {
				mPlusClient.connect();
			}
		}
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {

		// Message sur les acc�s
		if (status.isSuccess()) {
			mSignInStatus.setText(R.string.revoke_access_status);
		} else {
			mSignInStatus.setText(R.string.revoke_access_error_status);
			mPlusClient.disconnect();
		}
		mPlusClient.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {

		// Affichage du nom de compte du connect�
		String currentPersonName = mPlusClient.getCurrentPerson() != null ? mPlusClient
				.getCurrentPerson().getDisplayName()
				: getString(R.string.unknown_person);

		String currentPersonIMG = mPlusClient.getCurrentPerson().getImage()
				.getUrl().toString();
		String currentPersonLang = mPlusClient.getCurrentPerson().getLanguage();
		mSignInStatus.setText(getString(R.string.signed_in_status,
				currentPersonName));

		// Cr�ation du participant s'il n'existe pas
		if (bd.verifPart(currentPersonName) == -1) {
			Participant unPart = new Participant();
			int compte = bd.getPartCompte();
			unPart.setId(compte);
			unPart.setNomPart(currentPersonName);
			unPart.setImgPart(currentPersonIMG);
			unPart.setLang(currentPersonLang);
			idduPart = bd.createPart(unPart);
		} else {
			idduPart = bd.verifPart(currentPersonName);
		}
		updateButtons(true);

	}

	@Override
	public void onDisconnected() {

		// Message si d�connect�
		mSignInStatus.setText(R.string.loading_status);
		mPlusClient.connect();
		updateButtons(false);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {

		// Message si la connection �choue
		mConnectionResult = result;
		updateButtons(false);
	}

	private void updateButtons(boolean isSignedIn) {

		// Mis-�-jour des boutons affich�s selon le statut de connexion
		if (b.getInt("deconnexion") == 1) {
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.disconnect();
				mPlusClient.connect();
				b.putInt("deconnexion", 0);
				b.clear();
			}
		} else if (isSignedIn) {
			mSignInButton.setVisibility(View.INVISIBLE);
			mSignInStatus.setVisibility(View.INVISIBLE);
			Intent intent;
			intent = new Intent(this, principaleActivity.class);
			b.putInt("idPart", idduPart);
			b.putInt("deconnexion", 0);
			intent.putExtras(b);
			this.startActivity(intent);
		}

		if (!isSignedIn) {
			if (mConnectionResult == null) {
				mSignInButton.setVisibility(View.INVISIBLE);
				mSignInStatus.setText(getString(R.string.loading_status));
			} else {
				mSignInButton.setVisibility(View.VISIBLE);
				mSignInStatus.setText(getString(R.string.signed_out_status));
			}
		}
	}
}
