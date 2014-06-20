package us.zhoujing.goodbuylist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SignFacebookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_facebook);
	}

	public void signIn(View view) {
		Intent intent1 = new Intent(getApplicationContext(), SignInDoneActivity.class);
		startActivity(intent1);
	}
	
	
	public void backPre(View view) {
		Intent intent3 = new Intent(getApplicationContext(), SignInActivity.class);
		startActivity(intent3);
	}

}
