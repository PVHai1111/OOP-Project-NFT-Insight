package data;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NiftyGatewayCrawl {

    public static WebDriver driver;

    public static void main(String[] args) {

    	System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        int loadMore = 12;
        int i = 0;

        driver.get("https://www.niftygateway.com/marketplace?sortBy=recent&trending=true");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        Long lastHeight = 0L;

//        Set<String> uniqueCollections = new HashSet<>();
//        Set<String> uniqueFloorPrices = new HashSet<>();
//        Set<String> uniqueCreators = new HashSet<>();
        
        List<String> CollectionNames = new ArrayList<>();
        List<String> FloorPrices = new ArrayList<>();
        List<String> Creators = new ArrayList<>();

        while (i < loadMore) {
//            List<WebElement> collections = driver.findElements(By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 MuiTypography-noWrap css-1r0io98']"));
//            List<WebElement> floorPrices = driver.findElements(By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 css-1fiixjq']/span[1]/span"));
//            List<WebElement> creators = driver.findElements(By.xpath("//div[@class='MuiBox-root css-1jgs0cu']/div[1]/p[2]/a"));

        	List<WebElement> collections = driver.findElements(By.xpath("//div[@role='cell']"));
            for (int k = 0; k < collections.size(); k++) {
            	WebElement currentCollection = collections.get(k);
            	CollectionNames.add(currentCollection.findElement(By.xpath("//div[@role='cell']/a/div/div[2]/p[1]")).getText());
                FloorPrices.add(currentCollection.findElement(By.xpath("//div[@role='cell']/a/div/div[2]/p[2]/span[1]/span")).getText());
                Creators.add(currentCollection.findElement(By.xpath("//div[@role='cell']/a/div/div[2]/div[2]/div[1]/p[2]/a")).getText());
                
            }

            js.executeScript("window.scrollTo(arguments[0], arguments[1])", lastHeight, lastHeight + 1440);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lastHeight = lastHeight + 2500;
            i++;
        }

        try (FileWriter file = new FileWriter("nifty_gateway.json")) {

            for (int j = 0; j < CollectionNames.size(); j++) {
                String collectionName = CollectionNames.get(j);
                String floorPrice = FloorPrices.get(j);
                String creator = Creators.get(j);

                JSONObject articleJson = new JSONObject();
                articleJson.put("collectionName", collectionName);
                articleJson.put("floor price", floorPrice);
                articleJson.put("creator", creator);

                file.write("{\n");
                file.write("	\"collection\":\"" + collectionName + "\",\n");
                file.write("	\"floor price\":\"" + floorPrice + "\",\n");
                file.write("	\"creator\":\"" + creator + "\"\n");
                file.write("}");
                if (j < CollectionNames.size() - 1) {
                    file.write(",\n");
                } else {
                    file.write("\n");
                }
            }
            System.out.println("JSON objects written to nifty_gateway.json");

        } catch (IOException e) {
            e.printStackTrace();
        }

        driver.quit();
    }
}
