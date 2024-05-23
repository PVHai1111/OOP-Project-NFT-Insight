package path;

import getdatafromjson.JsonParser;
import java.io.InputStream;

public class PathCollection {
    JsonParser jsonParser = new JsonParser();
    private String pathBinance = getResourceFileContent("/filejson/binance.json");

    public String getPathBinance() {
        return pathBinance;
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

