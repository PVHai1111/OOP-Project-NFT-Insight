package path;

import getdatafromjson.JsonParser;

public class PathTweet {
	JsonParser jsonParser = new JsonParser();
	private String pathTweet = jsonParser.getJSONFromFile("D:\\java\\OOP_Nhom21\\src\\filejson\\twitter.json");

	public String getPathTweet() {
		return pathTweet;
	}
}
