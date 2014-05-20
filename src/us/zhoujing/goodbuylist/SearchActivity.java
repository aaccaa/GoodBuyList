package us.zhoujing.goodbuylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
	
	public void backPre(View view) {
		Intent intent3 = new Intent(getApplicationContext(), SignInDoneActivity.class);
		startActivity(intent3);
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

}
