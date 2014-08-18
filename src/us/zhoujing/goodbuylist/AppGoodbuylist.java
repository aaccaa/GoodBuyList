package us.zhoujing.goodbuylist;

import android.app.Application;

public class AppGoodbuylist extends Application{
	
		private String cookie;
		private String userId;
		
		public String getCookie(){
			return cookie;
		}
		
		public void setCookie(String cookies){
			this.cookie = cookies;
		}

		public String getUserId(){
			return userId;
		}
		
		public void setUser(String userIds){
			this.userId = userIds;
		}

		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			super.onCreate();
			cookie = "";
			userId = "";
		}

	}
