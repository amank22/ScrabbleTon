package com.teenscribblers.scrabbleton;

import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends CursorAdapter {
	private LayoutInflater mLayoutInflater;
	private DatabaseAssist dbhelper;

	public CustomAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
		
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// TODO Auto-generated method stub

		
		String words = c.getString(c.getColumnIndexOrThrow("word"));
		String imgletter = words.substring(0, 1).toUpperCase(
				Locale.getDefault());
		String[] colors = { "#40AFD5", "#D5E672", "#409072", "#C16718",
				"#6FDD1A", "#5EB098" };
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "Adec.ttf");
		Typeface tf1 = Typeface.createFromAsset(context.getAssets(),
				"Broadway.ttf");
		TextView words_text = (TextView) v.findViewById(R.id.textall);
		// setting text
		if (words_text != null) {
			words_text.setTypeface(tf);
			words_text.setText(words);
		}
		// setting image
		ImageView item_image = (ImageView) v.findViewById(R.id.sideimage);
		item_image.setBackgroundColor(Color.parseColor(colors[new Random()
				.nextInt(6)]));

		TextView image_text = (TextView) v.findViewById(R.id.imagetext);
		if (image_text != null) {
			image_text.setTypeface(tf1);
			image_text.setText(imgletter);
		}

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = mLayoutInflater.inflate(R.layout.worditem, parent, false);
		return v;
	}
	
	@Override
	public CharSequence convertToString(Cursor cursor) {
		// TODO Auto-generated method stub
		return super.convertToString(cursor);
	}
	
	

}
