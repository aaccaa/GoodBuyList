package us.zhoujing.goodbuylist;

import us.zhoujing.goodbuylist.lib.HtmlParseUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class SignInActivity extends Activity {
	
	ImageButton login;
	EditText inputEmail;
	EditText inputPassword;
	TextView textview;
	String url = "http://goodbuylist.com/user/loginGBL";
	AppGoodbuylist myapp;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		inputEmail = (EditText) findViewById(R.id.editTextUserName);
		inputPassword = (EditText) findViewById(R.id.editTextPassword);
		login = (ImageButton) findViewById(R.id.imageButton1);
		textview = (TextView)findViewById(R.id.textView1);
		
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				String[] exeInput = new String[3];
				exeInput[0] = url;
				exeInput[1] = email;
				exeInput[2] = password;

				DownloadTask downloadTask = new DownloadTask();
				downloadTask.execute(exeInput);

			}
		});
		
	}
	
	
	public void signFacebook(View view) {
		Intent intent = new Intent(getApplicationContext(), SignFacebookActivity.class);
		startActivity(intent);
	}
	
	public void forgetPassword(View view) {
		Intent intent = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}
	
	private class DownloadTask extends AsyncTask<String, Integer, String> {
		String result = "";

		@Override
		protected String doInBackground(String... url) {
			try {
				result = HtmlParseUtil.signInUrl(url[0], url[1], url[2]);
				
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			if (!result.equals("0")){
				myapp = (AppGoodbuylist)getApplication();
				myapp.setCookie(result);
			
				Intent intent = new Intent(SignInActivity.this,
					SignInDoneActivity.class);
				startActivity(intent);
		}else{
			textview.setText("login failed");
		}
	}
	}


}
