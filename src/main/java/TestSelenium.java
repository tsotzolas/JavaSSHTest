import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TestSelenium {

//    https://stackoverflow.com/questions/11245062/how-can-we-get-exact-time-to-load-a-page-using-selenium-webdriver
//    https://stackoverflow.com/questions/34321662/measuring-total-load-time-of-a-webpage-with-java


    public static void main(String[] args) throws Exception {

//        System.setProperty("webdriver.chrome.driver", "/home/tsotzo/chromedriver");
        System.setProperty("webdriver.gecko.driver", "geckodriver");

//        WebDriver driver = new ChromeDriver();
        WebDriver driver = new FirefoxDriver();

        driver.get("https://app.zapto.org/lab/login.jsf");
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        // time of the process of navigation and page load
        double loadTime = (Double) js.executeScript(
                "return (window.performance.timing.loadEventEnd - window.performance.timing.navigationStart) / 1000");
        System.out.print(loadTime + " seconds"); // 5.15 seconds

        driver.close();
    }

}


