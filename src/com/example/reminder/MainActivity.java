package com.example.reminder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String statusText = "Info";
	private TextView txt;
	private TextView updateCount;
	private Switch alarmSetSwitch;
	private Switch systemSwitch;
	private int count = 0;
	private boolean alarmSet = true;
	private AsyncTask<String, Void, String> thread;
	private MediaPlayer mPlayer;

	private Spinner spnAuDrate;
	private Spinner spnAuIrate;

	private Spinner dolarSpnIrate;
	private Spinner dolarSpnDrate;

	private Spinner euroSpnIrate;
	private Spinner euroSpnDrate;

	private Element altinAlis;
	private Element altinSatis;

	private Element dolarAlis;
	private Element dolarSatis;

	private Element euroAlis;
	private Element euroSatis;

	private TextView lblAuAverage;
	private TextView lblDolarAverage;
	private TextView lblEuroAverage;

	private EditText tfAverage;
	private EditText tfDolarAverage;
	private EditText tfEuroAverage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();

		alarmSetSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					alarmSet = true;
				} else {
					alarmSet = false;
				}

			}
		});

		systemSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					enableComponenets(false);
					thread = new LongOperation();
					thread.execute("");
				} else {
					thread.cancel(true);
					enableComponenets(true);
				}

			}

			private void enableComponenets(boolean value) {
				tfAverage.setEnabled(value);
				spnAuDrate.setEnabled(value);
				spnAuIrate.setEnabled(value);

				tfDolarAverage.setEnabled(value);
				dolarSpnDrate.setEnabled(value);
				dolarSpnIrate.setEnabled(value);

				tfEuroAverage.setEnabled(value);
				euroSpnDrate.setEnabled(value);
				euroSpnIrate.setEnabled(value);

				if (value) {
					lblAuAverage.setTextColor(Color.BLACK);
					lblDolarAverage.setTextColor(Color.BLACK);
					lblEuroAverage.setTextColor(Color.BLACK);
				}
			}
		});
	
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			System.out.println("onKeyDown");
			System.out.println("onKeyDown");
			System.out.println("onKeyDown");
			System.out.println("onKeyDown");
			System.out.println("onKeyDown");
			System.out.println("onKeyDown");
			exitByBackKey();
			// moveTaskToBack(false);

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	 
	protected void exitByBackKey() {

		AlertDialog alertbox = new AlertDialog.Builder(this).setMessage("Do you want to exit application?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {
						thread.cancel(true);
						finish();
						// close();

					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {

					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).show();

	}

	private void init() {
		tfAverage = (EditText) findViewById(R.id.tfAuAverage);
		tfDolarAverage = (EditText) findViewById(R.id.tfDolarAverage);
		tfEuroAverage = (EditText) findViewById(R.id.tfEuroAverage);

		lblAuAverage = (TextView) findViewById(R.id.lblAuAverage);
		lblDolarAverage = (TextView) findViewById(R.id.lblDolarAverage);
		lblEuroAverage = (TextView) findViewById(R.id.lblEuroAverage);

		String[] array_spinner = {"1","1,5","2","2,5","3","3,5","4","4,5","5","5,5"};
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner);

		spnAuDrate = (Spinner) findViewById(R.id.spnAuDrate);
		spnAuDrate.setAdapter(adapter);

		spnAuIrate = (Spinner) findViewById(R.id.spnAuIrate);
		spnAuIrate.setAdapter(adapter);

		dolarSpnIrate = (Spinner) findViewById(R.id.dolarSpnIrate);
		dolarSpnIrate.setAdapter(adapter);
		dolarSpnDrate = (Spinner) findViewById(R.id.dolarSpnDrate);
		dolarSpnDrate.setAdapter(adapter);
		
		euroSpnIrate = (Spinner) findViewById(R.id.euroSpnIrate);
		euroSpnIrate.setAdapter(adapter);
		euroSpnDrate = (Spinner) findViewById(R.id.euroSpnDrate);
		euroSpnDrate.setAdapter(adapter);

		mPlayer = MediaPlayer.create(MainActivity.this, R.raw.ringingbells);

		alarmSetSwitch = (Switch) findViewById(R.id.alarmSetSwitch);
		systemSwitch = (Switch) findViewById(R.id.systemSwitch);

		updateCount = (TextView) findViewById(R.id.updateCount);
		txt = (TextView) findViewById(R.id.statusText);
		txt.setText(statusText);
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static String readURL(String url) {

		String fileContents = "";
		String currentLine = "";

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			fileContents = reader.readLine();
			while (currentLine != null) {
				currentLine = reader.readLine();
				fileContents += "\n" + currentLine;
			}
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("Error Message");
			e.printStackTrace();

		}

		return fileContents;
	}

	private class LongOperation extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(45000);
				long tic = System.currentTimeMillis();
				System.out.println("tic: " + tic);
				Document doc = Jsoup.parse(readURL("http://www.altinpiyasa.com/"));
				long toc = System.currentTimeMillis();
				System.out.println("toc: " + System.currentTimeMillis());
				
				long diff = toc -tic;
				
				System.out.println("dif: " + diff);
				

				Elements p_tags = doc.select("td");
				for (int j = 0; j < p_tags.size(); j++) {
					Element p = p_tags.get(j);
					System.out.println("P tag is " + p.text());
					if (p.text().contains("24 Ayar")) {
						Element altinText = p_tags.get(j);
						altinAlis = p_tags.get(j + 1);
						altinSatis = p_tags.get(j + 2);
						Element time = p_tags.get(j + 5);
						statusText = "24 Ayar" + ": Alis: " + altinAlis.text() + "-- Satis: " + altinSatis.text()
								+ " -- " + time.text();
					} else if (p.text().equals("Dolar / TL")) {
						Element dolarText = p_tags.get(j);
						dolarAlis = p_tags.get(j + 1);
						dolarSatis = p_tags.get(j + 2);
						Element time = p_tags.get(j + 5);
						statusText += "\n" + dolarText.text() + ": Alis: " + dolarAlis.text() + "-- Satis: "
								+ dolarSatis.text() + " -- " + time.text();
					} else if (p.text().equals("Euro / TL")) {
						Element euroText = p_tags.get(j);
						euroAlis = p_tags.get(j + 1);
						euroSatis = p_tags.get(j + 2);
						Element time = p_tags.get(j + 5);
						statusText += "\n" + euroText.text() + ": Alis: " + euroAlis.text() + "-- Satis: "
								+ euroSatis.text() + " -- " + time.text();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return statusText;
		}

		@Override
		protected void onPostExecute(String result) {

			count++;
			txt.setText(statusText); // txt.setText(result);
			updateCount.setText(String.valueOf(count) + "->"+ new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())); // txt.setText(result);

			String altinAlisText = altinAlis.text();
			double altinAlisDouble = transformToDouble(altinAlisText);
			String altinSatisText = altinSatis.text();
			double altinSatisDouble = transformToDouble(altinSatisText);

			String iAuRate = spnAuIrate.getSelectedItem().toString();
			double iAuRateDouble = transformToDouble(iAuRate) / 100;
			String dAuRate = spnAuDrate.getSelectedItem().toString();
			double dAuRateDouble = transformToDouble(dAuRate) / 100;

			double averageAuDouble = transformToDouble(tfAverage.getText().toString());

			String dolarAlisText = dolarAlis.text();
			double dolarAlisDouble = transformToDouble(dolarAlisText);
			String dolarSatisText = dolarSatis.text();
			double dolarSatisDouble = transformToDouble(dolarSatisText);

			String iDolarRate = dolarSpnIrate.getSelectedItem().toString();
			double iDolarRateDouble = transformToDouble(iDolarRate) / 100;
			String dDolarRate = dolarSpnDrate.getSelectedItem().toString();
			double dDolarRateDouble = transformToDouble(dDolarRate) / 100;

			double averageDolarDouble = transformToDouble(tfDolarAverage.getText().toString());

			String euroAlisText = euroAlis.text();
			double euroAlisDouble = transformToDouble(euroAlisText);
			String euroSatisText = euroSatis.text();
			double euroSatisDouble = transformToDouble(euroSatisText);

			String iEuroRate = euroSpnIrate.getSelectedItem().toString();
			double iEuroRateDouble = transformToDouble(iEuroRate) / 100;
			String dEuroRate = euroSpnDrate.getSelectedItem().toString();
			double dEuroRateDouble = transformToDouble(dEuroRate) / 100;

			double averageEuroDouble = transformToDouble(tfEuroAverage.getText().toString());

			if (altinAlisDouble > (averageAuDouble + averageAuDouble * iAuRateDouble)) {
				try {
					lblAuAverage.setTextColor(Color.GREEN);
					if (alarmSet) {
						mPlayer.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (altinSatisDouble < (averageAuDouble - averageAuDouble * dAuRateDouble)) {
				lblAuAverage.setTextColor(Color.RED);
				if (alarmSet) {
					mPlayer.start();
				}
			}
			if (dolarAlisDouble > (averageDolarDouble + averageDolarDouble * iDolarRateDouble)) {
				try {
					lblDolarAverage.setTextColor(Color.GREEN);
					if (alarmSet) {
						mPlayer.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (dolarSatisDouble < (averageDolarDouble - averageDolarDouble * dDolarRateDouble)) {
				lblDolarAverage.setTextColor(Color.RED);
				if (alarmSet) {
					mPlayer.start();
				}
			}
			if (euroAlisDouble > (averageEuroDouble + averageEuroDouble * iEuroRateDouble)) {
				try {
					lblEuroAverage.setTextColor(Color.GREEN);
					if (alarmSet) {
						mPlayer.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (euroSatisDouble < (averageEuroDouble - averageEuroDouble * dEuroRateDouble)) {
				lblEuroAverage.setTextColor(Color.RED);
				if (alarmSet) {
					mPlayer.start();
				}
			}
			thread = new LongOperation();
			thread.execute("");
			// might want to change "executed" for the returned string passed
			// into onPostExecute() but that is upto you
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

		public double transformToDouble(String str) {
			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
			Number number;
			double d = 0;
			try {
				number = format.parse(str);
				d = number.doubleValue();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return d;
		}
	}
}
