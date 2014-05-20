package us.zhoujing.goodbuylist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


public class SignInDoneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_done);
	}
	
	public void backPre(View view) {
		Intent intent3 = new Intent(getApplicationContext(), SignInActivity.class);
		startActivity(intent3);
	}

	public void useract(View view) {
		Intent intent3 = new Intent(getApplicationContext(), UserActivity.class);
		startActivity(intent3);
	}
	
	public void peopleact(View view) {
		Intent intent3 = new Intent(getApplicationContext(), PeopleActivity.class);
		startActivity(intent3);
	}

	public void favoriteact(View view) {
		Intent intent3 = new Intent(getApplicationContext(), FavoriteActivity.class);
		startActivity(intent3);
	}

	public void newsfeed(View view) {
		Intent intent3 = new Intent(getApplicationContext(), NewsfeedActivity.class);
		startActivity(intent3);
	}
	
	public void purchaselist(View view) {
		Intent intent3 = new Intent(getApplicationContext(), PurchaseActivity.class);
		startActivity(intent3);
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
