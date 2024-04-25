package data;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TestCointelegraph extends NewsCrawler {



	public TestCointelegraph(int loadMore) {
        super(loadMore);
    }

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        TestCointelegraph testCointelegraph = new TestCointelegraph(5);
        testCointelegraph.crawlAndExport("https://cointelegraph.com", "cointelegraph.json");
    }

    @Override
    protected void crawlNews(String baseUrl) {
        String[] urls = {
                "bitcoin",
                "blockchain",
                "ai",
                "altcoin",
                "ethereum",
                "defi",
                "business",
                "nft"
        };

        JSONArray jsonArray = new JSONArray();

        for (String url : urls) {
            processUrl(baseUrl + "/tags/" + url, jsonArray);
        }

        exportToJson(jsonArray);
    }

    private void processUrl(String url, JSONArray jsonArray) {
        try {
            for (int page = 1; page <= loadMore; page++) {
                driver.get(url + "?page=" + page);

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
               // Thread.sleep(delayInSeconds * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject createEntryFromArticle(WebElement article, String url) {
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
        

	private void exportToJson(JSONArray jsonArray) {
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

	@Override
	protected void exportToJson(String outputFileName) {
		// TODO Auto-generated method stub
		
	}
}
