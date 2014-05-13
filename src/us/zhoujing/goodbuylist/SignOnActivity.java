package us.zhoujing.goodbuylist;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class SignOnActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		// ImageView imageView = (ImageView) findViewById(R.id.);

	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */
	public void signIn(View view) {
		Intent intent1 = new Intent(getApplicationContext(), UserActivity.class);
		startActivity(intent1);
	}
	
	public void signFacebook(View view) {
		Intent intent2 = new Intent(getApplicationContext(), SignFacebookActivity.class);
		startActivity(intent2);
	}
	
	public void backPre(View view) {
		Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent3);
	}


}
