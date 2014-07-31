package us.zhoujing.goodbuylist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


public class SignInActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
	}
	public void signIn(View view) {
		Intent intent1 = new Intent(getApplicationContext(), SignInDoneActivity.class);
		startActivity(intent1);
	}
	
	public void signFacebook(View view) {
		Intent intent2 = new Intent(getApplicationContext(), SignFacebookActivity.class);
		startActivity(intent2);
	}
	
	public void forgetPassword(View view) {
		Intent intent2 = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
		startActivity(intent2);
	}
	public void backPre(View view) {
		Intent intent3 = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent3);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}

}
