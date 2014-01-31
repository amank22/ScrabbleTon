package com.teenscribblers.scrabbleton;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

public class SplashActivity extends SherlockActivity {
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    TextView name,appname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		getSupportActionBar().hide();
		name=(TextView) findViewById(R.id.tsname);
		appname=(TextView) findViewById(R.id.appname);
		Typeface tf1 = Typeface.createFromAsset(getAssets(),
				"METROLOX.ttf");
		Typeface tf2 = Typeface.createFromAsset(getAssets(),
				"Broadway.ttf");
		name.setTypeface(tf1);
		appname.setTypeface(tf2);
		
		new Handler().postDelayed(new Runnable() {
			 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

	}

	

}
