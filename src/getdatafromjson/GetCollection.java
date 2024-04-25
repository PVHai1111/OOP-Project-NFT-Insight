package getdatafromjson;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import modal.Collection;

public class GetCollection {

	public ArrayList<Collection> getArrayList(String path) {
		try {
			ArrayList<Collection> CollectionList = new ArrayList<Collection>();
			JSONParser parser = new JSONParser();
			Object object = parser.parse(path);
			JSONArray mainJsonObject = (JSONArray) object;
			for (int i = 0; i < mainJsonObject.size(); i++) {
				Collection Collection = new Collection();
				JSONObject jsonObject = (JSONObject) mainJsonObject.get(i);
				String collection = (String) jsonObject.get("collection");
				Collection.setCollection(collection);
				String volumn = (String) jsonObject.get("volume");
				Collection.setVolumn(volumn);
				String volumn_change = (String) jsonObject.get("volume change");
				Collection.setVolumn_change(volumn_change);
				String floor_price = (String) jsonObject.get("floor price");
				String price_change = (String) jsonObject.get("price change");
				Collection.setFloor_price(floor_price);
				Collection.setPrice_change(price_change);
				CollectionList.add(Collection);
			}
			return CollectionList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
