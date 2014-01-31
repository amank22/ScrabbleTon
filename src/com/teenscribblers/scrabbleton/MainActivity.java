package com.teenscribblers.scrabbleton;

import java.util.Locale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.capricorn.RayMenu;

public class MainActivity extends SherlockActivity {

	private Cursor words, rwords;
	private DatabaseAssist db;
	ListView l;
	CustomAdapter cadapter;
	EditText searchbox;
	TextView toptext;
	private static final int[] ITEM_DRAWABLES = { R.drawable.abc,
			R.drawable.random, R.drawable.number };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ColorDrawable c=new ColorDrawable(Color.parseColor("#000000"));
		getSupportActionBar().setBackgroundDrawable(c);
		db = new DatabaseAssist(this);
		l = (ListView) findViewById(R.id.list);
		searchbox = (EditText) findViewById(R.id.searchbox);
		toptext = (TextView) findViewById(R.id.toptext);
		Typeface tf1 = Typeface.createFromAsset(this.getAssets(),
				"Broadway.ttf");
		toptext.setTypeface(tf1);
		new AsyncCaller().execute();
		// Actionbar functions
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
		initArcMenu(rayMenu, ITEM_DRAWABLES);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		words.close();
		db.close();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		cadapter = new CustomAdapter(MainActivity.this, words, true);
		l.setAdapter(cadapter);
	}

	private class AsyncCaller extends AsyncTask<Void, Void, Void> {
		ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// this method will be running on UI thread
			pdLoading.setMessage("\tLoading...");
			pdLoading.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			words = db.getWords(); // you would not typically call this on the
									// main
			// thread

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pdLoading.dismiss();
			cadapter = new CustomAdapter(MainActivity.this, words, true);
			l.setAdapter(cadapter);
			filter();
		}

	}

	private void aboutdialog() {
		// TODO Auto-generated method stub
		// Create custom dialog object
		final Dialog dialog = new Dialog(MainActivity.this);
		// Include dialog.xml file
		dialog.setContentView(R.layout.aboutdialog);
		// Set dialog title
		dialog.setTitle("About");
		dialog.show();
	}

	public void filter() {
		// TODO Auto-generated method stub
		searchbox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				cadapter.getFilter().filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		cadapter.setFilterQueryProvider(new FilterQueryProvider() {

			@Override
			public Cursor runQuery(CharSequence constraint) {
				// TODO Auto-generated method stub
				return db.getmatchingwords(constraint.toString());
			}
		});
	}

	private void dialogcreation() {
		// TODO Auto-generated method stub
		// Create custom dialog object
		final Dialog dialog = new Dialog(MainActivity.this);
		// Include dialog.xml file
		dialog.setContentView(R.layout.customdialog);
		// Set dialog title
		dialog.setTitle("Select Letter");
		dialog.show();
		// setting custom functions

		// Selection of the spinner
		Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
		final String[] array = { "select here", "a", "b", "c", "d", "e", "f",
				"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
				"s", "t", "u", "v", "w", "x", "y", "z" };
		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				MainActivity.this, android.R.layout.simple_spinner_item, array);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The
																							// drop
																							// down
																							// view
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view1,
					int pos, long id) {
				if (pos != 0) {
					rwords = db.getWordsByLetter(array[pos]);
					toptext.setText("Words starting with "
							+ array[pos].toUpperCase(Locale.US));
					cadapter = new CustomAdapter(MainActivity.this, rwords,
							true);
					l.setAdapter(cadapter);
					dialog.dismiss();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:{
			if(cadapter.getCursor()==rwords){
			cadapter.swapCursor(words);
			toptext.setText("All words");
			}
			
			return true;
		}
		case R.id.about:{
			aboutdialog();
			return true;
		}
		}
		return onOptionsItemSelected(item);

	}

	private void initArcMenu(RayMenu rayMenu, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(itemDrawables[i]);

			final int position = i;
			rayMenu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (position == 0) {
						dialogcreation();
						Toast.makeText(MainActivity.this, "Letter filter",
								Toast.LENGTH_SHORT).show();
					} else if (position == 1) {
						rwords = db.getRandomWords();
						toptext.setText("Random Words");
						cadapter.swapCursor(rwords);
						Toast.makeText(MainActivity.this, "Random words",
								Toast.LENGTH_SHORT).show();
					} else {
						dialogcreationforcount();
						Toast.makeText(MainActivity.this, "Words by count",
								Toast.LENGTH_SHORT).show();
					}

				}
			});
		}
	}

	// for word count

	private void dialogcreationforcount() {
		// TODO Auto-generated method stub
		// Create custom dialog object
		final Dialog dialog = new Dialog(MainActivity.this);
		// Include dialog.xml file
		dialog.setContentView(R.layout.customdialog);
		// Set dialog title
		dialog.setTitle("Select length");
		dialog.show();
		// setting custom functions

		// Selection of the spinner
		Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
		final String[] array = { "select here", "2", "3", "4", "5", "6", "7",
				"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18",
				"19", "20", "21" };
		// Application of the Array to the Spinner
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				MainActivity.this, android.R.layout.simple_spinner_item, array);
		spinnerArrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The
																							// drop
																							// down
																							// view
		spinner.setAdapter(spinnerArrayAdapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view1,
					int pos, long id) {
				if (pos != 0) {
					rwords = db.getWordsBycount(Integer.parseInt(array[pos]));
					toptext.setText("Words of length "
							+ Integer.parseInt(array[pos]));
					cadapter.swapCursor(rwords);
					dialog.dismiss();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}