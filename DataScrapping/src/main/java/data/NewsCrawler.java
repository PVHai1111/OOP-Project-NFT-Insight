package data;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

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

import org.json.JSONObject;

public abstract class NewsCrawler {

    protected WebDriver driver;
    protected int loadMore;

    public NewsCrawler(int loadMore) {
        this.loadMore = loadMore;
    }



	public void crawlAndExport(String url, String outputFileName) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();

        try {
            crawlNews(url);
            exportToJson(outputFileName);
        } finally {
            driver.quit();
        }
    }

    protected abstract void crawlNews(String url);

    protected abstract void exportToJson(String outputFileName);

    protected void clickLoadMore() {
        for (int i = 0; i < loadMore; i++) {
            driver.findElement(By.className("mr-4")).click();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected String convertDateFormat(String originalDate) {
        try {
            Date date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(originalDate);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return localDate.format(formatter);
        } catch (ParseException e) {
            e.printStackTrace();
            return originalDate; // Return the original date if conversion fails
        }
    }

    protected void writeJsonToFile(FileWriter file, JSONObject articleJson, boolean isLast) throws IOException {
        file.write(articleJson.toString(4));
        if (!isLast) {
            file.write(",\n");
        } else {
            file.write("\n");
        }
    }
}
