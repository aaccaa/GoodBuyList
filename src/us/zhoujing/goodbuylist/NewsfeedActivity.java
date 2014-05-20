package us.zhoujing.goodbuylist;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class NewsfeedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsfeed);
	}
	
	public void backPre(View view) {
		Intent intent1 = new Intent(getApplicationContext(), SignInDoneActivity.class);
		startActivity(intent1);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.newsfeed, menu);
		return true;
	}

}
