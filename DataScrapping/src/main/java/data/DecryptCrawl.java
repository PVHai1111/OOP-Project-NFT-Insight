package data;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DecryptCrawl {

    public static WebDriver driver;
    public static int loadMore = 50;
    
    

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        driver.get("https://decrypt.co/news");
        for (int i=0; i<loadMore; i++) {
        	driver.findElement(By.className("mr-4")).click();
        	try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        
        List<WebElement> titles = driver.findElements(By.xpath("//span[@class='font-medium']"));
        List<WebElement> authors = driver.findElements(By.xpath("//article[@class='max-w-[764px] max-w-3xl']/article/div[2]/div/article/div[2]/footer/div/p/span[1]"));
        List<WebElement> dates = driver.findElements(By.xpath("//h4"));
        List<WebElement> tags = driver.findElements(By.xpath("//p[@class='text-cc-pink-2 scene:text-scene-600 mb-1 gg-dark:text-gg-green gg-dark:font-poppins font-akzidenz-grotesk font-normal text-sm leading-none scene:font-itc-avant-garde-gothic-pro degen-alley-dark:text-degen-alley-primary']"));


        try (FileWriter file = new FileWriter("decrypt.json")) {
        	file.write("[\n");
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i).getText();
                String authorText = authors.get(i).getText().replace("by ", "");
                String originalDate = dates.get(i).getText();
                String tag = tags.get(i).getText();

                // Convert the original date format to "27/11/2023"
                String formattedDate = convertDateFormat(originalDate);

                // Create a JSON object for each article
                JSONObject articleJson = new JSONObject();
                articleJson.put("title", title);
                articleJson.put("author", authorText);
                articleJson.put("date", formattedDate);
                articleJson.put("tags", tag);
                articleJson.put("view", "null");                             

                // Write the formatted JSON object to the file with a line break
                file.write("	{\n");
                file.write("		\"title\":\"" + title + "\",\n");
                file.write("		\"author\":\"" + authorText + "\",\n");
                file.write("		\"href\":\"" + "\",\n");
                file.write("		\"date\":\"" + formattedDate + "\",\n");
                file.write("		\"tags\":\"" + tag + "\",\n");
                file.write("		\"views\":\"null\"\n");                              
                file.write("	}");
                if (i < authors.size() - 1) {
                    file.write(",\n");
                } else {
                    file.write("\n");
                }
            }
            file.write("\n]\n");
            System.out.println("JSON objects written to decrypt.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        driver.quit();
    	
    	 
    }
    
    private static String convertDateFormat(String originalDate) {
        try {
            Date date = new SimpleDateFormat("MMM dd, yyyy").parse(originalDate);
            LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return localDate.format(formatter);
        } catch (ParseException e) {
            e.printStackTrace();
            return originalDate; // Return the original date if conversion fails
        }
    }
    
}