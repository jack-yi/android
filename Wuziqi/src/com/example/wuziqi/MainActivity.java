package com.example.wuziqi;

import com.example.wuziqi.GoBangView.OnGameIsOverListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

	GoBangView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView=(GoBangView) findViewById(R.id.id_gobangview);
        OnGameIsOverListener ogl=new OnGameIsOverListener() {
			
			@Override
			public void alert() {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
				builder.setMessage(myView.isWhiteWinner()==true?"°×ÆåÊ¤Àû":"ºÚÆåÊ¤Àû");
				builder.setPositiveButton("ÖØÍæ", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						myView.start();
						
					}
				});
				builder.setNegativeButton("ÍË³ö", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
						
					}
				});
				builder.show();
			}
		};
		myView.setOnGameIsOverListener(ogl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	myView.start();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
