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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in_done, menu);
		return true;
	}

}
