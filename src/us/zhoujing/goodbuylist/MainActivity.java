package us.zhoujing.goodbuylist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import us.zhoujing.goodbuylist.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	GridView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String strUrl = "http://goodbuylist.com/service/getListItemsJSON?listId=248";

		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute(strUrl);

	}

	public void scanNow(View view) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		startActivityForResult(intent, 0);
		
	}

	public void signNow(View view) {
		Intent nextScreen = new Intent(getApplicationContext(),
				SignInActivity.class);
		startActivity(nextScreen);
	}

	public void searchNow(View view) {
		Intent nextScreen = new Intent(getApplicationContext(),
				SearchActivity.class);
		startActivity(nextScreen);
	}

	//For barcode scanning feature
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Log.i("xZing", "contents: " + contents + " format: " + format);
				
				Intent nextScreen = new Intent(getApplicationContext(),
						ProductActivity.class);
				nextScreen.putExtra("barcode", contents);
				// nextScreen.putExtra("email",
				// inputEmail.getText().toString());
				// Log.e("n", inputName.getText()+"."+ inputEmail.getText());
				startActivity(nextScreen);

			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Log.i("xZing", "Cancelled");
			}
		}

	}
	
	/** AsyncTask to download json data */
	private class DownloadTask extends AsyncTask<String, Void, SimpleAdapter> {

		List<HashMap<String, Object>> products;

		@Override
		protected SimpleAdapter doInBackground(String... url) {
			try {
				String data = ProductListJSONParser.downloadUrl(url[0]);
				ProductListJSONParser productListJSONParser = new ProductListJSONParser();
				products = productListJSONParser.parse(data);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}

			String[] from = { "pic" };
			int[] to = { R.id.iv_pic };
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
					products, R.layout.lv_product, from, to);
			return adapter;
		}

		@Override
		protected void onPostExecute(SimpleAdapter adapter) {

			mListView = (GridView) findViewById(R.id.lv_products);
			mListView.setAdapter(adapter);

			OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					Intent nextScreen = new Intent(getApplicationContext(),
							SignInActivity.class);
					startActivity(nextScreen);
				}
			};

			mListView.setOnItemClickListener(mMessageClickedHandler);

			for (int i = 0; i < adapter.getCount(); i++) {
				HashMap<String, Object> hm = (HashMap<String, Object>) adapter
						.getItem(i);
				hm.put("position", i);
				ImageLoaderTask imageLoaderTask = new ImageLoaderTask();
				imageLoaderTask.execute(hm);
			}

		}
	}

	/** AsyncTask to download and load an image in ListView */
	private class ImageLoaderTask extends
			AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {

		@Override
		protected HashMap<String, Object> doInBackground(
				HashMap<String, Object>... hm) {

			InputStream iStream = null;
			String imgUrl = (String) hm[0].get("picUrl");
			int position = (Integer) hm[0].get("position");
			HashMap<String, Object> hmBitmap = null;

			URL url;
			try {
				url = new URL(imgUrl);
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				urlConnection.connect();
				iStream = urlConnection.getInputStream();

				File cacheDirectory = getBaseContext().getCacheDir();
				File tmpFile = new File(cacheDirectory.getPath() + "/wpta_"
						+ position + ".png");
				FileOutputStream fOutStream = new FileOutputStream(tmpFile);
				Bitmap b = BitmapFactory.decodeStream(iStream);
				b.compress(Bitmap.CompressFormat.PNG, 100, fOutStream);

				fOutStream.flush();
				fOutStream.close();

				// Create a hashmap object to store image path and its position
				// in the listview
				hmBitmap = new HashMap<String, Object>();

				hmBitmap.put("pic", tmpFile.getPath());
				hmBitmap.put("position", position);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			return hmBitmap;
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			// Getting the path to the downloaded image
			String path = (String) result.get("pic");
			int position = (Integer) result.get("position");

			SimpleAdapter adapter = (SimpleAdapter) mListView.getAdapter();

			HashMap<String, Object> hm = (HashMap<String, Object>) adapter
					.getItem(position);
			hm.put("pic", path);

			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
