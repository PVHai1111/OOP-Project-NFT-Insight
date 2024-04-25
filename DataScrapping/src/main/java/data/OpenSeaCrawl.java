package data;


import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class OpenSeaCrawl {

    private static String openseacsvpath = "opensea.csv";

    private static void opensea() {
    	System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://opensea.io/rankings/trending?sortBy=one_day_volume");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long pageHeight = (Long) js.executeScript("return document.body.scrollHeight");
        Long lastHeight = 0L;
        Set<String> uniqueStrings = new HashSet<>();

        while (lastHeight <= pageHeight) {
            List<WebElement> elements = driver.findElements(By.cssSelector("#main > main > div > div > div.sc-beff130f-0.fFLSTY > div > div:nth-child(6) > div > div > a"));

            for (WebElement element : elements) {
                uniqueStrings.add(element.getText());
            }

            js.executeScript("window.scrollTo(arguments[0], arguments[1])", lastHeight, lastHeight + 1440);

            try {
                Thread.sleep(500); // Adjust the sleep time as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lastHeight = lastHeight + 1440;
        }

        try (FileWriter csvWriter = new FileWriter(openseacsvpath)) {
            StringBuilder header = new StringBuilder("#,collection,volume,change,floor price,sales\n");
            for (String str : uniqueStrings) {
                str = str.replace("\n", ",");
                header.append(str).append("\n");
            }
            csvWriter.append(header.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        driver.quit();
    }


    public static void main(String[] args) throws Exception {
        opensea();
    }
}