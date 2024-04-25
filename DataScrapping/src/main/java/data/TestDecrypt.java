package data;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TestDecrypt extends NewsCrawler {

    // Declare the variables
    private List<WebElement> titles;
    private List<WebElement> authors;
    private List<WebElement> dates;
    private List<WebElement> tags;

    public TestDecrypt() {
        super(15); // Set the loadMore value
    }

    @Override
    protected void crawlNews(String url) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();

        driver.get(url);
        clickLoadMore();

        // Add logic to fetch titles, authors, dates, and tags
        // For example:
        titles = driver.findElements(By.xpath("//span[@class='font-medium']"));
        authors = driver.findElements(By.xpath("//article[@class='max-w-[764px] max-w-3xl']/article/div[2]/div/article/div[2]/footer/div/p/span[1]"));
        dates = driver.findElements(By.xpath("//h4"));
        tags = driver.findElements(By.xpath("//p[@class='text-cc-pink-2 scene:text-scene-600 mb-1 gg-dark:text-gg-green gg-dark:font-poppins font-akzidenz-grotesk font-normal text-sm leading-none scene:font-itc-avant-garde-gothic-pro degen-alley-dark:text-degen-alley-primary']"));
    }

    @Override
    protected void exportToJson(String outputFileName) {
        try (FileWriter file = new FileWriter(outputFileName)) {
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
                articleJson.put("view", null);

                // Write the formatted JSON object to the file
                writeJsonToFile(file, articleJson, i < titles.size() - 1);
            }
            System.out.println("JSON objects written to " + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeJsonToFile(FileWriter file, JSONObject articleJson, boolean isLast) {
        try {
            file.write(articleJson.toJSONString());
            if (!isLast) {
                file.write(",\n");
            } else {
                file.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    protected String convertDateFormat(String originalDate) {
        try {
            java.util.Date date = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).parse(originalDate);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return localDate.format(formatter);
        } catch (ParseException e) {
            e.printStackTrace();
            return originalDate; // Return the original date if conversion fails
        }
    }


    public static void main(String[] args) {
        TestDecrypt testDecrypt = new TestDecrypt();
        testDecrypt.crawlAndExport("https://decrypt.co/news", "decrypt.json");
    }
}
