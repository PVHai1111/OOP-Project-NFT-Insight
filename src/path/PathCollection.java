package path;

import getdatafromjson.JsonParser;

public class PathCollection {
	JsonParser jsonParser = new JsonParser();
	private String pathBinance = jsonParser.getJSONFromFile("D:\\java\\OOP_Nhom21\\src\\filejson\\binance.json");

	public String getPathBinance() {
		return pathBinance;
	}
}
