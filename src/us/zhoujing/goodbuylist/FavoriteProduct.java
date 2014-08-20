package us.zhoujing.goodbuylist;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import us.zhoujing.goodbuylist.lib.HtmlParseUtil;
import us.zhoujing.goodbuylist.lib.ProductListJSONParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class FavoriteProduct extends Fragment{
	
	ListView mListView;
	AppGoodbuylist myapp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.favoriteproduct, container, false);
		myapp = (AppGoodbuylist)getActivity().getApplication();
		
		String[] input = new String[2];
		input[0] = myapp.getCookie();
		input[1] = "http://goodbuylist.com/user/viewProductLists/userId/" + myapp.getUserId();
		Log.e("tag", input[1]);
		mListView = (ListView)rootView.findViewById(R.id.favorite_list);
		
		DownloadTask downloadTask = new DownloadTask(getActivity(), mListView);
		downloadTask.execute(input);

		return rootView;
	}

	private class DownloadTask extends AsyncTask<String, Void, SimpleAdapter> {

		List<HashMap<String, Object>> products;
		ListView mListView;
        Activity mContex;
        ProgressDialog progDailog = new ProgressDialog(getActivity());
        SimpleAdapter adapter = null;

        
        private  DownloadTask(Activity contex,ListView listview)
        {
         this.mContex=contex;
         this.mListView=listview;
        }
        
        
		
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

		@Override
		protected SimpleAdapter doInBackground(String... url) {
			try {
				String data = HtmlParseUtil.getPageContent(url[0],url[1]);
				if (data.equals("0")){
					return adapter;
				}
				products = HtmlParseUtil.ParseFavorList(data);
				Log.e("tag", "done parsing" + products.size());
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}

			String[] from = { "pic", "listname", "totalnum" };
			int[] to = { R.id.iv_pic, R.id.tv_product_details1, R.id.tv_product_details2 };
			SimpleAdapter adapter = new SimpleAdapter(mContex,
					products, R.layout.lv_product, from, to);
			Log.e("tag","new adapter");
			return adapter;
		}

		@Override
		protected void onPostExecute(SimpleAdapter adapter) {

			if (adapter == null){
				Intent intent = new Intent(mContex, SignInActivity.class);
		        intent.putExtra("finish", true);
		        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
		        startActivity(intent);
			}
			else{
			mListView.setAdapter(adapter);

			OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					Intent nextScreen = new Intent(mContex,
							ProductListActivity.class);
					HashMap<String, Object> temp = (HashMap<String, Object>)parent.getAdapter().getItem(position);
					String url = (String)temp.get("href");
					nextScreen.putExtra("url", url);
					Log.e("tag", url);
					startActivity(nextScreen);
				}
			};
			

			mListView.setOnItemClickListener(mMessageClickedHandler);

			for (int i = 0; i < adapter.getCount(); i++) {
				HashMap<String, Object> hm = (HashMap<String, Object>) adapter
						.getItem(i);
				hm.put("position", i);
				ImageLoaderTask imageLoaderTask = new ImageLoaderTask(getActivity(), mListView);
				imageLoaderTask.execute(hm);
			}
			
			progDailog.dismiss();
			}
		}
	}

	/** AsyncTask to download and load an image in ListView */
	private class ImageLoaderTask extends
			AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {

		ListView mListView;
        Activity mContex;
        
        private  ImageLoaderTask(Activity contex,ListView listview)
        {
         this.mListView=listview;
         this.mContex=contex;
        }
        
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

				File cacheDirectory = mContex.getCacheDir();
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

			SimpleAdapter adapter = (SimpleAdapter) mListView.getAdapter();

			HashMap<String, Object> hm = (HashMap<String, Object>) adapter
					.getItem(position);
			hm.put("pic", path);

			adapter.notifyDataSetChanged();
		}
	}
}
