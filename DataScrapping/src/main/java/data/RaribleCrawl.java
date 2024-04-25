package data;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RaribleCrawl {
	
	public static WebDriver driver;
	public static int loadMore = 3;
	
	public static void main(String[] args) {
    	System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        
        driver.get("https://rarible.com/explore/ethereum/collections?period=WEEK");
        Long lastHeight = 0L;
        Set<String> uniqueStrings = new HashSet<>();
        List<String> rankings = new ArrayList<>();
        List<String> collectionNames = new ArrayList<>();
        List<String> floorPrices = new ArrayList<>();
        int i = 0;
        JavascriptExecutor js = (JavascriptExecutor) driver;   
        
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        while (i < loadMore) {
        	List<WebElement> elements = driver.findElements(By.xpath("//div[@class='ReactVirtualized__Grid__innerScrollContainer']/div"));
            
        	for (WebElement element : elements) {
        		try {
                    // Add each unique string to the set
                    uniqueStrings.add(element.getText());                 
                } catch (StaleElementReferenceException e) {
                    // Handle StaleElementReferenceException by ignoring and continuing with the loop
                    System.out.println("Stale Element Reference Exception. Trying again...");
                }
            }
            
            js.executeScript("window.scrollTo(arguments[0], arguments[1])", lastHeight, lastHeight + 1440);
            
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            lastHeight = lastHeight + 1440;
            i++;
        }
        
        
        for (String uniqueString : uniqueStrings) {
        	String[] lines = uniqueString.split("\n");
        	rankings.add(lines[0]);
        	collectionNames.add(lines[1]);
        	floorPrices.add(lines[2]);
        }
        
        try (FileWriter file = new FileWriter("rarible.json")) {
            for (int r = 0; r < rankings.size(); r++) {
                String ranking = rankings.get(r);
                String collectionName = collectionNames.get(r);
                String floorPrice = floorPrices.get(r);                                          

                // Write the formatted JSON object to the file with a line break
                file.write("{\n");
                file.write("	\"ranking\":\"" + ranking + "\",\n");
                file.write("	\"name\":\"" + collectionName + "\",\n"); 
                file.write("	\"floor price\":\"" + floorPrice + "\"\n");                            
                file.write("}");
                if (i < rankings.size() - 1) {
                    file.write(",\n");
                } else {
                    file.write("\n");
                }
            }
            System.out.println("JSON objects written to rarible.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        driver.quit();
	}

}
