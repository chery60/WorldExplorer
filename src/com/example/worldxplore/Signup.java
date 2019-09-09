package com.example.worldxplore;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends Activity {
SharedPreferences pref;
Editor edit;
ProgressDialog progressBar;
Handler handle;
EditText fn,ln,un,em,mo,ps,cps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		pref=getSharedPreferences("STORED", MODE_PRIVATE);
		edit=pref.edit();
		fn=(EditText)findViewById(R.id.firstname);
		ln=(EditText)findViewById(R.id.lastname);
		un=(EditText)findViewById(R.id.username);
		em=(EditText)findViewById(R.id.emailid);
		mo=(EditText)findViewById(R.id.mobileno);
		ps=(EditText)findViewById(R.id.password);
		cps=(EditText)findViewById(R.id.confirmpass);
		

	}
public void signup(View v) {
	if (fn.getText().toString().isEmpty()||ln.getText().toString().isEmpty()
			||un.getText().toString().isEmpty()||em.getText().toString().isEmpty()
			||mo.getText().toString().isEmpty()||ps.getText().toString().isEmpty()
			||cps.getText().toString().isEmpty()) {
	    Toast.makeText(getApplicationContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();

	}else {
		if (ps.getText().toString().equals(cps.getText().toString())) {
		    edit.putString("firstname", fn.getText().toString());
		    edit.putString("lastname", ln.getText().toString());
		    edit.putString("username", un.getText().toString());
		    edit.putString("emailid", em.getText().toString());
		    edit.putString("mobileno", mo.getText().toString());
		    edit.putString("password", ps.getText().toString());
		    edit.commit();
		    progressBar = new ProgressDialog(v.getContext());
	         progressBar.setCancelable(false);
	         progressBar.setMessage("Redirecting to login page..");
	         progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	         progressBar.setProgress(0);
//	         progressBar.setMax(100);
	         progressBar.setIndeterminate(true);
	         progressBar.show();
	         handle=new Handler();
	         handle.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						  Toast.makeText(getApplicationContext(), "Signed up sucessfully!", Toast.LENGTH_SHORT).show();
							finish();
						progressBar.dismiss();
					}
				}, 500);
		  
	}else {
	        Toast.makeText(getApplicationContext(), "Password doesn't match!", Toast.LENGTH_SHORT).show();

	  }
	}
	
	   

	    
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}

}
