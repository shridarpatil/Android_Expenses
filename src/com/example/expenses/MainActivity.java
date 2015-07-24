package com.example.expenses;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity{
	EditText ed_i1, ed_i2, ed_i3, ed_i4, ed_i5, ed_a1, ed_a2, ed_a3, ed_a4,
			ed_a5;
	Button btnSave, btnDisp;

	JSONArray arr = new JSONArray();
	JSONObject obj1 = new JSONObject();
	JSONObject obj2 = new JSONObject();
	JSONObject obj3 = new JSONObject();
	JSONObject obj4 = new JSONObject();
	JSONObject obj5 = new JSONObject();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ed_i1 = (EditText) findViewById(R.id.et_item1);
		ed_i2 = (EditText) findViewById(R.id.et_item2);
		ed_i3 = (EditText) findViewById(R.id.et_item3);
		ed_i4 = (EditText) findViewById(R.id.et_item4);
		ed_i5 = (EditText) findViewById(R.id.et_item6);

		ed_a1 = (EditText) findViewById(R.id.et_amt1);
		ed_a2 = (EditText) findViewById(R.id.et_amt2);
		ed_a3 = (EditText) findViewById(R.id.et_amt3);
		ed_a4 = (EditText) findViewById(R.id.et_amt4);
		ed_a5 = (EditText) findViewById(R.id.et_amt5);

		btnSave = (Button) findViewById(R.id.btnsave);
		btnDisp = (Button) findViewById(R.id.btndisplay);
		final Intent i = new Intent(this, List.class);

		btnDisp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(i);
			}
		});

		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (ed_i1.getText().toString() != ""
							&& ed_a1.getText().toString() != "") {
						obj1.put("item", ed_i1.getText().toString());
						obj1.put("amount",
								Integer.parseInt(ed_a1.getText().toString()));

						arr.put(obj1);
					}

					obj2.put("item", ed_i2.getText().toString());
					obj2.put("amount",
							Integer.parseInt(ed_a2.getText().toString()));

					arr.put(obj2);

					obj3.put("item", ed_i3.getText().toString());
					obj3.put("amount",
							Integer.parseInt(ed_a3.getText().toString()));

					arr.put(obj3);

					obj4.put("item", ed_i4.getText().toString());
					obj4.put("amount",
							Integer.parseInt(ed_a4.getText().toString()));

					arr.put(obj4);

					obj5.put("item", ed_i5.getText().toString());
					obj5.put("amount",
							Integer.parseInt(ed_a5.getText().toString()));

					arr.put(obj5);

					MyAsyncTask asyncTask = (MyAsyncTask) new MyAsyncTask()
							.execute(arr.toString());
					
					
					Toast.makeText(getApplicationContext(), asyncTask.get(),
							10000).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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

		/*public String getVal() {
			return rtVal;
		}*/

		protected void onProgressUpdate(Integer... progress) {
		}

		public void postData(String arr) {
			HttpClient httpClient = new DefaultHttpClient(); // Deprecated

			try {
				HttpPost request = new HttpPost(
						"http://192.168.1.10/insert.php");
				StringEntity params = new StringEntity(arr);
				request.addHeader("content-type", "application/json");
				request.setEntity(params);
				HttpResponse response = httpClient.execute(request);
				InputStream ips = response.getEntity().getContent();
				BufferedReader buf = new BufferedReader(new InputStreamReader(
						ips, "UTF-8"));
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
					throw new Exception(response.getStatusLine()
							.getReasonPhrase());
				}
				StringBuilder sb = new StringBuilder();
				String s;
				while (true) {
					s = buf.readLine();
					if (s == null || s.length() == 0)
						break;
					sb.append(s);

				}
				buf.close();
				ips.close();

				rtVal = sb.toString();

			} catch (Exception ex) {
				throw new RuntimeException();
				// handle exception here
			} finally {
				httpClient.getConnectionManager().shutdown();
			}
		}

	}
}