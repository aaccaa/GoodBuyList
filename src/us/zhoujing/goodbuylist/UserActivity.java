package us.zhoujing.goodbuylist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class UserActivity extends Activity {
	
	String userId;
	AppGoodbuylist myapp;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_actvity);
		
		String userProfileUrl = "http://goodbuylist.com/user/viewProfile/userId/"
				+ userId;
		
	}
	
	public void goToMenu(View view) {
		Intent intent = new Intent(getApplicationContext(), SignInDoneActivity.class);
		startActivity(intent);
	}

	public void searchproduct(View view) {
		Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
		startActivity(intent);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_actvity, menu);
		return true;
	}

}
