package us.zhoujing.goodbuylist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import us.zhoujing.goodbuylist.R;

import android.util.Log;

//import us.zhoujing.listviewwithjsonfromurl.R;

/** A class to parse json data */
public class ProductListJSONParser {

	// Receives a JSONObject and returns a list
	public List<HashMap<String, Object>> parse(String strJson) {

		JSONArray jProductList = new JSONArray();
		try {
			// Retrieves all the elements in the 'countries' array
			// jProductList = new JSONArray(strJson);

			JSONObject jsonResponse = new JSONObject(strJson);
			// jProductList = jsonResponse.names();
			// jProductList = jsonResponse.getJSONArray()

			for (int i = 0; i < jsonResponse.length(); i++) {

				String name = Integer.toString(i);
				JSONObject item = jsonResponse.getJSONObject(name);
				jProductList.put(i, item);
			}

			Log.e("jProductList length",
					((Integer) (jProductList.length())).toString());

			// JSONObject jsonResponse1 = jsonResponse.getJSONObject("0");
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("product data", "Json wrong1");
		}

		// Invoking getCountries with the array of json object
		// where each json object represent a country
		return getProductList(jProductList);
	}

	private List<HashMap<String, Object>> getProductList(JSONArray jProductList) {
		int productCount = jProductList.length();
		List<HashMap<String, Object>> productList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> product = null;

		// Taking each country, parses and adds to list object
		for (int i = 0; i < productCount; i++) {
			try {
				// Call getCountry with country JSON object to parse the country
				product = getProduct((JSONObject) jProductList.get(i));
				productList.add(product);

			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("product data", "Json wrong2");

			}
		}

		return productList;
	}

	// Parsing the Country JSON object
	private HashMap<String, Object> getProduct(JSONObject jProduct) {

		HashMap<String, Object> product = new HashMap<String, Object>();
		String productId = "";
		String name = "";
		String pic = "";
		String price = "";
		String currency = "";
		String url = "";

		try {
			// productId = jProduct.getString("id");
			name = jProduct.getString("productname");
			pic = jProduct.getString("imageurl");
			// price = jProduct.getString("price");
			// currency = jProduct.getString("currency");

			String details = ""; // name + "\n" + "Price: " + price + "\n";

			product.put("name", name);
			product.put("pic", R.drawable.blank);
			product.put("pic_path", pic);
			product.put("details", name);

			Log.e("product name", name);
			Log.e("product url", pic);

		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("product data", "Json wrong3");

		}
		return product;
	}
}