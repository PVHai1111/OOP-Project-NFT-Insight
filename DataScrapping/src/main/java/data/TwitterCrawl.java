package data;



import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.JavascriptExecutor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TwitterCrawl {

    public static WebDriver driver;
    static JavascriptExecutor js;
    static int loadMore = 50;
    static Long lastHeight = 0L;
    static Set<String> uniqueStrings = new HashSet<>();
    static List<String> authors = new ArrayList<>();
    static List<String> timeAgos = new ArrayList<>();
    static List<String> tags = new ArrayList<>();
    static List<String> replies = new ArrayList<>();
    static List<String> reposts = new ArrayList<>();
    static List<String> likes = new ArrayList<>();
    static List<String> views = new ArrayList<>();
    static List<String> contents = new ArrayList<>();    
    static int i = 0;
    
    private static String convertTimeAgoToDate(String timeAgo) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if (timeAgo.endsWith("m")) {
            // Convert minutes
            int minutesAgo = Integer.parseInt(timeAgo.substring(0, timeAgo.length() - 1));
            calendar.add(Calendar.MINUTE, -minutesAgo);
        } else if (timeAgo.endsWith("h")) {
            // Convert hours
            int hoursAgo = Integer.parseInt(timeAgo.substring(0, timeAgo.length() - 1));
            calendar.add(Calendar.HOUR, -hoursAgo);
        } else {
            // Assuming it's a date like "Dec 18"
            try {
                Date date = new SimpleDateFormat("MMM dd").parse(timeAgo);
                calendar.setTime(date);

                // Adjust the year to the current year
                calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        return dateFormat.format(calendar.getTime());
    }

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;

        driver.get("https://twitter.com");
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.cssSelector("a[href='/login']")).click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.name("text")).sendKeys("oopnhom21");
        driver.findElement(By.name("text")).sendKeys(Keys.ENTER);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.findElement(By.name("password")).sendKeys("thuha2003");
        driver.findElement(By.name("password")).sendKeys(Keys.ENTER);

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement searchInput = driver.findElement(By.xpath("//input[@aria-label='Search query']"));
        searchInput.sendKeys("#NFT");
        searchInput.sendKeys(Keys.ENTER);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (i < loadMore) {
        	List<WebElement> elements = driver.findElements(By.xpath("//div[@class='css-175oi2r r-1iusvr4 r-16y2uox r-1777fci r-kzbkwu']"));
            
        	for (WebElement element : elements) {
        		try {
                    // Add each unique string to the set
                    uniqueStrings.add(element.getText());                 
                } catch (StaleElementReferenceException e) {
                    // Handle StaleElementReferenceException by ignoring and continuing with the loop
                    System.out.println("Stale Element Reference Exception. Trying again...");
                }
            }
            
        	js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
        
        
        for (String uniqueString : uniqueStrings) {
//        	System.out.println(uniqueString);
//        	System.out.println("------------------------------");
        	
        	String[] lines = uniqueString.split("\n");
        	
        	String authorLine = lines[0];
            String timeAgoLine = lines[3];
            String likesLine = lines[lines.length - 2];
            String repostsLine = lines[lines.length -3];
            String repliesLine = lines[lines.length - 4];
            String viewsLine = lines[lines.length - 1];

            // Extracting the author
            String[] authorParts = authorLine.split("\\|");
            String author = authorParts[0].trim();
            authors.add(author);

            // Extracting the time ago
            String timeAgo = timeAgoLine.trim();
            String dateFormatted = convertTimeAgoToDate(timeAgo);
            timeAgos.add(dateFormatted);

            // Extracting the likes, reposts, replies, and views
            likes.add(likesLine.trim());
            reposts.add(repostsLine.trim());
            replies.add(repliesLine.trim());
            views.add(viewsLine.trim());

            // Searching for hashtags in the text
            StringBuilder tagBuilder = new StringBuilder();
            for (String line : lines) {
                if (line.contains("#")) {
                    String[] hashtags = line.split("\\s+");
                    for (String hashtag : hashtags) {
                        if (hashtag.startsWith("#")) {
                            tagBuilder.append(hashtag).append(" ");
                        }
                    }
                }
            }
            tags.add(tagBuilder.toString().trim());
            
            StringBuilder contentBuilder = new StringBuilder();
            for (String line : lines) {
                contentBuilder.append(line).append(" ");
            }
            String content = contentBuilder.toString().trim();
            contents.add(content);
            
            
        }
//        System.out.println("Authors: " + authors);
//        System.out.println("Time Agos: " + timeAgos);
//        System.out.println("Likes: " + likes);
//        System.out.println("Reposts: " + reposts);
//        System.out.println("Replies: " + replies);
//        System.out.println("Views: " + views);
//        System.out.println("Tags: " + tags);
//        System.out.println("------------------------------");
        
        try (FileWriter file = new FileWriter("twitter.json")) {
        	file.write("[\n");
            for (int r = 0; r < authors.size(); r++) {
                String author = authors.get(r);
                String timeAgo = timeAgos.get(r);
                String reply = replies.get(r);
                String repost = reposts.get(r);
                String like = likes.get(r);
                String view = views.get(r);
                String tag = tags.get(r);
                String content = contents.get(r);

                // Write the formatted JSON object to the file with a line break
                file.write("	{\n");
                file.write("		\"author\":\"" + author + "\",\n");
                file.write("		\"date\":\"" + timeAgo + "\",\n");
                file.write("		\"replies\":\"" + reply + "\",\n");
                file.write("		\"reposts\":\"" + repost + "\",\n");
                file.write("		\"likes\":\"" + like + "\",\n");
                file.write("		\"views\":\"" + view + "\",\n");
                file.write("		\"hashtags\":\"" + tag + "\",\n");
                file.write("		\"content\":\"" + content + "\"\n");
                file.write("	}");
                if (i < authors.size() - 1) {
                    file.write(",\n");
                } else {
                    file.write("\n");
                }
            }
            file.write("\n]\n");
            System.out.println("JSON objects written to twitter.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        driver.quit();
    }
}


