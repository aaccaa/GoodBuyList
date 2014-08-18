package us.zhoujing.goodbuylist;

import us.zhoujing.goodbuylist.lib.HtmlParseUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class SignInDoneActivity extends Activity {

	AppGoodbuylist myapp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_done);
		
		myapp = (AppGoodbuylist)getApplication();
		String[] input = new String[2];
		input[0] = myapp.getCookie();
		input[1] = "http://goodbuylist.com/user/viewProfile";

		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute(input);

	}
	
	private class DownloadTask extends AsyncTask<String, Integer, String> {
		
		String userId;

		@Override
		protected String doInBackground(String... url) {
			try {
				userId = HtmlParseUtil.getUserId(url[0], url[1]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return userId;
		}

		@Override
		protected void onPostExecute(String userId) {

			if (userId.length() == 0) {
				Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
				startActivity(intent);
			} else {
				myapp.setUser(userId);
				Log.e("userId", userId);
			}
		}
	}
	
	public void useract(View view) {
		Intent intent = new Intent(getApplicationContext(), UserActivity.class);
		startActivity(intent);
	}
	
	public void peopleact(View view) {
		Intent intent = new Intent(getApplicationContext(), PeopleActivity.class);
		startActivity(intent);
	}

	public void favoriteact(View view) {
		Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
		startActivity(intent);
	}

	public void newsfeed(View view) {
		Intent intent = new Intent(getApplicationContext(), NewsfeedActivity.class);
		startActivity(intent);
	}
	
	public void purchaselist(View view) {
		Intent intent = new Intent(getApplicationContext(), PurchaseActivity.class);
		startActivity(intent);
	}
	
	public void createlist(View view) {
		Intent intent = new Intent(getApplicationContext(), CreateListActivity.class);
		startActivity(intent);
	}
	
	public void searchproduct(View view) {
		Intent intent3 = new Intent(getApplicationContext(), SearchActivity.class);
		startActivity(intent3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in_done, menu);
		return true;
	}

}
