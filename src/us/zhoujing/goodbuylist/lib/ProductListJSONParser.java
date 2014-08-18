package us.zhoujing.goodbuylist.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.zhoujing.goodbuylist.R;

import android.util.Log;

/** A class to parse json data */
public class ProductListJSONParser {

	// Receives a JSONObject and returns a list
	public List<HashMap<String, Object>> parse(String strJson) {

		JSONArray jProductList = null;
		try {
			jProductList = new JSONArray(strJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getProductList(jProductList);
	}

	private List<HashMap<String, Object>> getProductList(JSONArray jProductList) {
		int productCount = jProductList.length();
		List<HashMap<String, Object>> productList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> product = null;
		for (int i = 0; i < productCount; i++) {
			try {
				product = getProduct((JSONObject) jProductList.get(i));
				productList.add(product);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return productList;
	}

	// Parsing the Country JSON object
	private HashMap<String, Object> getProduct(JSONObject jProduct) {

		HashMap<String, Object> product = new HashMap<String, Object>();
		String productId = "";
		String name = "";
		String picUrl = "";
		String price = "";
		String url = "";

		try {
			productId = jProduct.getString("id");
			name = jProduct.getString("name");
			picUrl = jProduct.getString("pic");
			price = jProduct.getString("price");
			url = jProduct.getString("url");

			String details = name + "\n" + "Price: " + price + "\n";

			product.put("productId", productId);
			product.put("name", name);
			product.put("pic", R.drawable.blank);
			product.put("picUrl", picUrl);
			product.put("details", details);
			product.put("url", url);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return product;
	}


}