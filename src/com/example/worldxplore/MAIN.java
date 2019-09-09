package com.example.worldxplore;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class MAIN extends Activity {
	Button location, location1;
	TextView heading, message, heading1, message1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences pref = getSharedPreferences("STORED", MODE_PRIVATE);
		heading = (TextView) findViewById(R.id.heading1);
		message = (TextView) findViewById(R.id.message1);
		heading1 = (TextView) findViewById(R.id.heading2);
		message1 = (TextView) findViewById(R.id.message2);
		location = (Button) findViewById(R.id.locaion);
		location1 = (Button) findViewById(R.id.locaion1);
		// heading.setText("Hello");
		heading.append(pref.getString("username", null) + ",");
		heading1.append(pref.getString("username", null) + ",");
		Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT)
				.show();
		TabHost tab = (TabHost) findViewById(R.id.tabhost);
		tab.setup();

		TabSpec spec = tab.newTabSpec("TAB1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("INDIA");
		tab.addTab(spec);

		spec = tab.newTabSpec("TAB2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("WORLD");
		tab.addTab(spec);
	}

	public void location(View view) {
	String place=heading.getText().toString();
	String transport="Train,Flight";
    startActivity(new Intent(MAIN.this,LocationActivity.class).putExtra("place", place).putExtra("transport", transport));
    
	}

	public void location1(View view) {
		String place=heading1.getText().toString();
		String transport="Flight";
	    startActivity(new Intent(MAIN.this,LocationActivity.class).putExtra("place", place).putExtra("transport", transport));
	    }

	public void tajmahal(View v) {
		location.setVisibility(View.VISIBLE);
		heading.setText("TAJ MAHAL");
		message.setText("     The Taj Mahal is an ivory-white marble mausoleum on the south bank of the Yamuna river in the Indian city of Agra. It was commissioned in 1632 by the Mughal emperor, Shah Jahan, to house the tomb of his favorite wife, Mumtaz Mahal.");

	}

	public void qutubminar(View v) {
		location.setVisibility(View.VISIBLE);
		heading.setText("QUTB MINAR");
		message.setText("     Qlnar, at 72 meters, is the tallest brick minaret in the world. Qutub Minar, along with the ancient and medieval monuments surrounding it, form the Qutub Complex, which is a UNESCO World Heritage Site.");

	}

	public void redfort(View v) {
		location.setVisibility(View.VISIBLE);
		heading.setText("RED FORT");
		message.setText("     The Red Fort was the residence of the Mughal emperor for nearly 200 years, until 1857. It is located in the centre of Delhi and houses a number of museums.");
	}

	public void lotustemple(View v) {
		location.setVisibility(View.VISIBLE);
		heading.setText("LOTUS TEMPLE");
		message.setText("     The Lotus Temple, located in New Delhi, India, is a Bahá'í House of Worship completed in 1986. Notable for its flowerlike shape, it serves as the Mother Temple of the Indian subcontinent and has become a prominent attraction in the city.");
	}

	public void eiffeltower(View v) {
		location1.setVisibility(View.VISIBLE);
		heading1.setText("EIFFEL TOWER");
		message1.setText("     The Eiffel Tower is a wrought iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower.");

	}

	public void chinawall(View v) {
		location1.setVisibility(View.VISIBLE);
		heading1.setText("GREAT WALL OF CHINA");
		message1.setText("     The Great Wall of China is the world's longest wall and biggest ancient architecture.It has a stunning array of scenery from the beaches of Qinhuangdao, to rugged mountains around Beijing, to a desert corridor between tall mountain ranges at Jiayu Pass.");
	}

	public void britishmuseum(View v) {
		location1.setVisibility(View.VISIBLE);
		heading1.setText("BRITISH MUSEUM");
		message1.setText("     The British Museum is a museum dedicated to human history, art, and culture, located in the Bloomsbury area of London.");
	}

	public void greatpyramid(View v) {
		location1.setVisibility(View.VISIBLE);
		heading1.setText("GREAT PYRAMID OF GIZA");
		message1.setText("     The Great Pyramid of Giza is the oldest and largest of the three pyramids in the Giza pyramid complex bordering what is now El Giza, Egypt. It is the oldest of the Seven Wonders of the Ancient World, and the only one to remain largely intact.");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
