package us.zhoujing.goodbuylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class PeopleActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		PeopleIFollowFragment peopleIF = new PeopleIFollowFragment();
		
		ft.add(R.id.fragment_holder, peopleIF);
		ft.commit();
	}
	
	public void backPre(View view) {
		Intent intent3 = new Intent(getApplicationContext(), SignInDoneActivity.class);
		startActivity(intent3);
	}

	public void onSelectFragment(View view) {
		
		Fragment newFragment;
		
		if (view == findViewById(R.id.btpeoplefollow)){
			newFragment = new PeopleIFollowFragment();
		} 
		else if (view == findViewById(R.id.btpeopleall)){
			newFragment = new PeopleAllFragment();
		}
		else{
			newFragment = new PeopleIFollowFragment();
		}
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_holder, newFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

}
