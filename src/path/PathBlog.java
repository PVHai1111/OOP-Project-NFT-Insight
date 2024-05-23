package path;

import getdatafromjson.JsonParser;
import java.io.InputStream;

public class PathBlog {
    JsonParser jsonParser = new JsonParser();

    private String pathCointelegraph = getResourceFileContent("/filejson/cointelegraph.json");
    private String pathDecrypt = getResourceFileContent("/filejson/decrypt.json");

    public String pathCoin() {
        return this.pathCointelegraph;
    }

    public String pathDecrypt() {
        return this.pathDecrypt;
    }

    private String getResourceFileContent(String resourcePath) {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + resourcePath);
            }
            return new String(inputStream.readAllBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
