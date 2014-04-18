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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;

public class connexionActivity extends Activity implements OnClickListener,
		PlusClient.ConnectionCallbacks, PlusClient.OnConnectionFailedListener,
		PlusClient.OnAccessRevokedListener {

	private QingPoolDatasource bd;
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

	private TextView mSignInStatus;
	private PlusClient mPlusClient;
	private SignInButton mSignInButton;
	private View mSignOutButton;
	private View btnPageAccueil;
	private View btnPagePrinc;
	private int idduPart;

	private ConnectionResult mConnectionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connexion);

		this.bd = new QingPoolDatasource(this);
		this.bd.open();
		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				MomentUtil.ACTIONS).build();

		mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
		mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
		mSignInButton.setOnClickListener(this);
		mSignOutButton = findViewById(R.id.sign_out_button);
		mSignOutButton.setOnClickListener(this);
		btnPageAccueil = findViewById(R.id.btnPageAccueil);
		btnPageAccueil.setOnClickListener(this);
		btnPagePrinc = findViewById(R.id.retourPagePrincipale);
		btnPagePrinc.setOnClickListener(this);

		Tracker tracker = GoogleAnalytics.getInstance(this).getTracker(
				"UA-50075921-1");

		HashMap<String, String> hitParameters = new HashMap<String, String>();
		hitParameters.put(Fields.HIT_TYPE, "appview");
		hitParameters.put(Fields.SCREEN_NAME, "Connexion");

		tracker.send(hitParameters);
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

		Intent intent;
		switch (view.getId()) {
		case R.id.sign_in_button:
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
				// Fetch a new result to start.
				mPlusClient.connect();
			}
			break;
		case R.id.sign_out_button:
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.disconnect();
				mPlusClient.connect();
			}
			break;
		case R.id.retourPagePrincipale:
			intent = new Intent(this, principaleActivity.class);
			Bundle b = new Bundle();
			b.putInt("idPart", idduPart);
			intent.putExtras(b);
			this.startActivity(intent);
			break;
		case R.id.btnPageAccueil:

			intent = new Intent(this, accueilActivity.class);

			this.startActivity(intent);
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
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
		if (requestCode == REQUEST_CODE_SIGN_IN
				|| requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
			if (resultCode == RESULT_OK && !mPlusClient.isConnected()
					&& !mPlusClient.isConnecting()) {
				// This time, connect should succeed.
				mPlusClient.connect();
			}
		}
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
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
		String currentPersonName = mPlusClient.getCurrentPerson() != null ? mPlusClient
				.getCurrentPerson().getDisplayName()
				: getString(R.string.unknown_person);

		String currentPersonIMG = mPlusClient.getCurrentPerson().getImage()
				.getUrl().toString();
		String currentPersonLang = mPlusClient.getCurrentPerson().getLanguage();
		mSignInStatus.setText(getString(R.string.signed_in_status,
				currentPersonName));
		updateButtons(true /* isSignedIn */);

		/* ========================================================== */
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

		/* ========================================================== */
	}

	@Override
	public void onDisconnected() {
		mSignInStatus.setText(R.string.loading_status);
		mPlusClient.connect();
		updateButtons(false /* isSignedIn */);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		mConnectionResult = result;
		updateButtons(false /* isSignedIn */);
	}

	private void updateButtons(boolean isSignedIn) {
		if (isSignedIn) {
			mSignInButton.setVisibility(View.INVISIBLE);
			btnPagePrinc.setVisibility(View.VISIBLE);
			mSignOutButton.setEnabled(true);
		} else {
			if (mConnectionResult == null) {
				// Disable the sign-in button until onConnectionFailed is called
				// with result.
				mSignInButton.setVisibility(View.INVISIBLE);
				btnPagePrinc.setVisibility(View.VISIBLE);
				mSignInStatus.setText(getString(R.string.loading_status));
			} else {
				// Enable the sign-in button since a connection result is
				// available.
				mSignInButton.setVisibility(View.VISIBLE);
				btnPagePrinc.setVisibility(View.INVISIBLE);
				mSignInStatus.setText(getString(R.string.signed_out_status));
			}

			mSignOutButton.setEnabled(false);
		}
	}

}
