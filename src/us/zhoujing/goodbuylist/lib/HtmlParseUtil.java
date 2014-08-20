package us.zhoujing.goodbuylist.lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import us.zhoujing.goodbuylist.R;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HtmlParseUtil {

	// Sign in by email and password
	// if success, return cookie; otherwise return 0;
	public static String signInUrl(String url, String email, String password)
			throws IOException {
		String result = "";
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", email));
		params.add(new BasicNameValuePair("userPassword", password));

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream iStream = httpEntity.getContent();

			String response = getStringByInputsream(iStream);
			
			Log.e("response", response);

			if (response.contains("success")) {

				String token = httpResponse.getFirstHeader("Set-cookie")
						.toString();
				Log.e("token = ", token);
				String[] temp = token.split("[:; ]+");
				result = temp[1];
				Log.e("cookie = ", result);
			} else {
				result = "0";
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.e("result", result);

		return result;
	}

	public static String getStringByInputsream(InputStream iStream) throws IOException{
		String data = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(
				iStream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line + "\n");
		}
		data = sb.toString() + "";
		br.close();
		iStream.close();
		return data;
	}
	
	//after login, get userId by parsing the html
	public static String getUserId(String cookie, String url)
			throws IOException {
		String userId = "";

		String data = getPageContent(cookie, url);

		if (data.contains("var viewedUserId =")) {
			int userIdStart = data.indexOf("var viewedUserId =");
			userId = data.substring(userIdStart + 20, userIdStart + 23);
			
		}
		Log.e("response", userId);

		return userId;
	}
	
	/* get page content without using cookie*/
	public static String getPageContent(String strUrl) throws IOException {
		String data = "";
		
		try {
			URL url = new URL(strUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.connect();
			InputStream iStream = urlConnection.getInputStream();
			String temp_data = getStringByInputsream(iStream);
			if (temp_data.contains("social-buttons.css")){
				data = "0";
			}
			else{
				data = temp_data;
			}

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} 
		return data;
	}

	
	//get the html page content as a string by using cookie
	public static String getPageContent(String cookie, String url)
			throws IOException {
		String data = "";
		
		try {
			Log.e("cookie", cookie);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			post.setHeader("Cookie", cookie);
			HttpResponse httpResponse = httpClient.execute(post);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream iStream = httpEntity.getContent();
			String temp_data = getStringByInputsream(iStream);
			if (temp_data.contains("social-buttons.css")){
				data = "0";
			}
			else{
				data = temp_data;
			}
			
			
			/* to check header details
			Header[] header = httpResponse.getAllHeaders();
			for (int i = 0; i < header.length; i++)
				data1 = data1 + " " + header[i].toString();
			Log.e("hearder length = ", ((Integer) header.length).toString());
			Log.e("hearder  = ", data1);
			 */
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e("tag", "getStringByInputstream");
		return data;
	}

	// parse http://goodbuylist.com/L1xxx to List<HashMap<String, Object>>
    public static List<HashMap<String, Object>> ParseProductHtml(String strHtml) throws IOException {
		List<HashMap<String, Object>> productList = new ArrayList<HashMap<String, Object>>();

		Document doc = Jsoup.parse(strHtml);
	//	Document doc = Jsoup.connect(strHtml).get();
		Elements items = doc.select("div[class=briefItem menuBar]");

		for (Element item : items) {
			HashMap<String, Object> product = new HashMap<String, Object>();

			Element subItem1 = item.children().first();
			Element subItem2 = item.children().last();

			Elements temp1 = subItem1.children();
			for (Element temp11 : temp1) {
				Log.e("tag", "itemname :" + temp11.attr("itemname"));
				product.put("itemname", temp11.attr("itemname"));

				Log.e("tag", "liztItemId : " + temp11.attr("liztItemId"));
				product.put("liztItemId", temp11.attr("liztItemId"));

				Log.e("tag", "viewItemDetailActionUrl : " + temp11.attr("viewItemDetailActionUrl"));
				product.put("viewItemDetailActionUrl",
						temp11.attr("viewItemDetailActionUrl"));

				Log.e("tag" , "href : " + temp11.attr("href"));
				product.put("href", "http://goodbuylist.com" + temp11.attr("href"));

				Elements imagesSrc = temp11
						.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
				Log.e("tag" , "picUrl : " + imagesSrc.attr("src"));
				product.put("picUrl", imagesSrc.attr("src"));

			}

			Elements temp2 = subItem2.select("p[class=priceLarge]");
			Element subtemp2Item1 = temp2.first();
			Element subtemp2Item2 = temp2.last();

			Log.e("tag" , "Price : " + subtemp2Item1.text());
			product.put("Price", "Price : " + subtemp2Item1.text());

			Log.e("tag" , "List Price : " + subtemp2Item2.text());
			product.put("List Price", "List Price : " + subtemp2Item2.text());
			
			product.put("pic", R.drawable.blank);
           
			productList.add(product);
			
			System.out.println("add one product");
		}

		return productList;
	}
    
    //// parse http://goodbuylist.com/user/viewProductLists/userId/xxx to followed list
    public static List<HashMap<String, Object>> ParseFavorList(String strHtml) throws IOException {
		List<HashMap<String, Object>> productList = new ArrayList<HashMap<String, Object>>();

		Document doc = Jsoup.parse(strHtml);
		Element followed = doc.select("div[id=followedLists]").first();
		Elements followedList = followed.select("div[class=normal]");
		
		for (Element item : followedList) {
			HashMap<String, Object> product = new HashMap<String, Object>();
			
			Element subItem = item.children().first();
			Log.e("tag", "href : " + "http://goodbuylist.com" + subItem.attr("href"));
			product.put("href", "http://goodbuylist.com" + subItem.attr("href"));
			
			Element subItem1 = subItem.select("div[class=plImage]").first();
			Element subItem2 = subItem.select("div[rollover=1]").first();
			
			Elements imageSrc = subItem1.select("img[src~=(?i)\\.(png|jpe?g|gif)]");	
			Log.e("tag", "picUrl : " + imageSrc.attr("src"));
			product.put("picUrl", imageSrc.attr("src"));
			
			Elements subItem2List = subItem2.children();
			product.put("listname", subItem2List.first().text());
			product.put("totalnum", subItem2List.last().text());
			
			Log.e("tag", "List Name: " + subItem2List.first().text());
			Log.e("tag", "Number of Items: " + subItem2List.last().text());
			
			product.put("pic", R.drawable.blank);
	          
			
			productList.add(product);
			
			System.out.println("add one list");
		}
		
		return productList;
	}
    
    //// parse http://goodbuylist.com/user/viewProductLists/userId/xxx to purchase list
    public static List<HashMap<String, Object>> ParsePurchaseList(String strHtml) throws IOException {
		List<HashMap<String, Object>> productList = new ArrayList<HashMap<String, Object>>();

		Document doc = Jsoup.parse(strHtml);
		Element owned = doc.select("div[id=userOwnedLists]").first();
		Elements ownedList = owned.select("div[class=normal]");
		
		for (Element item : ownedList) {
			HashMap<String, Object> product = new HashMap<String, Object>();
			
			Element subItem = item.children().first();
			Log.e("tag", "href : " + "http://goodbuylist.com" + subItem.attr("href"));
			product.put("href", "http://goodbuylist.com" + subItem.attr("href"));
			
			Element subItem1 = subItem.select("div[class=plImage]").first();
			Element subItem2 = subItem.select("div[rollover=1]").first();
			
			Elements imageSrc = subItem1.select("img[src~=(?i)\\.(png|jpe?g|gif)]");	
			Log.e("tag", "picUrl : " + imageSrc.attr("src"));
			product.put("picUrl", imageSrc.attr("src"));
			
			Elements subItem2List = subItem2.children();
			product.put("listname", "List Name: " + subItem2List.first().text());
			product.put("totalnum", "Number of Items: " + subItem2List.last().text());
			
			Log.e("tag", subItem2List.first().text());
			Log.e("tag", subItem2List.last().text());
			
			product.put("pic", R.drawable.blank);
	          
			
			productList.add(product);
			
			System.out.println("add one list");
		}
		
		return productList;
	}


}
