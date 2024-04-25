package data;

import java.io.FileWriter;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BinanceCrawl {
    private static String binancecsvpath = "binance.csv";
    private static void binance(){
    	System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.binance.com/vi/nft/ranking?tab=trend&chain=ALL");
        List<WebElement> elements = driver.findElements(By.cssSelector("#__APP > div > div.css-1pdrb2t > main > div > div > div > div.css-vurnku > div > div.css-m3366f > div"));
        int i = 1;
        try(FileWriter csvWriter = new FileWriter(binancecsvpath)){
         csvWriter.append("#,collection,volume,volume change,floor price,price change,owners,items\n");
         for (WebElement element : elements) {
             String word = element.getText();
             word = word.replace(",","");
             word = word.replace("\n%","%");
             word = word.replace("\n",",");
             if (i<4) {
              String number = String.valueOf(i);
              csvWriter.append(number+","+word+"\n");
              i++;
             }
             else{
              csvWriter.append(word+"\n");
             }
          }       
        }catch (Exception e) {
          e.printStackTrace();
        }
        driver.quit();
      }

public static void main(String[] args) throws Exception {
    binance();
}
}