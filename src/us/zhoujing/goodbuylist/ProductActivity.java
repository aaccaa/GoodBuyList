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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ProductActivity extends Activity {

	ListView mListView;

	// Boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);

		Intent i = getIntent();
		String barcode = i.getStringExtra("barcode");
		// TextView txtFormat = (TextView) findViewById(R.id.barcode);

		String strUrl1 = "http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=5C6FECBA-936C-49FA-8E42-3C8B10487F2B&upc=";
		String strUrl = strUrl1 + barcode;

		// txtFormat.setText(strUrl);
		Button btnClose = (Button) findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});

		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute(strUrl);

		mListView = (ListView) findViewById(R.id.lv_products);

	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		try {
			URL url = new URL(strUrl);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

			Log.e("product data", data);

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
		}

		return data;
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {
		String data = null;

		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {

			// The parsing of the xml data is done in a non-ui thread
			ProductViewLoaderTask productViewLoaderTask = new ProductViewLoaderTask();

			// Start parsing xml data
			productViewLoaderTask.execute(result);
		}
	}

	/** AsyncTask to parse json data and load ListView */
	private class ProductViewLoaderTask extends
			AsyncTask<String, Void, SimpleAdapter> {

		// JSONObject jObject;
		// Doing the parsing of xml data in a non-ui thread
		@Override
		protected SimpleAdapter doInBackground(String... strJson) {
			/*
			 * try{ // jObject = new JSONObject(strJson[0]);
			 * ProductListJSONParser productListJSONParser = new
			 * ProductListJSONParser(); productListJSONParser.parse(strJson[0]);
			 * }catch(Exception e){ Log.d("JSON Exception1",e.toString()); }
			 * 
			 * // Instantiating json parser class
			 */
			// A list object to store the parsed countries list
			List<HashMap<String, Object>> products = null;

			try {
				// Getting the parsed data as a List construct
				ProductListJSONParser productListJSONParser = new ProductListJSONParser();
				products = productListJSONParser.parse(strJson[0]);
			} catch (Exception e) {
				Log.d("Exception 2", e.toString());
				// TextView searchResult =
				// (TextView)findViewById(R.id.tv_result);
				// searchResult.setText("oops, I cannot find it !");
				// flag = false;
			}

			// Keys used in Hashmap
			String[] from = { "pic"};

			// Ids of views in listview_layout
			int[] to = { R.id.iv_pic };

			// Instantiating an adapter to store each items
			// R.layout.listview_layout defines the layout of each item
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
					products, R.layout.lv_product, from, to);

			// flag = true;
			return adapter;
		}

		/** Invoked by the Android on "doInBackground" is executed */
		@Override
		protected void onPostExecute(SimpleAdapter adapter) {

			// Setting adapter for the listview
			mListView.setAdapter(adapter);

			// if (!flag) return;
			// else{

			for (int i = 0; i < adapter.getCount(); i++) {
				HashMap<String, Object> hm = (HashMap<String, Object>) adapter
						.getItem(i);
				String imgUrl = (String) hm.get("pic_path");
				Log.e("imgUrl", imgUrl);
				ImageLoaderTask imageLoaderTask = new ImageLoaderTask();

				// HashMap<String, Object> hmDownload = new HashMap<String,
				// Object>();
				hm.put("pic_path", imgUrl);
				hm.put("position", i);

				// Starting ImageLoaderTask to download and populate image in
				// the listview
				imageLoaderTask.execute(hm);
			}
		}

	}

	/** AsyncTask to download and load an image in ListView */
	private class ImageLoaderTask extends
			AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {

		Boolean flag = true;

		@Override
		protected HashMap<String, Object> doInBackground(
				HashMap<String, Object>... hm) {

			InputStream iStream = null;

			String imgUrl = (String) hm[0].get("pic_path");

			if (imgUrl.length() == 0 || imgUrl == null || imgUrl.isEmpty()
					|| imgUrl.equals("N/A") || imgUrl.equals("n/a")) {

				// searchResult.setText("oops, I cannot find it !");
				Log.e("********", "oops, I cannot find it !");
				flag = false;
				HashMap<String, Object> hmBitmap = new HashMap<String, Object>();
				return hmBitmap;

			} else {

				Log.e("********", imgUrl);

				int position = (Integer) hm[0].get("position");

				URL url;
				try {
					url = new URL(imgUrl);

					Log.e("********", url.toString());

					// Creating an http connection to communicate with url
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();

					// Connecting to url
					urlConnection.connect();

					// Reading data from url
					iStream = urlConnection.getInputStream();

					// Getting Caching directory
					File cacheDirectory = getBaseContext().getCacheDir();

					// Temporary file to store the downloaded image
					File tmpFile = new File(cacheDirectory.getPath() + "/wpta_"
							+ position + ".png");

					// The FileOutputStream to the temporary file
					FileOutputStream fOutStream = new FileOutputStream(tmpFile);

					// Creating a bitmap from the downloaded inputstream
					Bitmap b = BitmapFactory.decodeStream(iStream);

					// Writing the bitmap to the temporary file as png file
					b.compress(Bitmap.CompressFormat.PNG, 100, fOutStream);

					// Flush the FileOutputStream
					fOutStream.flush();

					// Close the FileOutputStream
					fOutStream.close();

					// Create a hashmap object to store image path and its
					// position in the listview
					HashMap<String, Object> hmBitmap = new HashMap<String, Object>();

					// Storing the path to the temporary image file
					hmBitmap.put("pic", tmpFile.getPath());

					// Storing the position of the image in the listview
					hmBitmap.put("position", position);

					Log.e("put imgUrl", imgUrl);

					// Returning the HashMap object containing the image path
					// and position
					return hmBitmap;

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			// Getting the path to the downloaded image
			if (flag) {
				String path = (String) result.get("pic");

				Log.e("path", path);

				// Getting the position of the downloaded image
				int position = (Integer) result.get("position");

				// Getting adapter of the listview
				SimpleAdapter adapter = (SimpleAdapter) mListView.getAdapter();

				// Getting the hashmap object at the specified position of the
				// listview
				HashMap<String, Object> hm = (HashMap<String, Object>) adapter
						.getItem(position);

				// Overwriting the existing path in the adapter
				hm.put("pic", path);

				// Noticing listview about the dataset changes
				adapter.notifyDataSetChanged();
			} else {
				TextView searchResult = (TextView) findViewById(R.id.tv_result);
				searchResult.setText("oops, I cannot find it !");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
