package us.zhoujing.goodbuylist;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class SearchActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		SearchCategory fp = new SearchCategory();
		
		ft.add(R.id.fragment_holder_search, fp);
		ft.commit();
	}
	
	public void goToMenu(View view) {
		Intent intent = new Intent(getApplicationContext(), SignInDoneActivity.class);
		startActivity(intent);
	}

	public void searchproduct(View view) {
		Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
		startActivity(intent);
	}

	public void scanNow(View view) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE",
				"QR_CODE_MODE");
		startActivityForResult(intent, 0);
		
	}
	
//do operation on scan result	
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		if(requestCode == 0)     
		{         
			if(resultCode == RESULT_OK)         
			{             
				String contents = intent.getStringExtra("SCAN_RESULT");  
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				Log.e("xZing", "contents: "+contents+" format: "+format); 
				
				Intent nextScreen = new Intent(getApplicationContext(), SearchResultActivity.class);
				nextScreen.putExtra("barcode", contents);
				startActivity(nextScreen);

				}         
			else if(resultCode == RESULT_CANCELED) Log.e("xZing", "Cancelled");       
		}
	}


	public void onSelectFragment(View view) {
		
		Fragment newFragment;
		
		if (view == findViewById(R.id.btsearchcategory)){
			newFragment = new SearchCategory();
		} 
		else if (view == findViewById(R.id.btsearchmost)){
			newFragment = new SearchMostFollow();
		}
		else if (view == findViewById(R.id.btsearchatoz)){
			newFragment = new SearchAToZ();
		}
		else{
			newFragment = new SearchCategory();
		}
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_holder_search, newFragment);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}


}
