package path;

import getdatafromjson.JsonParser;

public class PathBlog {
	JsonParser jsonParser = new JsonParser();
	private String pathCointelegraph = jsonParser
			.getJSONFromFile("D:\\java\\OOP_Nhom21\\src\\filejson\\cointelegraph.json");
	private String pathDecrypt = jsonParser.getJSONFromFile("D:\\java\\OOP_Nhom21\\src\\filejson\\decrypt.json");

	public String pathCoin() {
		return this.pathCointelegraph;
	}

	public String pathDecrypt() {
		return this.pathDecrypt;
	}
}
