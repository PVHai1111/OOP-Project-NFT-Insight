package path;

import getdatafromjson.JsonParser;
import java.io.InputStream;

public class PathTweet {
    JsonParser jsonParser = new JsonParser();
    private String pathTweet = getResourceFileContent("/filejson/twitter.json");

    public String getPathTweet() {
        return pathTweet;
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
