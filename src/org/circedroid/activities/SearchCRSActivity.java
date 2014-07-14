package org.circedroid.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.circedroid.R;
import org.circedroid.core.RegisterManager;
import org.circedroid.core.exception.CompletementConException;
import org.circedroid.core.exception.NotFoundException;
import org.circedroid.core.geodesy.Axis;
import org.circedroid.core.geodesy.CRS;
import org.circedroid.core.geodesy.CRS.CRSType;
import org.circedroid.core.geodesy.Datum;
import org.circedroid.core.register.Register;
import org.circedroid.core.register.SearchResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SearchCRSActivity extends Activity {
	
	private static String LOG_TAG = SearchCRSActivity.class.getName();
	public final static String ACTION_SEARH = "org.circedroid.action.SEARCH_CRS";
	public final static String ACTION_SEARH_RESULT_KEY = "SelectedCRS";
	public final static int ACTION_SEARH_RESULT_OK = 200;

	public TextView tvsearch;
	public int count = 0;
	public LinearLayout resultsView;
	
	public OnClickListener onClickListSelect;
	public OnClickListener onClickListInfo;
	
	private static String selectedId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		// Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    
		tvsearch = (TextView) findViewById(R.id.searchText);
		tvsearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				search();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				//NOTHING TO DO
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				//NOTHING TO DO
			}
		});

		ImageView ivsearch = (ImageView) findViewById(R.id.searchSubmit);
		ivsearch.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				search();
			}
		});
		
		resultsView = (LinearLayout) findViewById(R.id.searchResultLayout);

		this.onClickListSelect = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView tv = (TextView) ((View) v.getParent()).findViewById(R.id.search_crs_id);
				selectCRS(tv.getText().toString());
			}
		};
		this.onClickListInfo = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView tv = (TextView) ((View) v.getParent()).findViewById(R.id.search_crs_id);
				selectedId = tv.getText().toString();
				AlertDialog.Builder adb = new AlertDialog.Builder(SearchCRSActivity.this);
				adb.setTitle(getString(R.string.info_of)+selectedId);
				adb.setIcon(android.R.drawable.ic_menu_info_details);
				adb.setItems(getCRSDescription(selectedId), null);
				adb.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectCRS(selectedId);
						
					}
				});
				adb.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				adb.show();
				
			}
		};
	}
	
	private void selectCRS(String id){
		Intent intentResult = new Intent(SearchCRSActivity.ACTION_SEARH);
		intentResult.putExtra(SearchCRSActivity.ACTION_SEARH_RESULT_KEY,id);
		setResult(ACTION_SEARH_RESULT_OK, intentResult);
		finish();
	}
	
	private String[] getCRSDescription(String id){
		ArrayList<String> description = new ArrayList<String>();
		
		CRS crs;
		try {
			crs = RegisterManager.getInstance().getCRSById(id);
		} catch (NotFoundException e) {
			description.add(getString(R.string.crs_not_found));
			return description.toArray(new String[description.size()]);
		}
		
		Log.d(LOG_TAG, crs.toString());
		//Type
		String tmp = "";
		switch (crs.getType()) {
		case GeocentricCRS:
			tmp = getString(R.string.geocentric);
			break;
		case GeographicCRS:
			tmp = getString(R.string.geographic);
			break;
		case ProjectedCRS:
			tmp = getString(R.string.projected);
			break;
		case CompoundCRS:
			tmp = getString(R.string.coumpound);
			break;
		case VerticalCRS:
			tmp = getString(R.string.vertical);
		default:
			break;
		}
		description.add(getString(R.string.type)+tmp);
		
		if (crs.getType() != CRSType.VerticalCRS){
			//Datum
			try {
				description.add(getString(R.string.datum)+crs.getDatum().getName());
				description.add(getString(R.string.ellipsoid)+crs.getEllipsoid().getName());
				description.add(getString(R.string.meridian)+crs.getPrimeMeridian().getName());
			} catch (NotFoundException e) {
				description.add(getString(R.string.datum)+getString(R.string.not_found));
				Log.e(LOG_TAG, e.getMessage());
			}
		}
		if (crs.getType() == CRSType.VerticalCRS || crs.getType() == CRSType.CompoundCRS){
			//vertical Datum
			try {
				description.add(getString(R.string.vertical_datum)+crs.getVerticalDatum().getName());
			} catch (NotFoundException e) {
				description.add(getString(R.string.vertical_datum)+getString(R.string.not_found));
				Log.e(LOG_TAG, e.getMessage());
			}
		}
		

		try {
			description.add(getString(R.string.projection)+crs.getConversion().getName());
		} catch (NotFoundException e) {
			description.add(getString(R.string.projection)+getString(R.string.not_found));
			Log.e(LOG_TAG, e.getMessage());
		} catch (CompletementConException e) {
			Log.e(LOG_TAG, e.getMessage());
		}
		
		if (crs.getType() != CRSType.VerticalCRS){
			try {
				List<Axis> axis = crs.getAxis();
				tmp = getString(R.string.not_found);
				switch (axis.get(0).getUom()) {
					case Sexa:
						tmp = getString(R.string.sexa);
						break;
					case Deg:
						tmp = getString(R.string.deg);
						break;
					case Grad:
						tmp = getString(R.string.grad);
						break;
					case Meter:
						tmp = getString(R.string.meter);
						break;
					default:
						break;
				}
				description.add(getString(R.string.uom)+tmp);
			} catch (NotFoundException e) {
				description.add(getString(R.string.uom)+getString(R.string.not_found));
				Log.e(LOG_TAG, e.getMessage());
			} catch (CompletementConException e) {
				Log.e(LOG_TAG, e.getMessage());
			}
		}
		
		return description.toArray(new String[description.size()]);
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

	public void search() {
		resultsView.removeAllViews();
		
		String search = tvsearch.getText().toString();
		Log.d(LOG_TAG, "Search #"+search+"#");
		
		List<SearchResult<CRS>> results = RegisterManager.getInstance()
				.findCRS(this,search);
		if (results == null){
//			displayAll();
			return;
		}
		Log.d(LOG_TAG, " - Find "+results.size()+" results");
		Log.d(LOG_TAG, " - Sort results");
		
		Log.d(LOG_TAG, " - Print results");
		if (results.size() == 0){
			LayoutInflater.from(this).inflate(R.layout.activity_search_noresult, resultsView);
			return;
		}
		for (SearchResult<CRS> result : results){
			addCRSView(result.getItem());
		}

	}
	public void displayAll(){
		resultsView.removeAllViews();
		for (CRS crs : RegisterManager.getInstance().getAllCRS()){
			addCRSView(crs);
		}
	}
	public void addCRSView(CRS crs){
		LayoutInflater.from(this).inflate(R.layout.activity_search_crs, resultsView);
		RelativeLayout ll = (RelativeLayout) resultsView.findViewById(R.id.activity_search_crs);
		ll.setId(++count);
		TextView tvCRSId = (TextView) ll.findViewById(R.id.search_crs_id);
		TextView tvCRSName = (TextView) ll.findViewById(R.id.search_crs_name);
		ImageView ivCRSInfo = (ImageView) ll.findViewById(R.id.search_crs_info);
		tvCRSId.setText(crs.getId());
		tvCRSName.setText(crs.getName());
		tvCRSId.setOnClickListener(this.onClickListSelect);
		tvCRSName.setOnClickListener(this.onClickListSelect);
		ivCRSInfo.setOnClickListener(this.onClickListInfo);
	}

}


