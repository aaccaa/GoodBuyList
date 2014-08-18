package us.zhoujing.goodbuylist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import us.zhoujing.goodbuylist.R;
import us.zhoujing.goodbuylist.lib.HtmlParseUtil;
import us.zhoujing.goodbuylist.lib.ProductListJSONParser;
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
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	GridView mGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mGridView = (GridView) findViewById(R.id.lv_products);
		
		String strUrl = "http://goodbuylist.com/service/getListItemsJSON?listId=248";

		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute(strUrl);

	}
/*	
    @Override
    protected void onDestroy() {
    	super.onDestroy();

    	unbindDrawables(findViewById(R.id.RootView));
    	System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
        	view.getBackground().setCallback(null);
        }
       // if (view instanceof ViewGroup) {
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            	unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup)view).removeAllViews();
        }
    }

*/
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

	public void scanNow(View view) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		startActivityForResult(intent, 0);
		
	}

	/* for barcode scanning feature
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
	*/
	
	/* AsyncTask to download json data */
	private class DownloadTask extends AsyncTask<String, Void, SimpleAdapter> {

		List<HashMap<String, Object>> products;

		@Override
		protected SimpleAdapter doInBackground(String... url) {
			try {
				String data = HtmlParseUtil.getPageContent(url[0]);
				ProductListJSONParser productListJSONParser = new ProductListJSONParser();
				products = productListJSONParser.parse(data);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}

			String[] from = { "pic" };
			int[] to = { R.id.iv_pic };
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(),
					products, R.layout.main_product, from, to);
			return adapter;
		}

		@Override
		protected void onPostExecute(SimpleAdapter adapter) {

			mGridView.setAdapter(adapter);

			OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					Intent nextScreen = new Intent(getApplicationContext(),
							SignInActivity.class);
					startActivity(nextScreen);
				}
			};

			mGridView.setOnItemClickListener(mMessageClickedHandler);

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

			SimpleAdapter adapter = (SimpleAdapter) mGridView.getAdapter();

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
