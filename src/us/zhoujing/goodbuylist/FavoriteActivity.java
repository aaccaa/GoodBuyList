package us.zhoujing.goodbuylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class FavoriteActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		FavoriteProduct fp = new FavoriteProduct();
		
		ft.add(R.id.fragment_holder_favorite, fp);
		ft.commit();
	}
	
	public void backPre(View view) {
		Intent intent3 = new Intent(getApplicationContext(), SignInDoneActivity.class);
		startActivity(intent3);
	}

	public void onSelectFragment(View view) {
		
		Fragment newFragment;
		
		if (view == findViewById(R.id.btfavoriteproduct)){
			newFragment = new FavoriteProduct();
		} 
		else if (view == findViewById(R.id.btfavoriteuser)){
			newFragment = new FavoriteUser();
		}
		else{
			newFragment = new FavoriteProduct();
		}
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_holder_favorite, newFragment);
		ft.addToBackStack(null);
		ft.commit();
	}

}
