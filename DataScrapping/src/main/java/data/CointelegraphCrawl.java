package data;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CointelegraphCrawl {

    public static void main(String[] args) {
        String[] urls = {
                "https://cointelegraph.com/tags/bitcoin",
                "https://cointelegraph.com/tags/blockchain",
                "https://cointelegraph.com/tags/ai",
                "https://cointelegraph.com/tags/altcoin",
                "https://cointelegraph.com/tags/ethereum",
                "https://cointelegraph.com/tags/defi",
                "https://cointelegraph.com/tags/business",
                "https://cointelegraph.com/tags/nft"
        };

        int numPages = 10;
        int delayInSeconds = 1;
        JSONArray jsonArray = new JSONArray();

    	System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Downloads\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            for (String url : urls) {
                processUrl(driver, url, numPages, delayInSeconds, jsonArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exportToJson(jsonArray);
            driver.quit();
        }
    }

    private static void processUrl(WebDriver driver, String url, int numPages, int delayInSeconds, JSONArray jsonArray) throws InterruptedException {
        for (int page = 1; page <= numPages; page++) {
            try {
                driver.get(url + "?page=" + page);
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

                List<WebElement> articles = driver.findElements(By.cssSelector("li[class*=group-][class*=inline][class*=mb-8]"));

                if (articles.isEmpty()) {
                    System.out.println("Không tìm thấy yếu tố bài viết trên trang " + page);
                    continue;
                }

                for (WebElement article : articles) {
                    JSONObject entry = createEntryFromArticle(article, url);
                    jsonArray.put(entry);
                }

                // Tạm dừng trong khoảng thời gian cố định trước khi lấy trang tiếp theo
                Thread.sleep(delayInSeconds * 1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static JSONObject createEntryFromArticle(WebElement article, String url) {
        JSONObject entry = new JSONObject();

        try {
            WebElement header = article.findElement(By.cssSelector("div.post-card-inline__header"));
            WebElement meta = header.findElement(By.cssSelector("div.post-card-inline__meta"));
            WebElement authorElement = meta.findElement(By.cssSelector("p.post-card-inline__author"));
            WebElement dateElement = meta.findElement(By.cssSelector("time.post-card-inline__date"));

            WebElement titleElement = article.findElement(By.cssSelector("span.post-card-inline__title"));
            String titleText = titleElement.getText().replaceAll("[\u2018\u2019]", "'").replace("\u2014", "-");
            entry.put("title", titleText);

            WebElement linkElement = article.findElement(By.cssSelector("a.post-card-inline__title-link"));
            String href = linkElement.getAttribute("href");
            entry.put("href", href);

            String authorText = authorElement.getText().replace("by ", "");
            entry.put("author", authorText);

            WebElement stats = article.findElement(By.cssSelector("div.post-card-inline__stats"));
            WebElement viewsElement = stats.findElement(By.cssSelector("span.post-card-inline__stats-item span:last-child"));
            entry.put("views", viewsElement.getText().trim());

            String[] urlParts = url.split("/");
            String tag = urlParts[urlParts.length - 1];
            entry.put("tags", tag);

            String isoDate = dateElement.getAttribute("datetime");
            String formattedDate = formatDate(isoDate);
            entry.put("date", formattedDate);

        } catch (Exception e) {
            System.out.println("Lỗi khi xử lý một bài viết: " + e.getMessage());
        }

        return entry;
    }

    private static void exportToJson(JSONArray jsonArray) {
        try {
            FileWriter fileWriter = new FileWriter("cointelegraph.json");
            fileWriter.write(jsonArray.toString(4));
            fileWriter.close();
            System.out.println("Successfully exported to cointelegraph.json");
        } catch (Exception e) {
            System.out.println("Error processing an article: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String formatDate(String isoDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inputFormat.parse(isoDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return isoDate;
        }
    }
}
