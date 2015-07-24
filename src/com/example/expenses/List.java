package com.example.expenses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class List extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		MyAsyncTask asyncTask = (MyAsyncTask) new MyAsyncTask().execute("test");

		try {
			try {
				init(asyncTask.get());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void init(String str) throws JSONException {

		TableLayout stk = (TableLayout) findViewById(R.id.table_main);
		TableRow tbrow0 = new TableRow(this);
		TextView tv0 = new TextView(this);
		tv0.setText(" Item Name ");
		tv0.setTextColor(Color.WHITE);
		tbrow0.addView(tv0);
		TextView tv1 = new TextView(this);
		tv1.setText(" Amount ");
		tv1.setTextColor(Color.WHITE);
		tbrow0.addView(tv1);
		TextView tv2 = new TextView(this);
		tv2.setText(" Created On ");
		tv2.setTextColor(Color.WHITE);
		tbrow0.addView(tv2);
		stk.addView(tbrow0);
		
		JSONArray arr = new JSONArray(str);
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);

			String item = obj.getString("item_name");
			String amt = String.valueOf(obj.getInt("amount"));
			String created_on = obj.getString("created_on");
			
			TableRow tbrow = new TableRow(this);
			TextView t1v = new TextView(this);
			t1v.setText("" + item);
			t1v.setTextColor(Color.WHITE);
			t1v.setGravity(Gravity.CENTER);
			tbrow.addView(t1v);
			TextView t2v = new TextView(this);
			t2v.setText("Rs. " + amt);
			t2v.setTextColor(Color.WHITE);
			t2v.setGravity(Gravity.CENTER);
			tbrow.addView(t2v);
			TextView t3v = new TextView(this);
			t3v.setText("" + created_on);
			t3v.setTextColor(Color.WHITE);
			t3v.setGravity(Gravity.CENTER);
			tbrow.addView(t3v);
			stk.addView(tbrow);			
		}

		
		
/*		for (int i = 0; i < 25; i++) {
			TableRow tbrow = new TableRow(this);
			TextView t1v = new TextView(this);
			t1v.setText("" + i);
			t1v.setTextColor(Color.WHITE);
			t1v.setGravity(Gravity.CENTER);
			tbrow.addView(t1v);
			TextView t2v = new TextView(this);
			t2v.setText("Product " + i);
			t2v.setTextColor(Color.WHITE);
			t2v.setGravity(Gravity.CENTER);
			tbrow.addView(t2v);
			TextView t3v = new TextView(this);
			t3v.setText("Rs." + i);
			t3v.setTextColor(Color.WHITE);
			t3v.setGravity(Gravity.CENTER);
			tbrow.addView(t3v);
			TextView t4v = new TextView(this);
			t4v.setText("" + i * 15 / 32 * 10);
			t4v.setTextColor(Color.WHITE);
			t4v.setGravity(Gravity.CENTER);
			tbrow.addView(t4v);
			stk.addView(tbrow);
		}
*/
	}

	private class MyAsyncTask extends AsyncTask<String, Integer, String> {
		String rtVal = null;

		@Override
		protected String doInBackground(String... params) {
			postData(params[0]);
			return rtVal;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(rtVal);
		}

		/*
		 * public String getVal() { return rtVal; }
		 */

		protected void onProgressUpdate(Integer... progress) {
		}

		public void postData(String arr) {
			InputStream inputStream = null;

			try {

				// create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// make GET request to the given URL
				HttpResponse httpResponse = httpclient.execute(new HttpGet(
						"http://192.168.1.10/get.php"));

				// receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();

				// convert inputstream to string
				if (inputStream != null)
					rtVal = convertInputStreamToString(inputStream);
				else
					rtVal = "Did not work!";

			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}
		}

		private String convertInputStreamToString(InputStream inputStream)
				throws IOException {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = "";
			String result = "";
			while ((line = bufferedReader.readLine()) != null)
				result += line;

			inputStream.close();
			return result;

		}

	}
}
