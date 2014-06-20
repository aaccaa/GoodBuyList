package us.zhoujing.goodbuylist;

import us.zhoujing.goodbuylist.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	public void scanNow(View view) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		startActivityForResult(intent, 0);
		Log.e("test", "button works!");
	}

	public void signNow(View view) {
		Intent nextScreen = new Intent(getApplicationContext(),
				SignInActivity.class);
		Log.e("test", "button works!");

		startActivity(nextScreen);
		Log.e("test", "button works!");

	}

	public void searchNow(View view) {
		Intent nextScreen = new Intent(getApplicationContext(),
				SearchActivity.class);
		Log.e("test", "button works!");

		startActivity(nextScreen);
		Log.e("test", "button works!");

	}

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

}
