package org.circedroid.activities;

import org.circedroid.R;
import org.circedroid.core.RegisterManager;
import org.circedroid.core.exception.NotFoundException;
import org.circedroid.core.geodesy.CRS;
import org.circedroid.core.register.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TransformActivity extends Activity {

	private static final String LOG_TAG = TransformActivity.class
			.getSimpleName();

	private static final String IN_CRS_ID_KEY = "inCRSId";
	private static final String OUT_CRS_ID_KEY = "outCRSId";

	private static final int IN_CRS_ID = 123456789;
	private static final int OUT_CRS_ID = 987654321;

	private CRS inCRS = null;
	private CRS outCRS = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transform);

		String inCRSId = null;
		if (savedInstanceState != null) {
			inCRSId = savedInstanceState.getString(IN_CRS_ID_KEY);
		}
		if (inCRSId == null) {
			inCRSId = getString(R.string.defaultInCRS);
		}
		setInCRS(inCRSId);

		String outCRSId = null;
		if (savedInstanceState != null) {
			outCRSId = savedInstanceState.getString(OUT_CRS_ID_KEY);
		}
		if (outCRSId == null) {
			outCRSId = getString(R.string.defaultoOutCRS);
		}
		setOutCRS(outCRSId);
	}

	public void setInCRS(String id) {
		try {
			inCRS = RegisterManager.getInstance().getCRSById(id);
		} catch (NotFoundException e) {
			Log.e(LOG_TAG, e.getMessage());
			inCRS = new CRS();
			inCRS.setId("NotFound");
			inCRS.setName("NotFound");
		}
	}

	public void setOutCRS(String id) {
		try {
			outCRS = RegisterManager.getInstance().getCRSById(id);
		} catch (NotFoundException e) {
			Log.e(LOG_TAG, e.getMessage());
			outCRS = new CRS();
			outCRS.setId("NotFound");
			outCRS.setName("NotFound");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(IN_CRS_ID_KEY, inCRS.getId());
		outState.putString(OUT_CRS_ID_KEY, outCRS.getId());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_transform, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null){
			return;
		}
		if (requestCode == IN_CRS_ID) {
			String result = data
					.getStringExtra(SearchCRSActivity.ACTION_SEARH_RESULT_KEY);
			setInCRS(result);
			refresh();
			return;
		} else if (requestCode == OUT_CRS_ID) {
			String result = data
					.getStringExtra(SearchCRSActivity.ACTION_SEARH_RESULT_KEY);
			setOutCRS(result);
			refresh();
			return;
		}
	}

	private void refresh() {
		Log.d(LOG_TAG, "refresh()");
		// Remove all
		LinearLayout mainll = (LinearLayout) findViewById(R.id.transform_layout);
		mainll.removeAllViews();

		// Generate InCRS Layout
		Log.d(LOG_TAG, "Generate InCRS Layout");
		Log.d(LOG_TAG, "inflate");
		LayoutInflater.from(this).inflate(R.layout.activity_tranform_crs,
				mainll);
		Log.d(LOG_TAG, "findLayout");
		RelativeLayout inCRSLayout = (RelativeLayout) findViewById(R.id.transform_crs_template);
		Log.d(LOG_TAG, "updateId");
		inCRSLayout.setId(IN_CRS_ID);
		TextView inTVCRSprefix = (TextView) inCRSLayout
				.findViewById(R.id.transform_crs_prefix);
		inTVCRSprefix.setText(R.string.start);
		TextView inTVCRSid = (TextView) inCRSLayout
				.findViewById(R.id.transform_crs_id);
		inTVCRSid.setText(inCRS.getId());
		TextView inTVCRSname = (TextView) inCRSLayout
				.findViewById(R.id.transform_crs_name);
		Log.d(LOG_TAG, inTVCRSname.getText().toString());
		inTVCRSname.setText(inCRS.getName());

		Log.d(LOG_TAG, "OnClick");

		inCRSLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent startIntent = new Intent(TransformActivity.this,
						SearchCRSActivity.class);
				startActivityForResult(startIntent, IN_CRS_ID);
			}
		});

		// Generate OutCRS Layout
		Log.d(LOG_TAG, "Generate OutCRS Layout");
		LayoutInflater.from(this).inflate(R.layout.activity_tranform_crs,
				mainll);
		RelativeLayout outCRSLayout = (RelativeLayout) findViewById(R.id.transform_crs_template);
		outCRSLayout.setId(OUT_CRS_ID);
		TextView outTVCRSprefix = (TextView) outCRSLayout
				.findViewById(R.id.transform_crs_prefix);
		TextView outTVCRSid = (TextView) outCRSLayout
				.findViewById(R.id.transform_crs_id);
		TextView outTVCRSname = (TextView) outCRSLayout
				.findViewById(R.id.transform_crs_name);
		outTVCRSprefix.setText(R.string.end);
		outTVCRSid.setText(outCRS.getId());
		outTVCRSname.setText(outCRS.getName());
		outCRSLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent startIntent = new Intent(TransformActivity.this,
						SearchCRSActivity.class);
				startActivityForResult(startIntent, OUT_CRS_ID);
			}
		});

	}

}
