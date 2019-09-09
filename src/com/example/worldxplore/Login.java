package com.example.worldxplore;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Login extends Activity {
Button b1;
EditText un,ps;
ProgressDialog progressBar;
Handler handle;
SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		b1=(Button)findViewById(R.id.button1);
		pref=getSharedPreferences("STORED", MODE_PRIVATE);
		un=(EditText)findViewById(R.id.username);
		ps=(EditText)findViewById(R.id.password);
	    b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				    progressBar = new ProgressDialog(arg0.getContext());
		            progressBar.setCancelable(false);
		            progressBar.setMessage("Please wait for validation process.");
		            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		            progressBar.setProgress(0);
//		            progressBar.setMax(100);
		            progressBar.setIndeterminate(true);
		            progressBar.show();
		            handle=new Handler();
		            handle.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressBar.dismiss();
							String username=pref.getString("username", null);
							String password=pref.getString("password", null);

						if (username!=null && password!=null) {

							 if ((un.getText().toString().isEmpty()) || (ps.getText().toString().isEmpty())) {
						  	      Toast.makeText(getApplicationContext(), "Username or Password is Empty!", Toast.LENGTH_SHORT).show();

						           }else {
						        	   if ((pref.getString("username", null).equals(un.getText().toString())) && (pref.getString("password", null).equals(ps.getText().toString()))){
						      		     Intent i=new Intent(Login.this,MAIN.class);
						      		      startActivity(i);
						              	}else {
						      		      Toast.makeText(getApplicationContext(), "Username or Password incorrect!", Toast.LENGTH_SHORT).show();

						           	    }
						        }
					       	}else {
						  	      Toast.makeText(getApplicationContext(), "Please do register by signup.", Toast.LENGTH_SHORT).show();

						}
       
		 }
		}, 2000);
		            
		     	}
		  });
	  }
	public void signup(View v) {
		 progressBar = new ProgressDialog(v.getContext());
         progressBar.setCancelable(false);
         progressBar.setMessage("Please wait..While the page is loading.");
         progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//         progressBar.setProgress(0);
//         progressBar.setMax(100);
         progressBar.setIndeterminate(true);
         progressBar.show();
         handle=new Handler();
         handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent i=new Intent(Login.this,Signup.class);
					startActivity(i);
					progressBar.dismiss();
					
				}
			}, 500);
		
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
